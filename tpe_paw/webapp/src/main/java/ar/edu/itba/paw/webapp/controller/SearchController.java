package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.utility.Constants;
import ar.edu.itba.paw.webapp.exception.*;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.utility.MavHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.*;

import static ar.edu.itba.paw.webapp.utility.Constants.MAX_SEARCH_QUERY_SIZE;
import static ar.edu.itba.paw.webapp.utility.Constants.SNIPPET_PAGE_SIZE;

@Controller
public class SearchController {

    private final static Map<String, SnippetDao.Types> typesMap;
    static {
        final Map<String, SnippetDao.Types> types = new HashMap<>();
        types.put(null, SnippetDao.Types.ALL);
        types.put("all", SnippetDao.Types.ALL);
        types.put("tag", SnippetDao.Types.TAG);
        types.put("title", SnippetDao.Types.TITLE);
        types.put("content", SnippetDao.Types.CONTENT);
        types.put("username", SnippetDao.Types.USER);
        types.put("language", SnippetDao.Types.LANGUAGE);
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

    @Autowired
    private SnippetService snippetService;
    @Autowired
    private LoginAuthentication loginAuthentication;
    @Autowired
    private TagService tagService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);
    private static final String HOME = "";
    private static final String FOLLOWING = "following/";
    private static final String FAVORITES = "favorites/";
    private static final String UPVOTED = "upvoted/";
    private static final String FLAGGED = "flagged/";
    private static final String LANGUAGES = "languages/";
    private static final String TAGS = "tags/";
    private static final String USER = "user/";

    @RequestMapping("/search")
    public ModelAndView searchInHome(@Valid @ModelAttribute("searchForm") final SearchForm searchForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("index");
        Collection<Snippet> snippets = this.findByCriteria(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.HOME, searchForm.getSort(), null, null, page);
        int totalSnippetCount = this.getSnippetByCriteriaCount(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.HOME, null, null);

        this.addModelAttributesHelper(mav, totalSnippetCount, page, snippets, HOME);
        MavHelper.addSnippetCardFavFormAttributes(mav, this.loginAuthentication.getLoggedInUser(), snippets);

        return mav;
    }

    @RequestMapping("/favorites/search")
    public ModelAndView searchInFavorites(@Valid @ModelAttribute("searchForm") final SearchForm searchForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("index");
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) this.logAndThrow(FAVORITES);

        Collection<Snippet> snippets = this.findByCriteria(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.FAVORITES, searchForm.getSort(), currentUser.getId(), null, page);
        int totalSnippetCount = this.getSnippetByCriteriaCount(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.FAVORITES, currentUser.getId(), null);

        this.addModelAttributesHelper(mav, totalSnippetCount, page, snippets, FAVORITES);
        MavHelper.addSnippetCardFavFormAttributes(mav, currentUser, snippets);
        return mav;
    }

    @RequestMapping("/following/search")
    public ModelAndView searchInFollowing(@Valid @ModelAttribute("searchForm") final SearchForm searchForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("snippet/snippetFollowing");
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) this.logAndThrow(FOLLOWING);

        Collection<Snippet> snippets = this.findByCriteria(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.FOLLOWING, searchForm.getSort(), currentUser.getId(), null, page);
        int totalSnippetCount = this.getSnippetByCriteriaCount(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.FOLLOWING, currentUser.getId(), null);
        this.addModelAttributesHelper(mav, totalSnippetCount, page, snippets, FOLLOWING);

        MavHelper.addTagChipUnfollowFormAttributes(mav, this.tagService.getMostPopularFollowedTagsForUser(currentUser.getId(), Constants.FOLLOWING_FEED_TAG_AMOUNT));
        MavHelper.addSnippetCardFavFormAttributes(mav, currentUser, snippets);

        return mav;
    }

    @RequestMapping("/upvoted/search")
    public ModelAndView searchInUpvoted(@Valid @ModelAttribute("searchForm") final SearchForm searchForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("index");
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) this.logAndThrow(UPVOTED);

        Collection<Snippet> snippets = this.findByCriteria(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.UPVOTED, searchForm.getSort(), currentUser.getId(), null, page);
        int totalSnippetCount = this.getSnippetByCriteriaCount(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.UPVOTED, currentUser.getId(), null);

        MavHelper.addSnippetCardFavFormAttributes(mav, currentUser, snippets);
        this.addModelAttributesHelper(mav, totalSnippetCount, page, snippets, UPVOTED);
        return mav;
    }

    @RequestMapping("/flagged/search")
    public ModelAndView searchInFlagged(@Valid @ModelAttribute("searchForm") final SearchForm searchForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("index");
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) this.logAndThrow(FLAGGED);

        Collection<Snippet> snippets = this.findByCriteria(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.FLAGGED, searchForm.getSort(), null, null, page);
        int totalSnippetCount = this.getSnippetByCriteriaCount(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.FLAGGED, null, null);

        MavHelper.addSnippetCardFavFormAttributes(mav, currentUser, snippets);
        this.addModelAttributesHelper(mav, totalSnippetCount, page, snippets, FLAGGED);
        return mav;
    }

    @RequestMapping("/languages/{langId}/search")
    public ModelAndView searchInLanguages(@PathVariable("langId") long langId, @Valid @ModelAttribute("searchForm") final SearchForm searchForm, @ModelAttribute("deleteForm") final DeleteForm deleteForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("tagAndLanguages/languageSnippets");
        // Retrieve the language
        Optional<Language> language = this.languageService.findById(langId);
        if (!language.isPresent()) {
            LOGGER.error("No language found with id {}", langId);
            throw new LanguageNotFoundException(messageSource.getMessage("error.404.language", new Object[]{langId}, LocaleContextHolder.getLocale()));
        }
        Collection<Snippet> snippets = this.findByCriteria(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.LANGUAGES, searchForm.getSort(), null, langId, page);
        int totalSnippetCount = this.getSnippetByCriteriaCount(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.LANGUAGES, null, langId);
        this.addModelAttributesHelper(mav, totalSnippetCount, page, snippets, LANGUAGES + langId + "/");
        MavHelper.addSnippetCardFavFormAttributes(mav, this.loginAuthentication.getLoggedInUser(), snippets);
        mav.addObject("language", language.get());
        return mav;
    }

    @RequestMapping("/tags/{tagId}/search")
    public ModelAndView searchInTags(@PathVariable("tagId") long tagId, @Valid @ModelAttribute("searchForm") final SearchForm searchForm, @ModelAttribute("followForm") final FollowForm followForm,
                                     @ModelAttribute("deleteForm") final DeleteForm deleteForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("tagAndLanguages/tagSnippets");
        // Retrieve the tag
        Optional<Tag> tag = this.tagService.findTagById(tagId);
        if (!tag.isPresent()) {
            LOGGER.error("No tag found with id {}", tagId);
            throw new TagNotFoundException(this.messageSource.getMessage("error.404.tag", new Object[]{tagId}, LocaleContextHolder.getLocale()));
        }
        //Retrieve the possible logged in user
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser != null) {
            followForm.setFollows(this.tagService.userFollowsTag(currentUser.getId(), tagId));
        }

        Collection<Snippet> snippets = this.findByCriteria(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.TAGS, searchForm.getSort(), null, tagId, page);
        int totalSnippetCount = this.getSnippetByCriteriaCount(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.TAGS, null, tagId);
        this.addModelAttributesHelper(mav, totalSnippetCount, page, snippets, TAGS + tagId + "/");
        MavHelper.addSnippetCardFavFormAttributes(mav, currentUser, snippets);
        mav.addObject("tag", tag.get());
        return mav;
    }

    @RequestMapping("/user/{id}/search")
    public ModelAndView searchUserProfileSnippets(
            final @PathVariable("id") long id,
            @ModelAttribute("profilePhotoForm") final ProfilePhotoForm profilePhotoForm,
            @ModelAttribute("descriptionForm") final DescriptionForm descriptionForm,
            @Valid @ModelAttribute("searchForm") final SearchForm searchForm,
            final @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            final @RequestParam(value = "editing", required = false, defaultValue = "false") boolean editing
    ) {
        String searchContext = String.format("user/%d/", id);
        Collection<Snippet> snippets = this.findByCriteria(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.USER, searchForm.getSort(), id, null, page);
        int totalSnippetCount = this.getSnippetByCriteriaCount(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.USER, id, null);

        return this.profileSearch(id, descriptionForm, Constants.USER_PROFILE_CONTEXT, searchContext, snippets, totalSnippetCount, page, editing);
    }

    @RequestMapping("/user/{id}/" + Constants.OWNER_DELETED_CONTEXT + "/search")
    public ModelAndView searchOwnerProfileDeletedSnippets(
            final @PathVariable("id") long id,
            @ModelAttribute("profilePhotoForm") final ProfilePhotoForm profilePhotoForm,
            @ModelAttribute("descriptionForm") final DescriptionForm descriptionForm,
            @Valid @ModelAttribute("searchForm") final SearchForm searchForm,
            final @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            final @RequestParam(value = "editing", required = false, defaultValue = "false") boolean editing
    ) {

        String searchContext = String.format("user/%d/%s/", id, Constants.OWNER_DELETED_CONTEXT);
        Collection<Snippet> snippets = this.findByCriteria(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.DELETED, searchForm.getSort(), id, null, page);
        int totalSnippetCount = this.getSnippetByCriteriaCount(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.DELETED, id, null);

        return this.profileSearch(id, descriptionForm, Constants.OWNER_DELETED_CONTEXT, searchContext, snippets, totalSnippetCount, page, editing);
    }

    @RequestMapping("/user/{id}/" + Constants.OWNER_ACTIVE_CONTEXT + "/search")
    public ModelAndView searchOwnerProfileSnippets(
            final @PathVariable("id") long id,
            @ModelAttribute("profilePhotoForm") final ProfilePhotoForm profilePhotoForm,
            @ModelAttribute("descriptionForm") final DescriptionForm descriptionForm,
            @Valid @ModelAttribute("searchForm") final SearchForm searchForm,
            final @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            final @RequestParam(value = "editing", required = false, defaultValue = "false") boolean editing
    ) {
        String searchContext = String.format("user/%d/%s/", id, Constants.OWNER_ACTIVE_CONTEXT);
        Collection<Snippet> snippets = this.findByCriteria(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.USER, searchForm.getSort(), id, null, page);
        int totalSnippetCount = this.getSnippetByCriteriaCount(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.USER, id, null);

        return this.profileSearch(id, descriptionForm, Constants.OWNER_ACTIVE_CONTEXT, searchContext, snippets, totalSnippetCount, page, editing);
    }

    private ModelAndView profileSearch(long id, DescriptionForm descriptionForm, String context, String searchContext, Collection<Snippet> snippets, int totalSnippetCount, int page, boolean editing) {
        final ModelAndView mav = new ModelAndView("user/profile");
        User profileUser = this.getUser(id, String.format("%s/search", searchContext));
        User currentUser = this.loginAuthentication.getLoggedInUser();

        descriptionForm.setDescription(profileUser.getDescription());

        //If context is "" but it is my profile, change it to active
        if (currentUser != null && currentUser.equals(profileUser) && context.equals(Constants.USER_PROFILE_CONTEXT)){
            context = Constants.OWNER_ACTIVE_CONTEXT;
            searchContext = String.format("user/%d/%s/", id, context);
        }

        this.addModelAttributesHelper(mav, totalSnippetCount, page, snippets, USER + id + "/");
        MavHelper.addSnippetCardFavFormAttributes(mav, currentUser, snippets);
        mav.addObject("followedTags", this.tagService.getFollowedTagsForUser(profileUser.getId()));
        mav.addObject("snippetsCount", profileUser.getCreatedSnippets().size());
        mav.addObject("editing", editing);
        mav.addObject("isEdit", false);
        mav.addObject("user", profileUser);
        mav.addObject("snippets", snippets);
        mav.addObject("tabContext", context);
        mav.addObject("searchContext", searchContext);
        return mav;
    }

    private Collection<Snippet> findByCriteria(String type, String query, SnippetDao.Locations location, String sort, Long userId, Long resourceId, int page) {
        if (query.length() > MAX_SEARCH_QUERY_SIZE)
            throw new URITooLongException(messageSource.getMessage("error.414.search", null, LocaleContextHolder.getLocale()));
        if (!typesMap.containsKey(type) || !ordersMap.containsKey(sort)) {
            return Collections.emptyList();
        } else {
            return this.snippetService.findSnippetByCriteria(
                    typesMap.get(type),
                    query == null ? "" : query,
                    location,
                    ordersMap.get(sort),
                    userId,
                    resourceId,
                    page,
                    SNIPPET_PAGE_SIZE);
        }
    }

    private int getSnippetByCriteriaCount(String type, String query, SnippetDao.Locations location, Long userId, Long resourceId) {
        return this.snippetService.getSnippetByCriteriaCount(
                typesMap.get(type),
                query == null ? "" : query,
                location,
                userId,
                resourceId);
    }

    private User getUser(final long id, String location) {
        Optional<User> maybeUser = this.userService.findUserById(id);
        if (!maybeUser.isPresent()) {
            this.logAndThrow(location);
        }
        return maybeUser.get();
    }

    private void logAndThrow(String location) {
        LOGGER.warn("User not found when searching profile {}", location);
        throw new ForbiddenAccessException(messageSource.getMessage("error.403", new Object[]{location}, LocaleContextHolder.getLocale()));
    }

    private void addModelAttributesHelper(ModelAndView mav, int snippetCount, int page, Collection<Snippet> snippets, String searchContext) {
        mav.addObject("pages", snippetCount / SNIPPET_PAGE_SIZE + (snippetCount % SNIPPET_PAGE_SIZE == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("snippetList", snippets);
        mav.addObject("totalSnippetCount", snippetCount);
        mav.addObject("searchContext", searchContext);
    }

    @ModelAttribute
    public void addAttributes(Model model, @Valid final SearchForm searchForm) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        MavHelper.addCurrentUserAttributes(model, currentUser, tagService, roleService);
        if (currentUser != null) {
            this.userService.updateLocale(currentUser.getId(), LocaleContextHolder.getLocale());
        }
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("searching", true);
    }
}
