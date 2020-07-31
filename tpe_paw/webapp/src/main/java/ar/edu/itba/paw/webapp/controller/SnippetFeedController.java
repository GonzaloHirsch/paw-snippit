package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.dto.SnippetDto;
import ar.edu.itba.paw.webapp.dto.SnippetWithVoteDto;
import ar.edu.itba.paw.webapp.dto.VoteFormDto;
import ar.edu.itba.paw.webapp.dto.ExploreDto;
import ar.edu.itba.paw.webapp.dto.SearchDto;
import ar.edu.itba.paw.webapp.dto.SnippetCreateDto;
import ar.edu.itba.paw.webapp.utility.*;
import ar.edu.itba.paw.webapp.exception.ForbiddenAccessException;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.utility.Constants.*;

@Component
@Path("/snippets")
public class SnippetFeedController {

    @Autowired
    private SnippetService snippetService;
    @Autowired
    private UserService userService;
    @Autowired
    private VoteService voteService;
//    @Autowired
    private LoginAuthentication loginAuthentication;
    //    @Autowired
    private TagService tagService;
    //    @Autowired
    private RoleService roleService;
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
    public Response searchInHome(final @BeanParam SearchDto searchForm, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {

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
    public Response exploreSearch(final @Valid @BeanParam ExploreDto exploreDto, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {

        Instant minDate = null;
        Instant maxDate = null;
//        if (exploreDto.getMinDate() != null){
//            minDate = exploreDto.getMinDate().toInstant();
//        }
//        if (exploreDto.getMaxDate() != null){
//            maxDate = exploreDto.getMaxDate().toInstant();
//        }
        Collection<Snippet> snippetsCollection = this.snippetService.findSnippetByDeepCriteria(
                minDate, maxDate,
                exploreDto.getMinRep(), exploreDto.getMaxRep(),
                exploreDto.getMinVotes(), exploreDto.getMaxVotes(),
                exploreDto.getLanguage() == -1 ? null : exploreDto.getLanguage(), exploreDto.getTag() == -1 ? null : exploreDto.getTag(),
                exploreDto.getTitle(), exploreDto.getUsername(),
                ordersMap.get(exploreDto.getSort()), typesMap.get(exploreDto.getField()), exploreDto.getIncludeFlagged(), page, SNIPPET_PAGE_SIZE);
        int snippetCount =  this.snippetService.getSnippetByDeepCriteriaCount(
                minDate, maxDate,
                exploreDto.getMinRep(), exploreDto.getMaxRep(),
                exploreDto.getMinVotes(), exploreDto.getMaxVotes(),
                exploreDto.getLanguage() == -1 ? null : exploreDto.getLanguage(), exploreDto.getTag() == -1 ? null : exploreDto.getTag(),
                exploreDto.getTitle(), exploreDto.getUsername(),
                exploreDto.getIncludeFlagged());

        final List<SnippetDto> snippets = snippetsCollection.stream().map(s -> SnippetDto.fromSnippet(s, uriInfo)).collect(Collectors.toList());
        final int pageCount = PagingHelper.CalculateTotalPages(snippetCount, SNIPPET_PAGE_SIZE);

        Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
        });
        ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
        return builder.build();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response snippetCreate(@Valid SnippetCreateDto createDto) {
        // TODO --> JWT Tokens
        User loggedInUser = this.loginAuthentication.getLoggedInUser();
        if (loggedInUser == null) {
            LOGGER.error(messageSource.getMessage("error.403.snippet.create", null, Locale.ENGLISH));
            return Response.status(Response.Status.FORBIDDEN).build();
            // throw new ForbiddenAccessException(this.messageSource.getMessage("error.403.snippet.create", null, LocaleContextHolder.getLocale()));
        } else if (this.roleService.isAdmin(loggedInUser.getId())) {
            LOGGER.error(messageSource.getMessage("error.403.admin.snippet.create", null, Locale.ENGLISH));
            return Response.status(Response.Status.FORBIDDEN).build();
            // throw new ForbiddenAccessException(this.messageSource.getMessage("error.403.admin.snippet.create", null, LocaleContextHolder.getLocale()));
        }
        /* Extra validation
        this.validator.validateTagsExists(snippetCreateForm.getTags(), errors, LocaleContextHolder.getLocale()); */

        Instant dateCreated = Instant.now();
        Long snippetId = this.snippetService.createSnippet(loggedInUser, createDto.getTitle(), createDto.getDescription(), createDto.getCode(), dateCreated, createDto.getLanguage(), createDto.getTags());

        if (snippetId == null) {
            LOGGER.error("Snippet creation was unsuccessful. Return id was null.");
            // throw new FormErrorException(this.messageSource.getMessage("error.404.form", null, LocaleContextHolder.getLocale()));
        }

        final URI snippetUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(snippetId)).build();
        return Response.created(snippetUri).build();
    }

    @GET
    @Path("/flagged")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getFlaggedSnippetFeed(final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {

        User loggedInUser = this.loginAuthentication.getLoggedInUser();
        if (loggedInUser == null || !roleService.isAdmin(loggedInUser.getId())) {
            LOGGER.warn("Only Admin can see flagged snippet feed");
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        final List<SnippetDto> snippets = this.snippetService.getAllFlaggedSnippets(page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, uriInfo)).collect(Collectors.toList());
        final int pageCount = PagingHelper.CalculateTotalPages(this.snippetService.getAllFlaggedSnippetsCount(), SNIPPET_PAGE_SIZE);

        Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
        });
        ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
        return builder.build();
    }

    @GET
    @Path("/flagged/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getFlaggedSnippetFeedSearch(final @BeanParam SearchDto searchForm, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {

        User loggedInUser = this.loginAuthentication.getLoggedInUser();
        if (loggedInUser == null || !roleService.isAdmin(loggedInUser.getId())) {
            LOGGER.warn("Only Admin can see flagged snippet feed");
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        final List<SnippetDto> snippets = SearchHelper.FindByCriteria(this.snippetService, searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.FLAGGED, searchForm.getSort(), null, null, page)
                .stream().map(s -> SnippetDto.fromSnippet(s, uriInfo)).collect(Collectors.toList());

        int totalSnippetCount = SearchHelper.GetSnippetByCriteriaCount(this.snippetService, searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.FLAGGED, null, null);
        final int pageCount = PagingHelper.CalculateTotalPages(totalSnippetCount, SNIPPET_PAGE_SIZE);

        //TODO Previously added the heart attributes

        Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
        });
        ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
        return builder.build();
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response snippetDetail(final @PathParam(PATH_PARAM_ID) long id) {
        Optional<Snippet> retrievedSnippet = this.snippetService.findSnippetById(id);

        if (retrievedSnippet.isPresent()) {
            Snippet snippet = retrievedSnippet.get();
            int voteCount = this.voteService.getVoteBalance(snippet.getId());
            return Response.ok(SnippetWithVoteDto.fromSnippetAndVote(snippet, voteCount, this.uriInfo)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST //TODO ---> o es un DELETE esto????
    @Path("/{id}/delete")
    public Response deleteSnippet(final @PathParam(PATH_PARAM_ID) long id){
        Optional<Snippet> retrievedSnippet = this.snippetService.findSnippetById(id);

        if (retrievedSnippet.isPresent()) {
            Snippet snippet = retrievedSnippet.get();
            User loggedInUser = this.loginAuthentication.getLoggedInUser();
            if (loggedInUser != null && loggedInUser.equals(snippet.getOwner())) {
                if (this.snippetService.deleteOrRestoreSnippet(snippet, true)) {
                    return Response.noContent().build();
                }
            } else {
                LOGGER.warn("User not logged in or owner of snippet {} attempting it's deletion", id);
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // TODO --> Merge with delete?
    @POST
    @Path("/{id}/restore")
    public Response restoreSnippet(final @PathParam(PATH_PARAM_ID) long id){
        Optional<Snippet> retrievedSnippet = this.snippetService.findSnippetById(id);

        if (retrievedSnippet.isPresent()) {
            Snippet snippet = retrievedSnippet.get();
            User loggedInUser = this.loginAuthentication.getLoggedInUser();
            if (loggedInUser != null && loggedInUser.equals(snippet.getOwner())) {
                if (this.snippetService.deleteOrRestoreSnippet(snippet, false)) {
                    return Response.noContent().build();
                }
            } else {
                LOGGER.warn("No user logged in or logged in user not admin but attempting to delete tag {}", id);
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/vote_positive")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response votePositiveSnippet(final @PathParam(PATH_PARAM_ID) long id, final VoteFormDto voteDto) {
        return performVote(id, voteDto, true);
    }

    @POST
    @Path("/{id}/vote_negative")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response voteNegativeSnippet(final @PathParam(PATH_PARAM_ID) long id, final VoteFormDto voteDto) {
        return performVote(id, voteDto, false);
    }

    private Response performVote(long id, final VoteFormDto voteDto, boolean isPositive) {
        Optional<Snippet> retrievedSnippet = this.snippetService.findSnippetById(id);

        if (retrievedSnippet.isPresent()) {
            Snippet snippet = retrievedSnippet.get();
            User loggedInUser = this.loginAuthentication.getLoggedInUser();
            if (loggedInUser != null) {
                this.voteService.performVote(snippet.getOwner().getId(), loggedInUser.getId(), id, voteDto.isVoteSelected(), isPositive);
                return Response.noContent().build();
            } else {
                LOGGER.error(messageSource.getMessage("error.403.snippet.vote", null, Locale.ENGLISH));
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/flag")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response flagSnippet(final @PathParam(PATH_PARAM_ID) long id) {
        return this.addOrRemoveSnippetFlag(id, true);
    }

    @POST
    @Path("/{id}/unflag")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response unflagSnippet(final @PathParam(PATH_PARAM_ID) long id) {
        return this.addOrRemoveSnippetFlag(id, false);
    }

    private Response addOrRemoveSnippetFlag(final long id, final boolean flag) {
        User loggedInUser = this.loginAuthentication.getLoggedInUser();
        if (loggedInUser == null || !roleService.isAdmin(loggedInUser.getId())) {
            LOGGER.error(messageSource.getMessage("error.403.snippet.flag", null, Locale.ENGLISH));
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Optional<Snippet> retrievedSnippet = this.snippetService.findSnippetById(id);
        if (retrievedSnippet.isPresent()) {
            Snippet snippet = retrievedSnippet.get();

            // Getting the url of the server
            final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            try {
                // Updating the flagged variable of snippet
                this.snippetService.updateFlagged(snippet, snippet.getOwner(), flag, baseUrl);
            } catch (Exception e) {
                LOGGER.error(e.getMessage() + "Failed to flag snippet {}", snippet.getId());
            }
            LOGGER.debug("Marked snippet {} as flagged by admin", id);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }


    /////////////////////////////////////////// OLD ////////////////////////////////////////////

    // Moved to UserController
    @Deprecated
    @RequestMapping("/favorites")
    public ModelAndView getFavoritesSnippetFeed(final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("index");

        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) this.logAndThrow(FAVORITES);

        Collection<Snippet> snippets = this.snippetService.getAllFavoriteSnippets(currentUser.getId(), page, SNIPPET_PAGE_SIZE);
        int totalSnippetCount = this.snippetService.getAllFavoriteSnippetsCount(currentUser.getId());

        this.addModelAttributesHelper(mav, currentUser, totalSnippetCount, page, snippets, FAVORITES);

        return mav;
    }

    // Moved to UserController
    @Deprecated
    @RequestMapping("/following")
    public ModelAndView getFollowingSnippetFeed(final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("snippet/snippetFollowing");

        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) this.logAndThrow(FOLLOWING);

        Collection<Snippet> snippets = this.snippetService.getAllFollowingSnippets(currentUser.getId(), page, SNIPPET_PAGE_SIZE);
        int totalSnippetCount = this.snippetService.getAllFollowingSnippetsCount(currentUser.getId());
        this.addModelAttributesHelper(mav, currentUser, totalSnippetCount, page, snippets, FOLLOWING);

        /* Show up to 25 tags -> most popular + non empty */
        MavHelper.addTagChipUnfollowFormAttributes(mav, this.tagService.getSomeOrderedFollowedTagsForUser(currentUser.getId(), Constants.FOLLOWING_FEED_TAG_AMOUNT), currentUser.getFollowedTags().size());
        return mav;
    }

    @Deprecated // Moved to UserController
    @RequestMapping("/upvoted")
    public ModelAndView getUpVotedSnippetFeed(final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("index");

        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) this.logAndThrow(UPVOTED);

        Collection<Snippet> snippets = this.snippetService.getAllUpVotedSnippets(currentUser.getId(), page, SNIPPET_PAGE_SIZE);
        int totalSnippetCount = this.snippetService.getAllUpvotedSnippetsCount(currentUser.getId());

        this.addModelAttributesHelper(mav, currentUser, totalSnippetCount, page, snippets, UPVOTED);

        return mav;
    }

    @Deprecated
    @RequestMapping("/flagged")
    public ModelAndView getFlaggedSnippetFeedDep(final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("index");

        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null || !roleService.isAdmin(currentUser.getId())) this.logAndThrow(FLAGGED);

        Collection<Snippet> snippets = this.snippetService.getAllFlaggedSnippets(page, SNIPPET_PAGE_SIZE);
        int totalSnippetCount = this.snippetService.getAllFlaggedSnippetsCount();

        this.addModelAttributesHelper(mav, currentUser, totalSnippetCount, page, snippets, FLAGGED);

        return mav;
    }

    @Deprecated
    private void addModelAttributesHelper(ModelAndView mav, User currentUser, int snippetCount, int page, Collection<Snippet> snippets, String searchContext) {
        mav.addObject("pages", snippetCount / SNIPPET_PAGE_SIZE + (snippetCount % SNIPPET_PAGE_SIZE == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("snippetList", snippets);
        mav.addObject("totalSnippetCount", snippetCount);
        mav.addObject("searchContext", searchContext);
        mav.addObject("searching", false);

        MavHelper.addSnippetCardFavFormAttributes(mav, currentUser, snippets);
    }

    @Deprecated
    @ModelAttribute
    public void addAttributes(Model model, @Valid final SearchForm searchForm) {
        User currentUser = this.loginAuthentication.getLoggedInUser();

        if (currentUser != null) {
            this.userService.updateLocale(currentUser.getId(), LocaleContextHolder.getLocale());
        }
        MavHelper.addCurrentUserAttributes(model, currentUser, tagService, roleService);
        model.addAttribute("searchForm", searchForm);
    }

    @Deprecated
    private void logAndThrow(String location) {
        LOGGER.warn("Inside {} with no logged in user", location);
        throw new ForbiddenAccessException(messageSource.getMessage("error.403", new Object[]{location}, LocaleContextHolder.getLocale()));
    }
}
