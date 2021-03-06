package ar.edu.itba.paw.webapp.utility;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public final class Constants {
    public static final int SNIPPET_PAGE_SIZE = 6;
    public static final int TAG_PAGE_SIZE = 20;
    public static final int LANGUAGE_PAGE_SIZE = 15;
    public static final int MAX_SEARCH_QUERY_SIZE = 1024;
    public static final int MENU_FOLLOWING_TAG_AMOUNT = 7;
    public static final int FOLLOWING_FEED_TAG_AMOUNT = 25;

    public static final String REFERER = "Referer";
    public static final String HOME = "/";
    public static final String REDIRECT_ATTRIBUTE = "url_prior_login";
    public static final String SAVED_REQUEST_ATTRIBUTE = "SPRING_SECURITY_SAVED_REQUEST";

    public static final String OWNER_DELETED_CONTEXT = "deleted";
    public static final String OWNER_ACTIVE_CONTEXT = "active";
    public static final String USER_PROFILE_CONTEXT = "";

    public static final String LOGIN = "/login";
    public static final String SIGNUP = "/signup";
    public static final String GOODBYE = "/goodbye";
    public static final String RESET_PASSWORD = "/reset_password";
    public static final String RECOVER_PASSWORD = "/recover_password";

    public static final String QUERY_PARAM_PAGE = "page";
    public static final String QUERY_PARAM_NAME = "name";
    public static final String QUERY_PARAM_SHOW_ONLY_FOLLOWING = "show_only_following";
    public static final String QUERY_PARAM_SHOW_EMPTY = "show_empty";
    public static final String PATH_PARAM_ID = "id";
    public static final String USER_PARAM_ID = "userId";
    public static final String PATH_PARAM_TOKEN = "token";

    public static final String LINK_FIRST = "first";
    public static final String LINK_LAST = "last";
    public static final String LINK_PREV = "prev";
    public static final String LINK_NEXT = "next";

    public static final String HEADER_ALL_ITEMS = "X-ALL-ITEMS";

    public static final String API_PREFIX = "/api/v1/";

    public static final int MB_1 = 1048576;
    public static final int UPLOAD_MAX_SIZE = 1 * MB_1;

    // Refresh authority to check if the token contains
    public static final GrantedAuthority REFRESH_AUTHORITY = new SimpleGrantedAuthority(Authorities.REFRESH.getValue());


    /*
     * "The AssertionError isn’t strictly required, but it provides insurance in case the
     *  constructor is accidentally invoked from within the class" - Page 19, Effective Java
     */
    private Constants() {
        throw new AssertionError();
    }
}
