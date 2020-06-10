package ar.edu.itba.paw.persistence;

import java.time.Instant;
import java.util.Locale;

public final class TestConstants {

    static final String USER_PASSWORD = "Password";
    static final String USER_USERNAME = "Username";
    static final String USER_USERNAME2 = "Username2";
    static final String USER_USERNAME3 = "userNAME123";
    static final String USER_EMAIL3 = "user@gmail.com";
    static final String USER_EMAIL = "email@email.com";
    static final String USER_EMAIL2 = "email2@email.com";
    static final Locale LOCALE_EN = new Locale("en","EN");
    static final Locale LOCALE_ES = new Locale("es","ES");
    static final Instant USER_DATE = Instant.now();
    static final boolean USER_VERIFIED = true;
    static final long USER_INVALID_ID = 100;

    static final String SNIPPET_TITLE = "Snippet Title";
    static final String SNIPPET_TITLE2 = "Snippet Title 2";
    static final String SNIPPET_TITLE3 = "Snippet Title 3";
    static final String SNIPPET_TITLE4 = "Different";
    static final String SNIPPET_TITLE5 = "Some title this is";
    static final String SNIPPET_TITLE6 = "Hello!";
    static final String SNIPPET_TITLE7 = "Tester method";
    static final String SNIPPET_TITLE8 = "Random title";
    static final String SNIPPET_TITLE9 = "Includes username with a t";
    static final String SNIPPET_DESCR = "Description";
    static final String SNIPPET_CODE = "Snippet Code";
    static final String SNIPPET_CODE2 = "This code contains a 3";
    static final String SNIPPET_CODE3 = "Random with no matching words";
    static final boolean SNIPPET_FLAGGED = true;
    static final boolean SNIPPET_DELETED = true;
    static final long SNIPPET_INVALID_ID = 100;

    static final String LANGUAGE = "Language 1";
    static final String LANGUAGE2 = "Language 2";
    static final String LANGUAGE3 = "Language 3";
    static final long INVALID_LANGUAGE_ID = 1000000;
    static final String INVALID_LANGUAGE_NAME = "Not a lang";
    static final String VALID_LANGUAGE_NAME_SEARCH = "Language";
    static final String INVALID_LANGUAGE_NAME_SEARCH = "XXXX";

    static final String TAG = "tag1";
    static final String TAG2 = "tag2";
    static final String TAG3 = "tag3";
    static final String TAG4 = "Unpopular Tag";

    static final String ADMIN_ROLE = "ADMIN";
    static final String USER_ROLE = "USER";

    static final int SNIPPET_PAGE_SIZE = 6;
    static final int TAG_PAGE_SIZE = 20;
    static final int LANGUAGE_PAGE_SIZE = 20;

    /* Snippet Criteria Testing */
    static final String SNIPPET_TITLE_TERM = "Snippet T";
    static final String SNIPPET_TITLE_TERM_UNIQUE = "random TItle";
    static final String SNIPPET_TITLE_TERM_MANY = "T";
    static final String SNIPPET_TITLE_TERM_CAPS = "TITLE";
    static final String SNIPPET_TITLE_TERM_INVALID = "Sorting";
    static final String USER_NAME_TERM = "NAME1";
    static final String USER_NAME_TERM_INVALID = "userWiz";
    static final String TAG_TERM = "tag";
    static final String TAG_TERM_INVALID = "tag8";
    static final String CODE_TERM = "code";
    static final String CODE_TERM_INVALID = "INVALID";
    static final String LANGUAGE_TERM = "guage 2";

    static final String GENERAL_TERM = "3";

    /* Snippet Deep Criteria Testing */

    static final Instant DATE_1 = Instant.parse("2018-11-30T18:35:24.00Z");
    static final Instant DATE_2 = Instant.parse("2017-11-30T18:35:24.00Z");
    static final Instant DATE_3 = Instant.parse("2016-11-30T18:35:24.00Z");

    static final int repMin1 = -1;
    static final int repMin2 = 0;
    static final int repMin3 = 1;

    static final int repMax1 = -1;
    static final int repMax2 = 5;
    static final int repMax3 = 10;

    static final int voteMin1 = 0;
    static final int voteMin2 = 5;
    static final int voteMin3 = 10;
    static final int voteMinTooHigh = 10000000;

    private TestConstants() {}
}
