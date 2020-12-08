package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.dto.LanguageDto;
import ar.edu.itba.paw.webapp.dto.SnippetDto;
import ar.edu.itba.paw.webapp.dto.SearchDto;
import ar.edu.itba.paw.webapp.utility.*;
import ar.edu.itba.paw.webapp.form.DeleteForm;
import ar.edu.itba.paw.webapp.form.ItemSearchForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.utility.Constants.*;

@Component
@Path("languages")
public class LanguagesController {
    @Autowired
    private LanguageService languageService;
    @Autowired
    private SnippetService snippetService;
    @Autowired
    private LoginAuthentication loginAuthentication;
//    @Autowired
//    private TagService tagService;
    @Autowired
    private RoleService roleService;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private MessageSource messageSource;

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(LanguagesController.class);

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getLanguagesByPage(final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page, final @QueryParam(QUERY_PARAM_SHOW_EMPTY) @DefaultValue("true") boolean showEmpty) {
        final List<LanguageDto> languages = languageService.getAllLanguages(showEmpty, page, TAG_PAGE_SIZE).stream().map(l -> LanguageDto.fromLanguage(l, uriInfo)).collect(Collectors.toList());
        final int languagesCount = this.languageService.getAllLanguagesCount(showEmpty);
        int pageCount = PagingHelper.CalculateTotalPages(languagesCount, LANGUAGE_PAGE_SIZE);

        Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<LanguageDto>>(languages) {
        });
        ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
        ResponseHelper.AddTotalItemsAttribute(builder, languagesCount);
        return builder.build();
    }

    @GET
    @Path("/all")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getAllLanguages() {
        final List<LanguageDto> languages = languageService.getAllLanguages().stream().map(l -> LanguageDto.fromLanguage(l, uriInfo)).collect(Collectors.toList());
        Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<LanguageDto>>(languages) {
        });
        return builder.build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getLanguageById(final @PathParam(PATH_PARAM_ID) long id){
        Optional<Language> maybeLang = this.languageService.findById(id);
        if (maybeLang.isPresent()) {
            LanguageDto langDto = LanguageDto.fromLanguage(maybeLang.get(), uriInfo);
            Response.ResponseBuilder builder = Response.ok(new GenericEntity<LanguageDto>(langDto) {
            });
            return builder.build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/snippets")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getSnippetsForLanguage(final @PathParam(PATH_PARAM_ID) long id, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page){
        Optional<Language> lang = this.languageService.findById(id);
        if (lang.isPresent()) {
            final List<SnippetDto> snippets = this.snippetService.getSnippetsWithLanguage(id, page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, uriInfo, LocaleContextHolder.getLocale())).collect(Collectors.toList());
            final int snippetCount = this.snippetService.getAllSnippetsByLanguageCount(id);
            int pageCount = PagingHelper.CalculateTotalPages(snippetCount, SNIPPET_PAGE_SIZE);

            Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
            });
            ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
            ResponseHelper.AddTotalItemsAttribute(builder, snippetCount);
            return builder.build();
        } else {
            LOGGER.error("No language found with id {}", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/snippets/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getSnippetsForLanguage(final @PathParam(PATH_PARAM_ID) long id,
                                           final @BeanParam SearchDto searchDto,
                                           final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page){
        Optional<Language> maybeLang = this.languageService.findById(id);
        if (maybeLang.isPresent()) {
            final List<SnippetDto> snippets = SearchHelper.FindByCriteria(this.snippetService, searchDto.getType(), searchDto.getQuery(), SnippetDao.Locations.LANGUAGES, searchDto.getSort(), null, id, page)
                    .stream().map(s -> SnippetDto.fromSnippet(s, uriInfo, LocaleContextHolder.getLocale())).collect(Collectors.toList());

            int totalSnippetCount = SearchHelper.GetSnippetByCriteriaCount(this.snippetService, searchDto.getType(), searchDto.getQuery(), SnippetDao.Locations.LANGUAGES, null, id);
            final int pageCount = PagingHelper.CalculateTotalPages(totalSnippetCount, SNIPPET_PAGE_SIZE);

            Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
            });
            ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
            ResponseHelper.AddTotalItemsAttribute(builder, totalSnippetCount);
            return builder.build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE  // TODO --> esto deberia ser un post al ser borrado logico?
    @Path("/{id}")
    public Response deleteLanguage(final @PathParam(PATH_PARAM_ID) long id){
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if ( currentUser != null && roleService.isAdmin(currentUser.getId())){
            this.languageService.removeLanguage(id);
            LOGGER.debug("Admin removed language with id {}", id);
            return Response.noContent().build();
        } else {
            LOGGER.error("No user logged in or logged in user not admin but language {} is trying to be deleted", id);
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    ////////////////////////////////////////// OLD ///////////////////////////////////////////////

    @Deprecated
    @RequestMapping("/languages")
    public ModelAndView showAllLanguages(@ModelAttribute("itemSearchForm") final ItemSearchForm searchForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        ModelAndView mav = new ModelAndView("tagAndLanguages/languages");
        Collection<Language> allLanguages = this.languageService.getAllLanguages(searchForm.isShowEmpty(), page, LANGUAGE_PAGE_SIZE);

        for (Language language : allLanguages) {
            this.snippetService.analizeSnippetsUsing(language);
        }
        int languageCount = this.languageService.getAllLanguagesCount(searchForm.isShowEmpty());
        mav.addObject("pages", (languageCount/ LANGUAGE_PAGE_SIZE) + (languageCount % LANGUAGE_PAGE_SIZE == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("searchContext","languages/");
        mav.addObject("languages", allLanguages);
        mav.addObject("searching", false);
        mav.addObject("totalLanguagesCount", languageCount);
        mav.addObject("itemSearchContext", "languages/");
        return mav;
    }

    @Deprecated
    @RequestMapping("/languages/search")
    public ModelAndView searchInAllTags(@ModelAttribute("itemSearchForm") final ItemSearchForm searchForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page){
        final ModelAndView mav = new ModelAndView("tagAndLanguages/languages");
        Collection<Language> allLanguages = this.languageService.findAllLanguagesByName(searchForm.getName(), searchForm.isShowEmpty(), page, LANGUAGE_PAGE_SIZE);
        int languageCount = this.languageService.getAllLanguagesCountByName(searchForm.getName(), searchForm.isShowEmpty());

        for (Language language : allLanguages) {
            this.snippetService.analizeSnippetsUsing(language);
        }
        mav.addObject("pages", (languageCount/ LANGUAGE_PAGE_SIZE) + (languageCount % LANGUAGE_PAGE_SIZE == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("searchContext","languages/");
        mav.addObject("languages", allLanguages);
        mav.addObject("searching", true);
        mav.addObject("totalLanguagesCount", languageCount);
        mav.addObject("itemSearchContext", "languages/");
        return mav;
    }

    @Deprecated
    @RequestMapping("/languages/{langId}")
    public ModelAndView showSnippetsForLang(@PathVariable("langId") long langId, @ModelAttribute("deleteForm") final DeleteForm deleteForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page){
        ModelAndView mav = new ModelAndView("tagAndLanguages/languageSnippets");

        // Retrieve the tag
        Optional<Language> language = languageService.findById(langId);
        if (!language.isPresent()) {
            LOGGER.error("No language found with id {}", langId);
//            throw new LanguageNotFoundException(messageSource.getMessage("error.404.language", new Object[]{langId}, LocaleContextHolder.getLocale()));
        }
        Collection<Snippet> snippets = snippetService.getSnippetsWithLanguage(langId, page, SNIPPET_PAGE_SIZE);
        int totalSnippetCount = this.snippetService.getAllSnippetsByLanguageCount(langId);

        mav.addObject("pages", totalSnippetCount/SNIPPET_PAGE_SIZE + (totalSnippetCount % SNIPPET_PAGE_SIZE == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("language", language.get());
        mav.addObject("searchContext","languages/"+langId+"/");
        mav.addObject("searching", false);
        mav.addObject("totalSnippetCount", totalSnippetCount);
        mav.addObject("snippetList", snippets);
        MavHelper.addSnippetCardFavFormAttributes(mav, this.loginAuthentication.getLoggedInUser(), snippets);
        return mav;
    }

    @Deprecated
    @Transactional
    @RequestMapping("/languages/{langId}/delete")
    public ModelAndView deleteLanguage (@PathVariable("langId") long langId, @ModelAttribute("deleteForm") final DeleteForm deleteForm) {
        User currentUser = loginAuthentication.getLoggedInUser();
        if ( currentUser != null && roleService.isAdmin(currentUser.getId())){
            this.languageService.removeLanguage(langId);
            LOGGER.debug("Admin removed language with id {}", langId);
        } else {
            LOGGER.error("No user logged in or logged in user not admin but language {} is trying to be deleted", langId);
//            throw new ForbiddenAccessException(messageSource.getMessage("error.403.admin.delete", null, LocaleContextHolder.getLocale()));
        }
        return new ModelAndView("redirect:/languages/" + langId);
    }

    @Deprecated
    @ModelAttribute
    public void addAttributes(Model model, @Valid final SearchForm searchForm) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
//        MavHelper.addCurrentUserAttributes(model, currentUser, tagService, roleService);
        model.addAttribute("searchForm", searchForm);
    }
}