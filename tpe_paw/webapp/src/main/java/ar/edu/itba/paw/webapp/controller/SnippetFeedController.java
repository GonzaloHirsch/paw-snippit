package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.RoleService;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.dto.SnippetDto;
import ar.edu.itba.paw.webapp.dto.form.ExploreFormDto;
import ar.edu.itba.paw.webapp.dto.form.SearchFormDto;
import ar.edu.itba.paw.webapp.form.ExploreForm;
import ar.edu.itba.paw.webapp.utility.*;
import ar.edu.itba.paw.webapp.exception.ForbiddenAccessException;
import ar.edu.itba.paw.webapp.form.FavoriteForm;
import ar.edu.itba.paw.webapp.form.FollowForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.utility.Constants.*;

@Component
@Path("/snippets")
public class SnippetFeedController {

    @Autowired
    private SnippetService snippetService;
//    @Autowired
    private LoginAuthentication loginAuthentication;
//    @Autowired
    private TagService tagService;
//    @Autowired
    private RoleService roleService;
//    @Autowired
    private UserService userService;
//    @Autowired
    private MessageSource messageSource;

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(SnippetFeedController.class);
    private static final String FOLLOWING = "following/";
    private static final String FAVORITES = "favorites/";
    private static final String UPVOTED = "upvoted/";
    private static final String FLAGGED = "flagged/";

    private final static Map<String, SnippetDao.Types> typesMap;
    static {
        final Map<String, SnippetDao.Types> types = new HashMap<>();
        types.put(null, SnippetDao.Types.TITLE);
        types.put("reputation", SnippetDao.Types.REPUTATION);
        types.put("votes", SnippetDao.Types.VOTES);
        types.put("date", SnippetDao.Types.DATE);
        types.put("title", SnippetDao.Types.TITLE);
        typesMap = Collections.unmodifiableMap(types);
    }

    private final static Map<String, SnippetDao.Orders> ordersMap;
    static {
        final Map<String, SnippetDao.Orders> orders = new HashMap<>();
        orders.put("asc", SnippetDao.Orders.ASC);
        orders.put("desc", SnippetDao.Orders.DESC);
        orders.put("no", SnippetDao.Orders.NO);
        ordersMap = Collections.unmodifiableMap(orders);
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getHomeSnippetFeed(final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {
        final List<SnippetDto> snippets = this.snippetService.getAllSnippets(page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, uriInfo)).collect(Collectors.toList());
        final int pageCount = PagingHelper.CalculateTotalPages(this.snippetService.getAllSnippetsCount(), SNIPPET_PAGE_SIZE);

        Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
        });
        ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
        return builder.build();
    }

    @GET
    @Path("/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response searchInHome(final @BeanParam SearchFormDto searchForm, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {

        final List<SnippetDto> snippets = SearchHelper.FindByCriteria(this.snippetService, searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.HOME, searchForm.getSort(), null, null, page)
                .stream().map(s -> SnippetDto.fromSnippet(s, uriInfo)).collect(Collectors.toList());

        int totalSnippetCount = SearchHelper.GetSnippetByCriteriaCount(this.snippetService, searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.HOME, null, null);
        final int pageCount = PagingHelper.CalculateTotalPages(totalSnippetCount, SNIPPET_PAGE_SIZE);

        //TODO Previously added the heart attributes

        Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
        });
        ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
        return builder.build();
    }

    @GET
    @Path("/explore/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response exploreSearch(final @BeanParam ExploreFormDto exploreFormDto, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {

        Instant minDate = null;
        Instant maxDate = null;
        if (exploreFormDto.getMinDate() != null){
            minDate = exploreFormDto.getMinDate().toInstant();
        }
        if (exploreFormDto.getMaxDate() != null){
            maxDate = exploreFormDto.getMaxDate().toInstant();
        }
        Collection<Snippet> snippetsCollection = this.snippetService.findSnippetByDeepCriteria(
                minDate, maxDate,
                exploreFormDto.getMinRep(), exploreFormDto.getMaxRep(),
                exploreFormDto.getMinVotes(), exploreFormDto.getMaxVotes(),
                exploreFormDto.getLanguage() == -1 ? null : exploreFormDto.getLanguage(), exploreFormDto.getTag() == -1 ? null : exploreFormDto.getTag(),
                exploreFormDto.getTitle(), exploreFormDto.getUsername(),
                ordersMap.get(exploreFormDto.getSort()), typesMap.get(exploreFormDto.getField()), exploreFormDto.getIncludeFlagged(), page, SNIPPET_PAGE_SIZE);
        int snippetCount =  this.snippetService.getSnippetByDeepCriteriaCount(
                minDate, maxDate,
                exploreFormDto.getMinRep(), exploreFormDto.getMaxRep(),
                exploreFormDto.getMinVotes(), exploreFormDto.getMaxVotes(),
                exploreFormDto.getLanguage() == -1 ? null : exploreFormDto.getLanguage(), exploreFormDto.getTag() == -1 ? null : exploreFormDto.getTag(),
                exploreFormDto.getTitle(), exploreFormDto.getUsername(),
                exploreFormDto.getIncludeFlagged());

        final List<SnippetDto> snippets = snippetsCollection.stream().map(s -> SnippetDto.fromSnippet(s, uriInfo)).collect(Collectors.toList());
        final int pageCount = PagingHelper.CalculateTotalPages(snippetCount, SNIPPET_PAGE_SIZE);

        Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
        });
        ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
        return builder.build();
    }


    // TODO Favorites moved to UserController
    /*@RequestMapping("/favorites")
    public ModelAndView getFavoritesSnippetFeed(final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("index");

        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) this.logAndThrow(FAVORITES);

        Collection<Snippet> snippets = this.snippetService.getAllFavoriteSnippets(currentUser.getId(), page, SNIPPET_PAGE_SIZE);
        int totalSnippetCount = this.snippetService.getAllFavoriteSnippetsCount(currentUser.getId());

        this.addModelAttributesHelper(mav, currentUser, totalSnippetCount, page, snippets, FAVORITES);

        return mav;
    }*/

    // TODO Following moved to UserController
    /*@RequestMapping("/following")
    public ModelAndView getFollowingSnippetFeed(final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("snippet/snippetFollowing");

        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) this.logAndThrow(FOLLOWING);

        Collection<Snippet> snippets = this.snippetService.getAllFollowingSnippets(currentUser.getId(), page, SNIPPET_PAGE_SIZE);
        int totalSnippetCount = this.snippetService.getAllFollowingSnippetsCount(currentUser.getId());
        this.addModelAttributesHelper(mav, currentUser, totalSnippetCount, page, snippets, FOLLOWING);

        *//* Show up to 25 tags -> most popular + non empty *//*
        MavHelper.addTagChipUnfollowFormAttributes(mav, this.tagService.getSomeOrderedFollowedTagsForUser(currentUser.getId(), Constants.FOLLOWING_FEED_TAG_AMOUNT), currentUser.getFollowedTags().size());
        return mav;
    }*/

    // TODO Upvoted moved to UserController
    /*@RequestMapping("/upvoted")
    public ModelAndView getUpVotedSnippetFeed(final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("index");

        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) this.logAndThrow(UPVOTED);

        Collection<Snippet> snippets = this.snippetService.getAllUpVotedSnippets(currentUser.getId(), page, SNIPPET_PAGE_SIZE);
        int totalSnippetCount = this.snippetService.getAllUpvotedSnippetsCount(currentUser.getId());

        this.addModelAttributesHelper(mav, currentUser, totalSnippetCount, page, snippets, UPVOTED);

        return mav;
    }*/

   /* @RequestMapping("/flagged")
    public ModelAndView getFlaggedSnippetFeed(final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("index");

        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null || !roleService.isAdmin(currentUser.getId())) this.logAndThrow(FLAGGED);

        Collection<Snippet> snippets = this.snippetService.getAllFlaggedSnippets(page, SNIPPET_PAGE_SIZE);
        int totalSnippetCount = this.snippetService.getAllFlaggedSnippetsCount();

        this.addModelAttributesHelper(mav, currentUser, totalSnippetCount, page, snippets, FLAGGED);

        return mav;
    }*/

    /*private void addModelAttributesHelper(ModelAndView mav, User currentUser, int snippetCount, int page, Collection<Snippet> snippets, String searchContext) {
        mav.addObject("pages", snippetCount / SNIPPET_PAGE_SIZE + (snippetCount % SNIPPET_PAGE_SIZE == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("snippetList", snippets);
        mav.addObject("totalSnippetCount", snippetCount);
        mav.addObject("searchContext", searchContext);
        mav.addObject("searching", false);

        MavHelper.addSnippetCardFavFormAttributes(mav, currentUser, snippets);
    }
*/
    /*@ModelAttribute
    public void addAttributes(Model model, @Valid final SearchForm searchForm) {
        User currentUser = this.loginAuthentication.getLoggedInUser();

        if (currentUser != null) {
            this.userService.updateLocale(currentUser.getId(), LocaleContextHolder.getLocale());
        }
        MavHelper.addCurrentUserAttributes(model, currentUser, tagService, roleService);
        model.addAttribute("searchForm", searchForm);
    }*/

  /*  private void logAndThrow(String location) {
        LOGGER.warn("Inside {} with no logged in user", location);
        throw new ForbiddenAccessException(messageSource.getMessage("error.403", new Object[]{location}, LocaleContextHolder.getLocale()));
    }*/
}
