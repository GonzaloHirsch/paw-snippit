package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.LanguageService;
import ar.edu.itba.paw.interfaces.service.RoleService;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.utility.PagingHelper;
import ar.edu.itba.paw.webapp.utility.ResponseHelper;
import ar.edu.itba.paw.webapp.utility.SearchHelper;
import ar.edu.itba.paw.webapp.utility.UserHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
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
    @Autowired
    private RoleService roleService;

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(LanguagesController.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response languageCreate(@Valid ItemCreateDto langCreateDto) {
        User loggedInUser = this.loginAuthentication.getLoggedInUser();
        // Check if user is admin
        if (loggedInUser == null || !this.roleService.isAdmin(loggedInUser.getId())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        // Create language
        long langId = this.languageService.addLanguage(langCreateDto.getName());
        // Add URI to response
        final URI langUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(langId)).build();
        return Response.created(langUri).build();
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getLanguagesByPage(final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page, final @QueryParam(QUERY_PARAM_SHOW_EMPTY) @DefaultValue("true") boolean showEmpty) {
        // Get languages
        final Collection<Language> rawLanguages = this.languageService.getAllLanguages(showEmpty, page, LANGUAGE_PAGE_SIZE);
        // Analyze if they are empty or not
        rawLanguages.forEach(l -> this.snippetService.analizeSnippetsUsing(l));
        // Generate DTOs
        final List<LanguageWithEmptyDto> languages = rawLanguages.stream().map(l -> LanguageWithEmptyDto.fromLanguage(l, uriInfo)).collect(Collectors.toList());
        final int languagesCount = this.languageService.getAllLanguagesCount(showEmpty);
        int pageCount = PagingHelper.CalculateTotalPages(languagesCount, LANGUAGE_PAGE_SIZE);

        // If the page asked is greater than the one existing
        if (page > pageCount && pageCount > 0){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<LanguageWithEmptyDto>>(languages) {
        });
        ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
        ResponseHelper.AddTotalItemsAttribute(builder, languagesCount);
        return builder.build();
    }

    @GET
    @Path("/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response searchInAllTags(final @BeanParam LanguageSearchDto languageSearchDto, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page){
        final Collection<Language> rawLanguages = this.languageService.findAllLanguagesByName(languageSearchDto.getQuery(), languageSearchDto.isShowEmpty(), page, LANGUAGE_PAGE_SIZE);
        // Analyze if they are empty or not
        rawLanguages.forEach(l -> this.snippetService.analizeSnippetsUsing(l));
        // Generate DTOs
        final List<LanguageWithEmptyDto> languages = rawLanguages.stream().map(l -> LanguageWithEmptyDto.fromLanguage(l, uriInfo)).collect(Collectors.toList());
        int languagesCount = this.languageService.getAllLanguagesCountByName(languageSearchDto.getQuery(), languageSearchDto.isShowEmpty());
        int pageCount = PagingHelper.CalculateTotalPages(languagesCount, LANGUAGE_PAGE_SIZE);

        // If the page asked is greater than the one existing
        if (page > pageCount && pageCount > 0){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<LanguageWithEmptyDto>>(languages) {
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
    @Path("/exists")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getLanguageByName(final @QueryParam(QUERY_PARAM_NAME) String name) {
        // Check that user is admin
        User loggedInUser = this.loginAuthentication.getLoggedInUser();
        if (loggedInUser == null || !this.roleService.isAdmin(loggedInUser.getId())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (name == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(BooleanDto.fromBoolean(this.languageService.languageExists(name))).build();
    }

    @GET
    @Path("/{id}/snippets")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getSnippetsForLanguage(final @PathParam(PATH_PARAM_ID) long id, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page){
        Optional<Language> lang = this.languageService.findById(id);
        if (lang.isPresent()) {
            final List<SnippetDto> snippets = this.snippetService.getSnippetsWithLanguage(id, page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, UserHelper.GetLoggedUserId(this.loginAuthentication), uriInfo, LocaleContextHolder.getLocale())).collect(Collectors.toList());
            final int snippetCount = this.snippetService.getAllSnippetsByLanguageCount(id);
            int pageCount = PagingHelper.CalculateTotalPages(snippetCount, SNIPPET_PAGE_SIZE);

            // If the page asked is greater than the one existing
            if (page > pageCount && pageCount > 0){
                return Response.status(Response.Status.NOT_FOUND).build();
            }

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
                    .stream().map(s -> SnippetDto.fromSnippet(s, UserHelper.GetLoggedUserId(this.loginAuthentication), uriInfo, LocaleContextHolder.getLocale())).collect(Collectors.toList());

            int totalSnippetCount = SearchHelper.GetSnippetByCriteriaCount(this.snippetService, searchDto.getType(), searchDto.getSort(), searchDto.getQuery(), SnippetDao.Locations.LANGUAGES, null, id);
            final int pageCount = PagingHelper.CalculateTotalPages(totalSnippetCount, SNIPPET_PAGE_SIZE);

            // If the page asked is greater than the one existing
            if (page > pageCount && pageCount > 0){
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
            });
            ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
            ResponseHelper.AddTotalItemsAttribute(builder, totalSnippetCount);
            return builder.build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteLanguage(final @PathParam(PATH_PARAM_ID) long id){
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if ( currentUser != null && roleService.isAdmin(currentUser.getId())){
            this.languageService.removeLanguage(id);
            LOGGER.debug("Admin removed language with id {}", id);
            return Response.noContent().build();
        } else {
            LOGGER.error("No user logged in or logged in user not admin but language {} is trying to be deleted", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}