package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.dto.SnippetDto;
import ar.edu.itba.paw.webapp.dto.TagDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.dto.EmailVerificationDto;
import ar.edu.itba.paw.webapp.dto.RecoveryDto;
import ar.edu.itba.paw.webapp.dto.RegisterDto;
import ar.edu.itba.paw.webapp.dto.SearchDto;
import ar.edu.itba.paw.webapp.form.DescriptionForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Instant;
import java.util.Collection;
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

        /* TODO How to do this?
        this.signUpAuthentication.authWithAuthManager(request, registerForm.getUsername(), registerForm.getPassword());
        String redirectUrl = this.signUpAuthentication.redirectionAuthenticationSuccess(request);
        */
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
        //TODO only owner can be here

        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            final User user = maybeUser.get();

            final List<SnippetDto> snippets = this.snippetService.getAllSnippetsByOwner(user.getId(), page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, uriInfo)).collect(Collectors.toList());
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
        //TODO only owner can be here

        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            final User user = maybeUser.get();
            final User loggedUser = this.loginAuthentication.getLoggedInUser();
            if (loggedUser != null && loggedUser.getId().equals(user.getId())) {
                final List<SnippetDto> snippets = this.snippetService.getAllDeletedSnippetsByOwner(user.getId(), page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, uriInfo)).collect(Collectors.toList());
                final int snippetCount = this.snippetService.getAllDeletedSnippetsByOwnerCount(user.getId());
                final int pageCount = PagingHelper.CalculateTotalPages(snippetCount, SNIPPET_PAGE_SIZE);

                Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
                });
                ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
                ResponseHelper.AddTotalItemsAttribute(builder, snippetCount);
                return builder.build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
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
                    this.userService.changeProfilePhoto(id, data);
                    return Response.noContent().build();
                } catch (IOException e) {
                    LOGGER.error("Exception when changing profile photo for user id {}", id);
                    return Response.serverError().build();
                }
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/{id}/send_verify_email")
    public Response verifyUserEmailSendEmail(final @PathParam(PATH_PARAM_ID) long id) {
        //TODO Must be logged in
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
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/verify_email")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response verifyUserEmailCode(final @PathParam(PATH_PARAM_ID) long id, final @BeanParam EmailVerificationDto verificationFormDto){
        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            final User user = maybeUser.get();
            final User loggedUser = this.loginAuthentication.getLoggedInUser();
            if (loggedUser != null && loggedUser.getId().equals(user.getId())) {
                if (!this.cryptoService.checkValidTOTP(user, verificationFormDto.getCode())) {
                    //ERROR - WRONG CODE
                }
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/recover_password")
    public Response recoverPasswordSendEmail(final @Valid RecoveryDto recoveryDto) {
        User user = this.userService.findUserByEmail(recoveryDto.getEmail()).orElse(null);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // UserNotFound
        }
        // Getting the URL for the server
        // TODO --> FIX depending on how we change Reset password
        final String baseUrl = this.uriInfo.getBaseUri().toString();
        try {
            this.emailService.sendRecoveryEmail(user, baseUrl);
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + "Failed to send recovery email to user {}", recoveryDto.getEmail());
        }
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeUserDescription(final @PathParam(PATH_PARAM_ID) long id, final UserDto userDto){
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
    }

    @GET
    @Path("/{id}/favorite_snippets")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getFavoriteSnippetsForUser(final @PathParam(PATH_PARAM_ID) long id, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {
        //TODO only owner can be here

        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            final User user = maybeUser.get();

            final List<SnippetDto> snippets = this.snippetService.getAllFavoriteSnippets(user.getId(), page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, uriInfo)).collect(Collectors.toList());
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

            final List<SnippetDto> snippets = this.snippetService.getAllFollowingSnippets(user.getId(), page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, uriInfo)).collect(Collectors.toList());
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
    public Response getFollowingTagsForUser(final @PathParam(PATH_PARAM_ID) long id, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {
        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            final User user = maybeUser.get();
            final List<TagDto> followedTags = this.tagService.getSomeOrderedFollowedTagsForUser(user.getId(), Integer.MAX_VALUE).stream().map(s -> TagDto.fromTag(s, uriInfo)).collect(Collectors.toList());

            Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<TagDto>>(followedTags) {
            });
            return builder.build();
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

            final List<SnippetDto> snippets = this.snippetService.getAllUpVotedSnippets(user.getId(), page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, uriInfo)).collect(Collectors.toList());
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
    }

    @GET
    @Path("/{id}/upvoted_snippets/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getUpvotedSnippetsForUserSearch(final @PathParam(PATH_PARAM_ID) long id, final @BeanParam SearchDto searchDto, final @QueryParam(QUERY_PARAM_PAGE) @DefaultValue("1") int page) {
        return this.userContextSearch(id, SnippetDao.Locations.UPVOTED, searchDto, page);
    }

    private Response userContextSearch(final long id, SnippetDao.Locations location, SearchDto searchDto, int page) {
        //TODO only owner can be here
        Optional<User> maybeUser = this.userService.findUserById(id);
        if (maybeUser.isPresent()) {
            final User user = maybeUser.get();

            final List<SnippetDto> snippets = SearchHelper.FindByCriteria(this.snippetService, searchDto.getType(), searchDto.getQuery(), location, searchDto.getSort(), user.getId(), null, page)
                    .stream().map(s -> SnippetDto.fromSnippet(s, uriInfo)).collect(Collectors.toList());

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

    ///////////////////////////////////////////////// OLD ///////////////////////////////////////////////////////

/* TODO --> NEEDS CONVERTING
    @RequestMapping(value = "/user/{id}/{context}/edit", method = {RequestMethod.POST})
    public ModelAndView endEditUserProfile(
            final @PathVariable("id") long id,
            final @PathVariable("context") String context,
            @Valid @ModelAttribute("descriptionForm") final DescriptionForm descriptionForm,
            final BindingResult errors,
            @ModelAttribute("profilePhotoForm") final ProfilePhotoForm profilePhotoForm,
            final @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            final @RequestParam(value = "editing", required = false, defaultValue = "false") boolean editing
    ) {
        if (!(context.equals(Constants.OWNER_DELETED_CONTEXT) || context.equals(Constants.OWNER_ACTIVE_CONTEXT) || context.equals(Constants.USER_PROFILE_CONTEXT))) {
            LOGGER.warn("Invalid URL in profile. Context = {}", context);
            throw new InvalidUrlException();
        }
        if (errors.hasErrors()) {
            return context.equals(Constants.OWNER_DELETED_CONTEXT) ?
                    deletedSnippetUserProfile(id, profilePhotoForm, descriptionForm, page, true) :
                    activeSnippetUserProfile(id, profilePhotoForm, descriptionForm, page, true);
        }
        User currentUser = this.loginAuthentication.getLoggedInUser();
        User user = this.getUserWithId(id);
        if (currentUser != null && currentUser.equals(user)) {
            this.userService.changeDescription(id, descriptionForm.getDescription());
        } else {
            LOGGER.error(messageSource.getMessage("error.403.profile.owner", null, Locale.ENGLISH));
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.profile.owner", null, LocaleContextHolder.getLocale()));
        }
        return new ModelAndView("redirect:/user/" + id + "/" + context);
    }*/

    @Deprecated
    private void logAndThrow(long id) {
        LOGGER.warn("User with id {} doesn't exist", id);
        // throw new UserNotFoundException(messageSource.getMessage("error.404.user", new Object[]{id}, LocaleContextHolder.getLocale()));
    }

    @Deprecated
    private ModelAndView profileMav(long id, User currentUser, User user, String searchContext, DescriptionForm descriptionForm, String tabContext, Collection<Snippet> snippets, final int totalSnippetCount, final int totalUserSnippetCount, final int page, final boolean editing) {
        final ModelAndView mav = new ModelAndView("user/profile");

        MavHelper.addSnippetCardFavFormAttributes(mav, this.loginAuthentication.getLoggedInUser(), snippets);

        descriptionForm.setDescription(user.getDescription());
        mav.addObject("followedTags", this.tagService.getFollowedTagsForUser(user.getId()));
        mav.addObject("pages", totalSnippetCount / SNIPPET_PAGE_SIZE + (totalSnippetCount % SNIPPET_PAGE_SIZE == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("editing", editing);
        mav.addObject("isEdit", false);
        mav.addObject("user", user);
        mav.addObject("snippets", snippets);
        mav.addObject("snippetsCount", totalUserSnippetCount);
        mav.addObject("tabSnippetCount", totalSnippetCount);
        mav.addObject("searchContext", searchContext);
        mav.addObject("tabContext", tabContext);
        return mav;
    }

    @Deprecated
    @ModelAttribute
    public void addAttributes(Model model, @Valid final SearchForm searchForm) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        // MavHelper.addCurrentUserAttributes(model, currentUser, tagService, roleService);
        if (currentUser != null) {
            this.userService.updateLocale(currentUser.getId(), LocaleContextHolder.getLocale());
        }
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("searching", false);
    }

}
