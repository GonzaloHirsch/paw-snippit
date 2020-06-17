package ar.edu.itba.paw.persistence;

import java.time.Instant;
import java.util.Locale;

public final class TestConstants {

    static final String USER_PASSWORD = "Password";
    static final String USER_USERNAME = "Username";
    static final String USER_USERNAME2 = "Username2";
    static final String USER_USERNAME3 = "userNAME123";
    static final String USER_USERNAME4 = "userNAME4";
    static final String USER_USERNAME5 = "userNAME5";
    static final String USER_USERNAME6 = "userNAME6";
    static final String USER_USERNAME7 = "userNAME7";
    static final String USER_USERNAME8 = "userNAME8";
    static final String USER_USERNAME9 = "userNAME9";
    static final String USER_USERNAME10 = "userNAME10";

    static final String USER_EMAIL = "email@email.com";
    static final String USER_EMAIL2 = "email2@email.com";
    static final String USER_EMAIL3 = "user3@gmail.com";
    static final String USER_EMAIL4 = "user4@gmail.com";
    static final String USER_EMAIL5 = "user5@gmail.com";
    static final String USER_EMAIL6 = "user6@gmail.com";
    static final String USER_EMAIL7 = "user7@gmail.com";
    static final String USER_EMAIL8 = "user8@gmail.com";
    static final String USER_EMAIL9 = "user9@gmail.com";
    static final String USER_EMAIL10 = "user10@gmail.com";

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
    static final Instant DATE_2 = Instant.parse("2014-11-30T18:35:24.00Z");
    static final Instant DATE_3 = Instant.parse("2016-11-30T18:35:24.00Z");
    static final Instant DATE_4 = Instant.parse("2016-12-30T18:35:24.00Z");
    static final Instant DATE_5 = Instant.parse("2016-12-30T19:35:24.00Z");
    static final Instant DATE_6 = Instant.parse("2016-12-30T18:34:24.00Z");
    static final Instant DATE_7 = Instant.parse("2013-11-30T18:35:24.00Z");
    static final Instant DATE_8 = Instant.parse("2012-11-30T18:35:24.00Z");
    static final Instant DATE_9 = Instant.parse("2011-11-30T18:35:24.00Z");
    static final Instant DATE_MIN_MINUTES = Instant.parse("2016-12-30T18:30:00.00Z");
    static final Instant DATE_MAX_MINUTES = Instant.parse("2016-12-30T18:40:00.00Z");
    static final Instant DATE_MIN_YEAR = Instant.parse("2016-01-01T00:00:00.00Z");
    static final Instant DATE_MAX_YEAR = Instant.parse("2017-01-01T00:00:00.00Z");

    static final int REP_VALUE_1 = -10;
    static final int REP_VALUE_2 = -1;
    static final int REP_VALUE_3 = 0;
    static final int REP_VALUE_4 = 5;
    static final int REP_VALUE_5 = 10;
    static final int REP_VALUE_6 = 10000000;

    static final int VOTE_VALUE_1 = 0;
    static final int VOTE_VALUE_2 = 5;
    static final int VOTE_VALUE_3 = 10;
    static final int VOTE_VALUE_4 = 10000000;

    private TestConstants() {
        throw new AssertionError();
    }
}
