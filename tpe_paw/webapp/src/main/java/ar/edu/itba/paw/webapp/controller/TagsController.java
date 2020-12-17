package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.RoleService;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Tag;
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
@Path("tags")
public class TagsController {

    @Autowired
    private TagService tagService;
    @Autowired
    private SnippetService snippetService;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginAuthentication loginAuthentication;
    @Autowired
    private RoleService roleService;

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(TagsController.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response tagCreate(@Valid ItemCreateDto tagCreateDto) {
        User loggedInUser = this.loginAuthentication.getLoggedInUser();
        if (loggedInUser == null || !this.roleService.isAdmin(loggedInUser.getId())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        long tagId = this.tagService.addTag(tagCreateDto.getName());
        // Add URI to response
        final URI tagUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(tagId)).build();
        return Response.created(tagUri).build();
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getTagsByPage(final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page, final @QueryParam(QUERY_PARAM_SHOW_EMPTY) @DefaultValue("true") boolean showEmpty, final @QueryParam(QUERY_PARAM_SHOW_ONLY_FOLLOWING) @DefaultValue("false") boolean showOnlyFollowing) {
        User currentUser = loginAuthentication.getLoggedInUser();
        final long loggedUserId = UserHelper.GetLoggedUserId(this.loginAuthentication);
        final Collection<Tag> rawTags = tagService.getAllTags(showEmpty, showOnlyFollowing, currentUser != null ? currentUser.getId() : null, page, TAG_PAGE_SIZE);

        // Analyze if they are empty or not
        rawTags.forEach(t -> this.snippetService.analizeSnippetsUsing(t));

        final List<TagWithEmptyDto> tags = rawTags.stream().map(t -> TagWithEmptyDto.fromTag(t, loggedUserId, uriInfo)).collect(Collectors.toList());

        final int tagCount = this.tagService.getAllTagsCount(showEmpty, showOnlyFollowing, currentUser != null ? currentUser.getId() : null);
        int pageCount = PagingHelper.CalculateTotalPages(tagCount, TAG_PAGE_SIZE);

        Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<TagWithEmptyDto>>(tags) {
        });
        ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
        ResponseHelper.AddTotalItemsAttribute(builder, tagCount);
        return builder.build();
    }

    @GET
    @Path("/all")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getAllTags() {
        final List<TagDto> tags = tagService.getAllTags().stream().map(t -> TagDto.fromTag(t, uriInfo)).collect(Collectors.toList());

        Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<TagDto>>(tags) {
        });
        return builder.build();
    }

    @GET
    @Path("/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response searchInAllTags(final @BeanParam TagSearchDto tagSearchDto, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page){
        User currentUser = loginAuthentication.getLoggedInUser();
        final long loggedUserId = UserHelper.GetLoggedUserId(this.loginAuthentication);

        // Cannot be null user and show only following
        if (currentUser == null && tagSearchDto.isShowOnlyFollowing()){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        final Collection<Tag> rawTags = this.tagService.findTagsByName(tagSearchDto.getQuery(), tagSearchDto.isShowEmpty(), tagSearchDto.isShowOnlyFollowing(), currentUser != null ? currentUser.getId() : null, page, TAG_PAGE_SIZE);

        rawTags.forEach(t -> this.snippetService.analizeSnippetsUsing(t));

        final List<TagWithEmptyDto> tags = rawTags.stream().map(t -> TagWithEmptyDto.fromTag(t, loggedUserId, uriInfo)).collect(Collectors.toList());
        final int tagCount = this.tagService.getAllTagsCountByName(tagSearchDto.getQuery(), tagSearchDto.isShowEmpty(), tagSearchDto.isShowOnlyFollowing(), currentUser != null ? currentUser.getId() : null);
        int pageCount = PagingHelper.CalculateTotalPages(tagCount, TAG_PAGE_SIZE);

        Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<TagWithEmptyDto>>(tags) {
        });
        ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
        ResponseHelper.AddTotalItemsAttribute(builder, tagCount);
        return builder.build();
    }


    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getTagById(final @PathParam(PATH_PARAM_ID) long id){
        Optional<Tag> maybeTag = this.tagService.findTagById(id);
        if (maybeTag.isPresent()) {
            TagDto tagDto = TagDto.fromTag(maybeTag.get(), uriInfo);
            Response.ResponseBuilder builder = Response.ok(new GenericEntity<TagDto>(tagDto) {
            });
            return builder.build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/exists")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTagByName(final @QueryParam(QUERY_PARAM_NAME) String name) {
        User loggedInUser = this.loginAuthentication.getLoggedInUser();
        if (loggedInUser == null || !this.roleService.isAdmin(loggedInUser.getId())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (name == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(BooleanDto.fromBoolean(this.tagService.tagExists(name))).build();
    }

    @GET
    @Path("/{id}/snippets")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getSnippetsForTag(final @PathParam(PATH_PARAM_ID) long id, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page){
        Optional<Tag> maybeTag = this.tagService.findTagById(id);
        if (maybeTag.isPresent()) {
            final List<SnippetDto> snippets = this.snippetService.findSnippetsForTag(id, page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, UserHelper.GetLoggedUserId(this.loginAuthentication), uriInfo, LocaleContextHolder.getLocale())).collect(Collectors.toList());
            final int snippetCount = this.snippetService.getAllSnippetsByTagCount(id);
            int pageCount = PagingHelper.CalculateTotalPages(snippetCount, SNIPPET_PAGE_SIZE);

            Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
            });
            ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
            ResponseHelper.AddTotalItemsAttribute(builder, snippetCount);
            return builder.build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/snippets/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getSnippetsForLanguage(final @PathParam(PATH_PARAM_ID) long id,
                                           final @BeanParam SearchDto searchDto,
                                           final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page){
        Optional<Tag> maybeTag = this.tagService.findTagById(id);
        if (maybeTag.isPresent()) {
            final List<SnippetDto> snippets = SearchHelper.FindByCriteria(this.snippetService, searchDto.getType(), searchDto.getQuery(), SnippetDao.Locations.TAGS, searchDto.getSort(), null, id, page)
                    .stream().map(s -> SnippetDto.fromSnippet(s, UserHelper.GetLoggedUserId(this.loginAuthentication), uriInfo, LocaleContextHolder.getLocale())).collect(Collectors.toList());

            int totalSnippetCount = SearchHelper.GetSnippetByCriteriaCount(this.snippetService, searchDto.getType(), searchDto.getQuery(), SnippetDao.Locations.TAGS, null, id);
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

    @PUT
    @Path("/{id}/follow")
    public Response followTag(final @PathParam(PATH_PARAM_ID) long id){
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser != null) {
            if (this.tagService.tagExists(id)){
                if (!this.tagService.userFollowsTag(currentUser.getId(), id)){
                    this.tagService.followTag(currentUser.getId(), id);
                }
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            LOGGER.warn("Inside the follow form of tag {} without a logged in user", id);
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @DELETE
    @Path("/{id}/follow")
    public Response unfollowTag(final @PathParam(PATH_PARAM_ID) long id){
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser != null) {
            if (this.tagService.tagExists(id)){
                if (this.tagService.userFollowsTag(currentUser.getId(), id)){
                    this.tagService.unfollowTag(currentUser.getId(), id);
                }
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            LOGGER.warn("Inside the follow form of tag {} without a logged in user", id);
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @GET
    @Path("/{id}/users/{userId}/follows")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response userFollowsTag(final @PathParam(USER_PARAM_ID) long userId, final @PathParam(PATH_PARAM_ID) long id) {
        // Check permissions
        User loggedInUser = this.loginAuthentication.getLoggedInUser();
        if (loggedInUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } else if (loggedInUser.getId() != userId){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // Get tags and user
        Optional<User> maybeUser = this.userService.findUserById(userId);
        Optional<Tag> maybeTag = this.tagService.findTagById(id);

        if (maybeUser.isPresent() && maybeTag.isPresent()) {
            return Response.ok(BooleanDto.fromBoolean(this.tagService.userFollowsTag(userId, id))).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
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
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}