package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.RoleService;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.dto.SnippetDto;
import ar.edu.itba.paw.webapp.dto.TagDto;
import ar.edu.itba.paw.webapp.utility.Constants;
import ar.edu.itba.paw.webapp.exception.ForbiddenAccessException;
import ar.edu.itba.paw.webapp.exception.TagNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.utility.MavHelper;
import ar.edu.itba.paw.webapp.utility.PagingHelper;
import ar.edu.itba.paw.webapp.utility.ResponseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.utility.Constants.*;

@Component
@Path("tags")
public class TagsController {

    @Autowired
    private TagService tagService;
    @Autowired
    private SnippetService snippetService;
    @Autowired
    private LoginAuthentication loginAuthentication;
    @Autowired
    private RoleService roleService;

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(TagsController.class);

    // TODO: ADD CREATE TAG

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getAllTags(final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page, final @QueryParam(QUERY_PARAM_SHOW_EMPTY) @DefaultValue("true") boolean showEmpty, final @QueryParam(QUERY_PARAM_SHOW_ONLY_FOLLOWING) @DefaultValue("false") boolean showOnlyFollowing) {
        User currentUser = loginAuthentication.getLoggedInUser();
        final List<TagDto> tags = tagService.getAllTags(showEmpty, showOnlyFollowing, currentUser != null ? currentUser.getId() : null, page, TAG_PAGE_SIZE).stream().map(t -> TagDto.fromTag(t, uriInfo)).collect(Collectors.toList());
        int pageCount = PagingHelper.CalculateTotalPages(this.tagService.getAllTagsCount(showEmpty, showOnlyFollowing, currentUser != null ? currentUser.getId() : null), TAG_PAGE_SIZE);

        Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<TagDto>>(tags) {
        });
        ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
        return builder.build();
    }

    /*@RequestMapping("/tags/search")
    public ModelAndView searchInAllTags(@ModelAttribute("itemSearchForm") final ItemSearchForm searchForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page){
        final ModelAndView mav = new ModelAndView("tagAndLanguages/tags");
        User currentUser = loginAuthentication.getLoggedInUser();
        Collection<Tag> allTags = this.tagService.findTagsByName(searchForm.getName(), searchForm.isShowEmpty(), searchForm.isShowOnlyFollowing(), currentUser != null ? currentUser.getId() : null, page, TAG_PAGE_SIZE);
        int tagCount = this.tagService.getAllTagsCountByName(searchForm.getName(), searchForm.isShowEmpty(), searchForm.isShowOnlyFollowing(), currentUser != null ? currentUser.getId() : null);
        return this.setTagsWithPage(mav, allTags, currentUser, tagCount, page, true);
    }*/

    /*private ModelAndView setTagsWithPage(ModelAndView mav, Collection<Tag> allTags, User currentUser, int tagCount, int page, boolean searching) {
        mav.addObject("pages", (tagCount/TAG_PAGE_SIZE) + (tagCount % TAG_PAGE_SIZE == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("searchContext","tags/");
        mav.addObject("tags", allTags);
        mav.addObject("itemSearchContext", "tags/");
        mav.addObject("loggedUser", currentUser);
        mav.addObject("searching", searching);
        mav.addObject("totalTagsCount", tagCount);

        for (Tag tag : allTags) {
            if (currentUser != null) {
                *//* Follow form quick action *//*
                FollowForm followForm = new FollowForm();
                followForm.setFollows(currentUser.getFollowedTags().contains(tag));
                mav.addObject("followIconForm" + tag.getId().toString(), followForm);
                *//* Tag snippet amount count *//*
            }
            this.snippetService.analizeSnippetsUsing(tag);
        }
        this.addAttributes(mav);
        return mav;
    }*/

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getSnippetsForTag(final @PathParam(PATH_PARAM_ID) long id, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page){
        Optional<Tag> maybeTag = this.tagService.findTagById(id);
        if (maybeTag.isPresent()) {
            final List<SnippetDto> snippets = this.snippetService.findSnippetsForTag(id, page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, uriInfo)).collect(Collectors.toList());
            int pageCount = PagingHelper.CalculateTotalPages(this.snippetService.getAllSnippetsByTagCount(id), TAG_PAGE_SIZE);

            Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
            });
            ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
            return builder.build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /*public ModelAndView showSnippetsForTag(@PathVariable("tagId") long tagId,
                                           @ModelAttribute("followForm") final FollowForm followForm,
                                           @ModelAttribute("deleteForm") final DeleteForm deleteForm,
                                           final @RequestParam(value = "page", required = false, defaultValue = "1") int page){

        ModelAndView mav = new ModelAndView("tagAndLanguages/tagSnippets");

        *//* Retrieve the tag *//*
        Optional<Tag> tag = this.tagService.findTagById(tagId);
        if (!tag.isPresent()) {
            LOGGER.warn("No tag found with id {}", tagId);
            throw new TagNotFoundException(messageSource.getMessage("error.404.tag", new Object[]{tagId}, LocaleContextHolder.getLocale()));
        }
        *//* If user is logged in, check if they follow the tag *//*
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser != null){
            followForm.setFollows(this.tagService.userFollowsTag(currentUser.getId(), tagId));
        }

        Collection<Snippet> snippets = snippetService.findSnippetsForTag(tagId, page, SNIPPET_PAGE_SIZE);
        int totalSnippetCount = this.snippetService.getAllSnippetsByTagCount(tag.get().getId());
        mav.addObject("pages", totalSnippetCount/SNIPPET_PAGE_SIZE + (totalSnippetCount % SNIPPET_PAGE_SIZE == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("tag", tag.get());
        mav.addObject("searching", false);
        mav.addObject("totalSnippetCount", totalSnippetCount);
        mav.addObject("searchContext","tags/"+tagId+"/");
        mav.addObject("snippetList", snippets);
        this.addAttributes(mav);
        MavHelper.addSnippetCardFavFormAttributes(mav, currentUser, snippets);
        return mav;
    }*/

    @POST
    @Path("/{id}")
    public Response followTag(final @PathParam(PATH_PARAM_ID) long id){
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser != null) {
            // TODO: CHANGE TO DETERMINE IF FOLLOWED INSIDE SERVICE/DAO
            //this.tagService.updateFollowing(currentUser.getId(), id, followForm.isFollows());
            return Response.noContent().build();
        } else {
            LOGGER.warn("Inside the follow form of tag {} without a logged in user", id);
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTag(final @PathParam(PATH_PARAM_ID) long id){
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if ( currentUser != null && roleService.isAdmin(currentUser.getId())){
            this.tagService.removeTag(id);
            LOGGER.debug("Admin deleted tag with id {}", id);
            return Response.noContent().build();
        } else {
            LOGGER.warn("No user logged in or logged in user not admin but attempting to delete tag {}", id);
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    /*@RequestMapping(value="/tags/{tagId}/follow", method= RequestMethod.POST)
    public ModelAndView followTag(
            HttpServletRequest request,
            @ModelAttribute("tagId") @PathVariable("tagId") long tagId,
            @ModelAttribute("followForm") final FollowForm followForm
    ) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser != null) {
            this.tagService.updateFollowing(currentUser.getId(), tagId, followForm.isFollows());
        } else {
            LOGGER.warn("Inside the follow form of tag {} without a logged in user", tagId);
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.follow", null, LocaleContextHolder.getLocale()));
        }
        String referer = request.getHeader(Constants.REFERER);
        String redirect = referer != null ? referer : ("/tags/" + tagId);
        return new ModelAndView("redirect:" + redirect);
    }
    
    @RequestMapping(value = "/tags/{tagId}/delete",  method= RequestMethod.POST)
    public ModelAndView deleteTag(@PathVariable("tagId") long tagId, @ModelAttribute("deleteForm") final DeleteForm deleteForm) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if ( currentUser != null && roleService.isAdmin(currentUser.getId())){
            this.tagService.removeTag(tagId);
            LOGGER.debug("Admin deleted tag with id {}", tagId);
        } else {
            LOGGER.warn("No user logged in or logged in user not admin but attempting to delete tag {}", tagId);
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.admin", null, LocaleContextHolder.getLocale()));
        }
        return new ModelAndView("redirect:/tags");
    }*/

    /* Are not in the @ModelAttribute method because I do not want the values in the URL */
    /*private void addAttributes(ModelAndView mav) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        Collection<Tag> userTags =  Collections.emptyList();
        Collection<Tag> allFollowedTags = Collections.emptyList();
        Collection<String> userRoles = Collections.emptyList();

        if (currentUser != null) {
            userTags = this.tagService.getMostPopularFollowedTagsForUser(currentUser.getId(), Constants.MENU_FOLLOWING_TAG_AMOUNT);
            allFollowedTags = this.tagService.getFollowedTagsForUser(currentUser.getId());
            this.userService.updateLocale(currentUser.getId(), LocaleContextHolder.getLocale());
            userRoles = this.roleService.getUserRoles(currentUser.getId());
        }
        mav.addObject("currentUser", currentUser);
        mav.addObject("userTags", userTags);
        mav.addObject("userTagsCount", userTags.isEmpty() ? 0 : allFollowedTags.size() - userTags.size());
        mav.addObject("userRoles", userRoles);
    }*/

   /* @ModelAttribute
    public void addAttributes(Model model, @Valid final SearchForm searchForm) {
        model.addAttribute("searchForm", searchForm);
    }*/
}