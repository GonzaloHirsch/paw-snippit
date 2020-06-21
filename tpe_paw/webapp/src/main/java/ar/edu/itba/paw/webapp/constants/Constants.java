package ar.edu.itba.paw.webapp.constants;

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
    public static final String RESET_PASSWORD = "/reset-password";
    public static final String RECOVER_PASSWORD = "/recover-password";

    /*
     * "The AssertionError isn’t strictly required, but it provides insurance in case the
     *  constructor is accidentally invoked from within the class" - Page 19, Effective Java
     */
    private Constants() {
        throw new AssertionError();
    }
}
