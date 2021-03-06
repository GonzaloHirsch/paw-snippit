package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.utility.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.utility.Constants.*;

@Component
@Path("/snippets")
public class SnippetController {

    @Autowired
    private SnippetService snippetService;
    @Autowired
    private UserService userService;
    @Autowired
    private VoteService voteService;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private LoginAuthentication loginAuthentication;
    @Autowired
    private TagService tagService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MessageSource messageSource;

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(SnippetController.class);
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
        long loggedUserId = UserHelper.GetLoggedUserId(this.loginAuthentication);
        final List<SnippetDto> snippets = this.snippetService.getAllSnippets(page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, loggedUserId, uriInfo, LocaleContextHolder.getLocale())).collect(Collectors.toList());
        final int numberOfSnippets = this.snippetService.getAllSnippetsCount();
        final int pageCount = PagingHelper.CalculateTotalPages(numberOfSnippets, SNIPPET_PAGE_SIZE);

        // If the page asked is greater than the one existing
        if (page > pageCount && pageCount > 0){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
        });
        ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
        ResponseHelper.AddTotalItemsAttribute(builder, numberOfSnippets);
        return builder.build();
    }

    @GET
    @Path("/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response searchInHome(final @BeanParam SearchDto searchDto, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {
        long loggedUserId = UserHelper.GetLoggedUserId(this.loginAuthentication);
        final List<SnippetDto> snippets = SearchHelper.FindByCriteria(this.snippetService, searchDto.getType(), searchDto.getQuery(), SnippetDao.Locations.HOME, searchDto.getSort(), null, null, page)
                .stream().map(s -> SnippetDto.fromSnippet(s, loggedUserId, uriInfo, LocaleContextHolder.getLocale())).collect(Collectors.toList());

        int totalSnippetCount = SearchHelper.GetSnippetByCriteriaCount(this.snippetService, searchDto.getType(), searchDto.getSort(), searchDto.getQuery(), SnippetDao.Locations.HOME, null, null);
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
    }

    @GET
    @Path("/explore/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response exploreSearch(final @Valid @BeanParam ExploreDto exploreDto, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {

        Instant minDate = null;
        Instant maxDate = null;
        if (exploreDto.getMinDate() != null) {
            minDate = exploreDto.getMinDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        }
        if (exploreDto.getMaxDate() != null) {
            maxDate = exploreDto.getMaxDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        }
        Collection<Snippet> snippetsCollection = this.snippetService.findSnippetByDeepCriteria(
                minDate, maxDate,
                exploreDto.getMinRep(), exploreDto.getMaxRep(),
                exploreDto.getMinVotes(), exploreDto.getMaxVotes(),
                exploreDto.getLanguage() == -1 ? null : exploreDto.getLanguage(), exploreDto.getTag() == -1 ? null : exploreDto.getTag(),
                exploreDto.getTitle(), exploreDto.getUsername(),
                ordersMap.get(exploreDto.getSort()), typesMap.get(exploreDto.getField()), exploreDto.getIncludeFlagged(), page, SNIPPET_PAGE_SIZE);
        int snippetCount = this.snippetService.getSnippetByDeepCriteriaCount(
                minDate, maxDate,
                exploreDto.getMinRep(), exploreDto.getMaxRep(),
                exploreDto.getMinVotes(), exploreDto.getMaxVotes(),
                exploreDto.getLanguage() == -1 ? null : exploreDto.getLanguage(), exploreDto.getTag() == -1 ? null : exploreDto.getTag(),
                exploreDto.getTitle(), exploreDto.getUsername(),
                exploreDto.getIncludeFlagged());

        long loggedUserId = UserHelper.GetLoggedUserId(this.loginAuthentication);
        final List<SnippetDto> snippets = snippetsCollection.stream().map(s -> SnippetDto.fromSnippet(s, loggedUserId, uriInfo, LocaleContextHolder.getLocale())).collect(Collectors.toList());
        final int pageCount = PagingHelper.CalculateTotalPages(snippetCount, SNIPPET_PAGE_SIZE);

        // If the page asked is greater than the one existing
        if (page > pageCount && pageCount > 0){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
        });
        ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
        ResponseHelper.AddTotalItemsAttribute(builder, snippetCount);
        return builder.build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response snippetCreate(@Valid SnippetCreateDto createDto) {
        User loggedInUser = this.loginAuthentication.getLoggedInUser();
        if (loggedInUser == null) {
            LOGGER.error(messageSource.getMessage("error.403.snippet.create", null, Locale.ENGLISH));
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (this.roleService.isAdmin(loggedInUser.getId())) {
            LOGGER.error(messageSource.getMessage("error.403.admin.snippet.create", null, Locale.ENGLISH));
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Instant dateCreated = Instant.now();
        Long snippetId = this.snippetService.createSnippet(loggedInUser, createDto.getTitle(), createDto.getDescription(), createDto.getCode(), dateCreated, createDto.getLanguage(), createDto.getTags());

        // If error creating snippet return bad request because probably client error in request
        if (snippetId == null) {
            LOGGER.error("Snippet creation was unsuccessful. Return id was null.");
            return Response.status(Response.Status.BAD_REQUEST).build();
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
        if (loggedInUser == null || !this.roleService.isAdmin(loggedInUser.getId())) {
            LOGGER.warn("Only Admin can see flagged snippet feed");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        long loggedUserId = UserHelper.GetLoggedUserId(this.loginAuthentication);
        final List<SnippetDto> snippets = this.snippetService.getAllFlaggedSnippets(page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, loggedUserId, uriInfo, LocaleContextHolder.getLocale())).collect(Collectors.toList());
        final int snippetCount = this.snippetService.getAllFlaggedSnippetsCount();
        final int pageCount = PagingHelper.CalculateTotalPages(snippetCount, SNIPPET_PAGE_SIZE);

        // If the page asked is greater than the one existing
        if (page > pageCount && pageCount > 0){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
        });
        ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
        ResponseHelper.AddTotalItemsAttribute(builder, snippetCount);
        return builder.build();
    }

    @GET
    @Path("/flagged/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getFlaggedSnippetFeedSearch(final @BeanParam SearchDto searchDto, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {

        User loggedInUser = this.loginAuthentication.getLoggedInUser();
        if (loggedInUser == null || !roleService.isAdmin(loggedInUser.getId())) {
            LOGGER.warn("Only Admin can see flagged snippet feed");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        long loggedUserId = UserHelper.GetLoggedUserId(this.loginAuthentication);
        final List<SnippetDto> snippets = SearchHelper.FindByCriteria(this.snippetService, searchDto.getType(), searchDto.getQuery(), SnippetDao.Locations.FLAGGED, searchDto.getSort(), null, null, page)
                .stream().map(s -> SnippetDto.fromSnippet(s, loggedUserId, uriInfo, LocaleContextHolder.getLocale())).collect(Collectors.toList());

        int totalSnippetCount = SearchHelper.GetSnippetByCriteriaCount(this.snippetService, searchDto.getType(), searchDto.getSort(), searchDto.getQuery(), SnippetDao.Locations.FLAGGED, null, null);
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
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response snippetDetail(final @PathParam(PATH_PARAM_ID) long id) {
        Optional<Snippet> retrievedSnippet = this.snippetService.findSnippetById(id);

        if (retrievedSnippet.isPresent()) {
            Snippet snippet = retrievedSnippet.get();
            int voteCount = this.voteService.getVoteBalance(snippet.getId());
            return Response.ok(SnippetWithVoteDto.fromSnippetAndVote(snippet, voteCount, this.uriInfo, UserHelper.GetLoggedUserId(this.loginAuthentication), LocaleContextHolder.getLocale())).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}/tags")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSnippetTags(final @PathParam(PATH_PARAM_ID) long id) {
        // Don't go directly to DB because we need to know if the snippet exists
        Optional<Snippet> retrievedSnippet = this.snippetService.findSnippetById(id);

        if (retrievedSnippet.isPresent()) {
            Snippet snippet = retrievedSnippet.get();
            final List<TagDto> tags = snippet.getTags().stream().map(t -> TagDto.fromTag(t, uriInfo)).collect(Collectors.toList());
            final int tagCount = tags.size();

            Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<TagDto>>(tags) {
            });
            ResponseHelper.AddTotalItemsAttribute(builder, tagCount);
            return builder.build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}/vote_count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSnippetVoteCount(final @PathParam(PATH_PARAM_ID) long id) {
        Optional<Snippet> retrievedSnippet = this.snippetService.findSnippetById(id);

        if (retrievedSnippet.isPresent()) {
            int voteCount = this.voteService.getVoteBalance(id);
            return Response.ok(ValueDto.fromValue(voteCount)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSnippet(final @PathParam(PATH_PARAM_ID) long id) {
        Optional<Snippet> retrievedSnippet = this.snippetService.findSnippetById(id);

        if (retrievedSnippet.isPresent()) {
            Snippet snippet = retrievedSnippet.get();
            User loggedInUser = this.loginAuthentication.getLoggedInUser();
            if (loggedInUser != null && loggedInUser.equals(snippet.getOwner())) {
                // If the method returns false, it is because the snippet does not exists, will return NOT_FOUND
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

    @PUT
    @Path("/{id}/restore")
    public Response restoreSnippet(final @PathParam(PATH_PARAM_ID) long id) {
        Optional<Snippet> retrievedSnippet = this.snippetService.findSnippetById(id);

        if (retrievedSnippet.isPresent()) {
            Snippet snippet = retrievedSnippet.get();
            User loggedInUser = this.loginAuthentication.getLoggedInUser();
            if (loggedInUser != null && loggedInUser.equals(snippet.getOwner())) {
                // If the method returns false, it is because the snippet does not exists, will return NOT_FOUND
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

    @PUT
    @Path("/{id}/vote_positive")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPositiveVoteSnippet(final @PathParam(PATH_PARAM_ID) long id) {
        return performVote(id, true, true);
    }

    @DELETE
    @Path("/{id}/vote_positive")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removePositiveVoteSnippet(final @PathParam(PATH_PARAM_ID) long id) {
        return performVote(id, false, true);
    }

    @PUT
    @Path("/{id}/vote_negative")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNegativeVoteSnippet(final @PathParam(PATH_PARAM_ID) long id) {
        return performVote(id, true, false);
    }

    @DELETE
    @Path("/{id}/vote_negative")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeNegativeVoteSnippet(final @PathParam(PATH_PARAM_ID) long id) {
        return performVote(id, false, false);
    }

    private Response performVote(long id, final boolean shouldAdd, boolean isPositive) {
        Optional<Snippet> retrievedSnippet = this.snippetService.findSnippetById(id);

        if (retrievedSnippet.isPresent()) {
            Snippet snippet = retrievedSnippet.get();
            User loggedInUser = this.loginAuthentication.getLoggedInUser();
            if (loggedInUser != null) {
                this.voteService.performVote(snippet.getOwner().getId(), loggedInUser.getId(), id, shouldAdd, isPositive);
                return Response.noContent().build();
            } else {
                LOGGER.error(messageSource.getMessage("error.403.snippet.vote", null, Locale.ENGLISH));
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}/flag")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response flagSnippet(final @PathParam(PATH_PARAM_ID) long id) {
        return this.addOrRemoveSnippetFlag(id, true);
    }

    @DELETE
    @Path("/{id}/flag")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response unflagSnippet(final @PathParam(PATH_PARAM_ID) long id) {
        return this.addOrRemoveSnippetFlag(id, false);
    }

    private Response addOrRemoveSnippetFlag(final long id, final boolean flag) {
        User loggedInUser = this.loginAuthentication.getLoggedInUser();
        if (loggedInUser == null || !roleService.isAdmin(loggedInUser.getId())) {
            LOGGER.error(messageSource.getMessage("error.403.snippet.flag", null, Locale.ENGLISH));
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Snippet> retrievedSnippet = this.snippetService.findSnippetById(id);
        if (retrievedSnippet.isPresent()) {
            Snippet snippet = retrievedSnippet.get();

            // Getting the url of the server
            final String baseUrl = this.uriInfo.getBaseUri().toString().replace(Constants.API_PREFIX, "/#");
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


    @PUT
    @Path("/{id}/fav")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response favSnippet(final @PathParam(PATH_PARAM_ID) long id) {
        return this.addOrRemoveSnippetFav(id, true);
    }

    @DELETE
    @Path("/{id}/fav")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response unfavSnippet(final @PathParam(PATH_PARAM_ID) long id) {
        return this.addOrRemoveSnippetFav(id, false);
    }

    private Response addOrRemoveSnippetFav(final long id, final boolean isFav) {
        Optional<Snippet> maybeSnippet = this.snippetService.findSnippetById(id);
        if (!maybeSnippet.isPresent()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) {
            LOGGER.error(messageSource.getMessage("error.403.snippet.fav", null, Locale.ENGLISH));
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        this.favoriteService.updateFavorites(currentUser.getId(), id, isFav);
        LOGGER.debug("User {} updated favorite on snippet {}", currentUser.getUsername(), id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}/report")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reportSnippet(final @PathParam(PATH_PARAM_ID) long id, final @Valid ReportDto reportDto) {
        final String baseUrl = this.uriInfo.getBaseUri().toString().replace(Constants.API_PREFIX, "/#");
        Optional<Snippet> retrievedSnippet = this.snippetService.findSnippetById(id);
        if (!retrievedSnippet.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (retrievedSnippet.get().isDeleted()){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Snippet snippet = retrievedSnippet.get();
        User loggedInUser = this.loginAuthentication.getLoggedInUser();

        if (loggedInUser == null || loggedInUser.equals(snippet.getOwner())) {
            LOGGER.error(messageSource.getMessage("error.403.snippet.report.owner", null, Locale.ENGLISH));
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (!this.reportService.canReport(loggedInUser)) {
            LOGGER.error(messageSource.getMessage("error.403.snippet.report.reputation", null, Locale.ENGLISH));
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        try {
            this.reportService.reportSnippet(loggedInUser, snippet, reportDto.getReportDetail(), baseUrl);
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + "Failed report snippet: user {} about their snippet {}", snippet.getOwner().getUsername(), snippet.getId());
        }
        LOGGER.debug("User {} reported snippet {} with message {}", loggedInUser.getUsername(), id, reportDto.getReportDetail());
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}/report/dismiss")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response dismissReportWarning(final @PathParam(PATH_PARAM_ID) long id) {
        User loggedInUser = loginAuthentication.getLoggedInUser();
        Optional<Snippet> retrievedSnippet = this.snippetService.findSnippetById(id);
        if (!retrievedSnippet.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Snippet snippet = retrievedSnippet.get();

        if (loggedInUser == null || !loggedInUser.equals(snippet.getOwner())) {
            LOGGER.error(messageSource.getMessage("error.403.snippet.report.dismiss", null, Locale.ENGLISH));
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        this.reportService.dismissReportsForSnippet(id);
        return Response.noContent().build();
    }
}
