package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.RoleService;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.dto.ImageDto;
import ar.edu.itba.paw.webapp.dto.SnippetDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.utility.PagingHelper;
import ar.edu.itba.paw.webapp.utility.ResponseHelper;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.utility.Constants.*;

//@Controller
@Component
@Path("users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private SnippetService snippetService;
    @Autowired
    private LoginAuthentication loginAuthentication;
    //    @Autowired
    private TagService tagService;

    //    @Autowired
    private RoleService roleService;
    //    @Autowired
    private MessageSource messageSource;

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

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

            final List<SnippetDto> snippets = this.snippetService.getAllSnippetsByOwner(user.getId(), page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, uriInfo)).collect(Collectors.toList());
            final int pageCount = PagingHelper.CalculateTotalPages(this.snippetService.getAllSnippetsByOwnerCount(user.getId()), SNIPPET_PAGE_SIZE);

            Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
            });
            ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
            return builder.build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
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
                final List<SnippetDto> snippets = this.snippetService.getAllDeletedSnippetsByOwner(user.getId(), page, SNIPPET_PAGE_SIZE).stream().map(s -> SnippetDto.fromSnippet(s, uriInfo)).collect(Collectors.toList());
                final int pageCount = PagingHelper.CalculateTotalPages(this.snippetService.getAllDeletedSnippetsByOwnerCount(user.getId()), SNIPPET_PAGE_SIZE);

                Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<SnippetDto>>(snippets) {
                });
                ResponseHelper.AddLinkAttributes(builder, this.uriInfo, page, pageCount);
                return builder.build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
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

    /*@RequestMapping(value = "/user/{id}/{context}", method = {RequestMethod.POST})
    public ModelAndView endEditPhoto(
            final @PathVariable("id") long id,
            final @PathVariable("context") String context,
            @ModelAttribute("profilePhotoForm") @Valid final ProfilePhotoForm profilePhotoForm,
            final BindingResult errors,
            @ModelAttribute("descriptionForm") final DescriptionForm descriptionForm
    ){
        List<String> possibleContexts = new ArrayList<>();
        possibleContexts.add(Constants.OWNER_DELETED_CONTEXT);
        possibleContexts.add(Constants.OWNER_ACTIVE_CONTEXT);
        possibleContexts.add(Constants.USER_PROFILE_CONTEXT);

        if (!possibleContexts.contains(context)) {
            LOGGER.warn("Invalid URL in profile. Context = {}", context);
            throw new InvalidUrlException();
        }
        if (errors.hasErrors()){
            return userProfile(id, profilePhotoForm, descriptionForm, 1, false);
        }

        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser != null && currentUser.getId() == id) {
            try {
                this.userService.changeProfilePhoto(id, profilePhotoForm.getFile().getBytes());
                LOGGER.debug("User {} changed their profile picture", id);
            } catch (IOException e) {
                LOGGER.error("Exception changing profile photo for user {}", id);
                FieldError photoError = new FieldError("profilePhotoForm","file" , messageSource.getMessage("profile.photo.error", null, LocaleContextHolder.getLocale()));
                errors.addError(photoError);
                return userProfile(id, profilePhotoForm, descriptionForm, 1, false);
            }
        }
        return new ModelAndView("redirect:/user/" + id + "/" + context);
    }

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
    }

    private void logAndThrow(long id) {
        LOGGER.warn("User with id {} doesn't exist", id);
        throw new UserNotFoundException(messageSource.getMessage("error.404.user", new Object[]{id}, LocaleContextHolder.getLocale()));
    }

    private User getUserWithId(final long id) {
        Optional<User> user = this.userService.findUserById(id);
        if (!user.isPresent() || this.roleService.isAdmin(user.get().getId())) {
            this.logAndThrow(id);
        }
        return user.get();
    }

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

    @ModelAttribute
    public void addAttributes(Model model, @Valid final SearchForm searchForm) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        MavHelper.addCurrentUserAttributes(model, currentUser, tagService, roleService);
        if (currentUser != null) {
            this.userService.updateLocale(currentUser.getId(), LocaleContextHolder.getLocale());
        }
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("searching", false);
    }*/

}
