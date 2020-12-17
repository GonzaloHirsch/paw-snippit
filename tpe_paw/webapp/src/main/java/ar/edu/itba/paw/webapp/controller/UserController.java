package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.utility.*;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.utility.Constants.*;

@Component
@Path("users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private SnippetService snippetService;
    @Autowired
    private TagService tagService;
    @Autowired
    private LoginAuthentication loginAuthentication;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CryptoService cryptoService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(final @Valid RegisterDto registerDto) {
        User registeredUser = this.userService.register(registerDto.getUsername(), this.passwordEncoder.encode(registerDto.getPassword()), registerDto.getEmail(), Instant.now(), LocaleContextHolder.getLocale());
        try {
            this.userService.registerFollowUp(registeredUser);
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + "Failed to send registration email to user {}", registerDto.getUsername());
        }

        final URI userUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(registeredUser.getId())).build();
        return Response.created(userUri).build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getById(final @PathParam(PATH_PARAM_ID) long id) {
        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            return Response.ok(UserDto.fromUser(maybeUser.get(), this.uriInfo)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/active_snippets")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getActiveSnippetsForUser(final @PathParam(PATH_PARAM_ID) long id, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {
        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            final User user = maybeUser.get();

            final List<SnippetDto> snippets = this.snippetService.getAllSnippetsByOwner(user.getId(), page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, UserHelper.GetLoggedUserId(this.loginAuthentication), uriInfo, LocaleContextHolder.getLocale())).collect(Collectors.toList());
            final int snippetCount = this.snippetService.getAllSnippetsByOwnerCount(user.getId());
            final int pageCount = PagingHelper.CalculateTotalPages(snippetCount, SNIPPET_PAGE_SIZE);

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
    @Path("/{id}/active_snippets/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getActiveSnippetsForUserSearch(final @PathParam(PATH_PARAM_ID) long id, final @BeanParam SearchDto searchDto, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {
        return this.userContextSearch(id, SnippetDao.Locations.USER, searchDto, page);
    }

    @GET
    @Path("/{id}/deleted_snippets")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getDeletedSnippetsForUser(final @PathParam(PATH_PARAM_ID) long id, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {
        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            final User user = maybeUser.get();
            final User loggedUser = this.loginAuthentication.getLoggedInUser();
            if (loggedUser != null && loggedUser.getId().equals(user.getId())) {
                final List<SnippetDto> snippets = this.snippetService.getAllDeletedSnippetsByOwner(user.getId(), page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, UserHelper.GetLoggedUserId(this.loginAuthentication), uriInfo, LocaleContextHolder.getLocale())).collect(Collectors.toList());
                final int snippetCount = this.snippetService.getAllDeletedSnippetsByOwnerCount(user.getId());
                final int pageCount = PagingHelper.CalculateTotalPages(snippetCount, SNIPPET_PAGE_SIZE);

                Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
                });
                ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
                ResponseHelper.AddTotalItemsAttribute(builder, snippetCount);
                return builder.build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/deleted_snippets/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getDeletedSnippetsForUserSearch(final @PathParam(PATH_PARAM_ID) long id, final @BeanParam SearchDto searchDto, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {
        return this.userContextSearch(id, SnippetDao.Locations.DELETED, searchDto, page);
    }

    @GET
    @Path("/{id}/profile_photo")
    @Produces("image/png")
    public Response getUserProfilePhoto(final @PathParam(PATH_PARAM_ID) long id) {
        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            final User user = maybeUser.get();
            CacheControl cc = new CacheControl();
            cc.setMustRevalidate(true);
            cc.setNoTransform(true);
            cc.setSMaxAge(60 * 60 * 24);
            return Response.ok(user.getIcon()).cacheControl(cc).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}/profile_photo")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response changeProfilePhoto(final @PathParam(PATH_PARAM_ID) long id, @FormDataParam("file") InputStream inputStream,
                                       @FormDataParam("file") FormDataContentDisposition contentDisposition) {
        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            final User user = maybeUser.get();
            final User loggedUser = this.loginAuthentication.getLoggedInUser();
            if (loggedUser != null && loggedUser.getId().equals(user.getId())) {
                try {
                    byte[] data = IOUtils.toByteArray(inputStream);
                    // If data is over the limit, return BAD_REQUEST
                    if (data.length > Constants.UPLOAD_MAX_SIZE){
                        return Response.status(Response.Status.BAD_REQUEST).build();
                    }
                    this.userService.changeProfilePhoto(id, data);
                    return Response.noContent().build();
                } catch (IOException e) {
                    LOGGER.error("Exception when changing profile photo for user id {}", id);
                    return Response.serverError().build();
                }
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}/description")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeDescription(final @PathParam(PATH_PARAM_ID) long id, @Valid UserDescriptionDto userDescriptionDto) {
        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            final User user = maybeUser.get();
            final User loggedUser = this.loginAuthentication.getLoggedInUser();
            if (loggedUser != null && loggedUser.getId().equals(user.getId())) {
                this.userService.changeDescription(id, userDescriptionDto.getDescription());
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/send_verify_email")
    public Response verifyUserEmailSendEmail(final @PathParam(PATH_PARAM_ID) long id) {
        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            final User user = maybeUser.get();
            final User loggedUser = this.loginAuthentication.getLoggedInUser();
            if (loggedUser != null && loggedUser.getId().equals(user.getId())) {
                try {
                    this.emailService.sendVerificationEmail(user);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage() + "Failed to send verification email to user {}", user.getUsername());
                }
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/verify_email")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response verifyUserEmailCode(final @PathParam(PATH_PARAM_ID) long id, final @Valid EmailVerificationDto verificationFormDto) {
        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            final User user = maybeUser.get();
            final User loggedUser = this.loginAuthentication.getLoggedInUser();
            if (loggedUser != null && loggedUser.getId().equals(user.getId())) {
                if (!this.cryptoService.checkValidTOTP(user, verificationFormDto.getCode())) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                this.userService.verifyUserEmail(id);
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/recover_password")
    public Response recoverPasswordSendEmail(final @Valid RecoveryDto recoveryDto) {
        // No logged user can use this
        final User loggedUser = this.loginAuthentication.getLoggedInUser();
        if (loggedUser != null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        User user = this.userService.findUserByEmail(recoveryDto.getEmail()).orElse(null);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Getting the URL for the server
        // We remove the API prefix and add the required #/ for the hashrouter of the client
        final String baseUrl = this.uriInfo.getBaseUri().toString().replace(Constants.API_PREFIX, "/#");
        try {
            this.emailService.sendRecoveryEmail(user, baseUrl);
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + "Failed to send recovery email to user {}", recoveryDto.getEmail());
        }
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/valid_token")
    public Response isRecoverPasswordTokenValid(final @PathParam(PATH_PARAM_ID) long id, final @Valid TokenDto tokenDto) {
        // No logged user can use this
        final User loggedUser = this.loginAuthentication.getLoggedInUser();
        if (loggedUser != null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        /// Get the user
        Optional<User> maybeUser = userService.findUserById(id);

        // Test the token
        Optional<Response> maybeResponse = this.isResetPasswordTokenValid(maybeUser, tokenDto.getToken());

        // If present, return the error response, if not returns a 204 (NoContent), indicating the token is valid
        return maybeResponse.orElseGet(() -> Response.noContent().build());
    }

    @POST
    @Path("/{id}/change_password")
    public Response changePasswordWithToken(final @PathParam(PATH_PARAM_ID) long id, final @Valid ResetPasswordDto resetPasswordDto) {
        // No logged user can use this
        final User loggedUser = this.loginAuthentication.getLoggedInUser();
        if (loggedUser != null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Get the user
        Optional<User> maybeUser = userService.findUserById(id);

        // Test the token
        Optional<Response> maybeResponse = this.isResetPasswordTokenValid(maybeUser, resetPasswordDto.getToken());

        // If present, return the error response
        if (maybeResponse.isPresent()) {
            return maybeResponse.get();
        }

        //noinspection OptionalGetWithoutIsPresent
        userService.changePassword(maybeUser.get().getEmail(), passwordEncoder.encode(resetPasswordDto.getNewPassword()));

        // Returns a 204 (NoContent), indicating the operation success
        return Response.noContent().build();
    }

  /*  @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    // TODO: HAY 2 metodos iguales
    public Response changeUserDescription(final @PathParam(PATH_PARAM_ID) long id, final UserDto userDto) {
        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            final User user = maybeUser.get();
            final User loggedUser = this.loginAuthentication.getLoggedInUser();
            if (loggedUser != null && loggedUser.getId().equals(user.getId())) {
                this.userService.changeDescription(id, userDto.getDescription());
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }*/

    @GET
    @Path("/{id}/favorite_snippets")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getFavoriteSnippetsForUser(final @PathParam(PATH_PARAM_ID) long id, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {
        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            final User user = maybeUser.get();
            final User loggedUser = this.loginAuthentication.getLoggedInUser();
            if (loggedUser != null && loggedUser.getId().equals(user.getId())) {
                final List<SnippetDto> snippets = this.snippetService.getAllFavoriteSnippets(user.getId(), page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, UserHelper.GetLoggedUserId(this.loginAuthentication), uriInfo, LocaleContextHolder.getLocale())).collect(Collectors.toList());
                final int snippetCount = this.snippetService.getAllFavoriteSnippetsCount(user.getId());
                final int pageCount = PagingHelper.CalculateTotalPages(snippetCount, SNIPPET_PAGE_SIZE);

                Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
                });
                ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
                ResponseHelper.AddTotalItemsAttribute(builder, snippetCount);
                return builder.build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/favorite_snippets/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getFavoriteSnippetsForUserSearch(final @PathParam(PATH_PARAM_ID) long id, final @BeanParam SearchDto searchDto, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {
        return this.userContextSearch(id, SnippetDao.Locations.FAVORITES, searchDto, page);
    }

    @GET
    @Path("/{id}/following_snippets")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getFollowingSnippetsForUser(final @PathParam(PATH_PARAM_ID) long id, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {
        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            final User user = maybeUser.get();
            final User loggedUser = this.loginAuthentication.getLoggedInUser();
            if (loggedUser != null && loggedUser.getId().equals(user.getId())) {
                final List<SnippetDto> snippets = this.snippetService.getAllFollowingSnippets(user.getId(), page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, UserHelper.GetLoggedUserId(this.loginAuthentication), uriInfo, LocaleContextHolder.getLocale())).collect(Collectors.toList());
                final int snippetCount = this.snippetService.getAllFollowingSnippetsCount(user.getId());
                final int pageCount = PagingHelper.CalculateTotalPages(snippetCount, SNIPPET_PAGE_SIZE);

                Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
                });
                ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
                ResponseHelper.AddTotalItemsAttribute(builder, snippetCount);
                return builder.build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/following_snippets/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getFollowingSnippetsForUserSearch(final @PathParam(PATH_PARAM_ID) long id, final @BeanParam SearchDto searchDto, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {
        return this.userContextSearch(id, SnippetDao.Locations.FOLLOWING, searchDto, page);
    }

    @GET
    @Path("/{id}/following_tags")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getFollowingTagsForUser(final @PathParam(PATH_PARAM_ID) long id) {
        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            final User loggedUser = this.loginAuthentication.getLoggedInUser();
            if (loggedUser != null && loggedUser.getId().equals(id)) {
                final User user = maybeUser.get();
                final List<TagDto> followedTags = this.tagService.getSomeOrderedFollowedTagsForUser(user.getId(), Integer.MAX_VALUE).stream().map(s -> TagDto.fromTag(s, uriInfo)).collect(Collectors.toList());

                Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<TagDto>>(followedTags) {
                });
                return builder.build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/upvoted_snippets")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getUpvotedSnippetsForUser(final @PathParam(PATH_PARAM_ID) long id, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {
        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            final User user = maybeUser.get();
            final User loggedUser = this.loginAuthentication.getLoggedInUser();
            if (loggedUser != null && loggedUser.getId().equals(user.getId())) {
                final List<SnippetDto> snippets = this.snippetService.getAllUpVotedSnippets(user.getId(), page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, UserHelper.GetLoggedUserId(this.loginAuthentication), uriInfo, LocaleContextHolder.getLocale())).collect(Collectors.toList());
                final int snippetCount = this.snippetService.getAllUpvotedSnippetsCount(user.getId());
                final int pageCount = PagingHelper.CalculateTotalPages(snippetCount, SNIPPET_PAGE_SIZE);

                Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
                });
                ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
                ResponseHelper.AddTotalItemsAttribute(builder, snippetCount);
                return builder.build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/upvoted_snippets/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getUpvotedSnippetsForUserSearch(final @PathParam(PATH_PARAM_ID) long id, final @BeanParam SearchDto searchDto, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {
        return this.userContextSearch(id, SnippetDao.Locations.UPVOTED, searchDto, page);
    }

    private Response userContextSearch(final long id, SnippetDao.Locations location, SearchDto searchDto, int page) {
        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            final User user = maybeUser.get();

            // If it is a protected feed, check if it is the owner
            if (location != SnippetDao.Locations.USER) {
                final User loggedUser = this.loginAuthentication.getLoggedInUser();
                if (loggedUser != null && loggedUser.getId().equals(user.getId())) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }

            final List<SnippetDto> snippets = SearchHelper.FindByCriteria(this.snippetService, searchDto.getType(), searchDto.getQuery(), location, searchDto.getSort(), user.getId(), null, page)
                    .stream().map(s -> SnippetDto.fromSnippet(s, UserHelper.GetLoggedUserId(this.loginAuthentication), uriInfo, LocaleContextHolder.getLocale())).collect(Collectors.toList());

            int totalSnippetCount = SearchHelper.GetSnippetByCriteriaCount(this.snippetService, searchDto.getType(), searchDto.getQuery(), location, user.getId(), null);
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

    /**
     * Determines if given a user Id and a token, the token for the user is valid
     *
     * @param maybeUser Optional with maybe a user
     * @param token     Token given in the request
     * @return An Optional with a possible response, empty if valid token
     */
    private Optional<Response> isResetPasswordTokenValid(Optional<User> maybeUser, String token) {
        // Find the user by the given id
        if (!maybeUser.isPresent()) {
            return Optional.of(Response.status(Response.Status.NOT_FOUND).build()); // UserNotFound
        }

        // Check if the token is valid
        User user = maybeUser.get();
        boolean pass = this.cryptoService.checkValidRecoverToken(user, token);

        /* If link is no longer valid */
        if (!pass) {
            return Optional.of(Response.status(Response.Status.FORBIDDEN).build()); // Forbidden
        }
        return Optional.empty();
    }
}
