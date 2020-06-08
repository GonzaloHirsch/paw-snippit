package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class TestConstants {

    static final long USER_ID = 1;
    static final String USER_PASSWORD = "Password";
    static final String USER_USERNAME = "Username";
    static final String USER_EMAIL = "email@email.com";
    static final String USER_PASSWORD2 = "Password2";
    static final String USER_USERNAME2 = "Username2";
    static final String USER_EMAIL2 = "email2@email.com";
    static final Locale LOCALE_EN = new Locale("en","EN");
    static final Locale LOCALE_ES = new Locale("es","ES");
    static final Timestamp USER_DATE = Timestamp.from(Instant.now());
    static final boolean USER_NOT_VERIFIED = false;
    static final boolean USER_VERIFIED = true;

    static final String SNIPPET_TITLE = "Snippet Title";
    static final String SNIPPET_TITLE2 = "Snippet Title 2";
    static final String SNIPPET_DESCR = "Description";
    static final String SNIPPET_CODE = "Snippet Code";
    static final boolean SNIPPET_FLAGGED = true;
    static final boolean SNIPPET_DELETED = true;
    static final long SNIPPET_INVALID_ID = 100;

    static final String LANGUAGE = "Language 1";
    static final String LANGUAGE2 = "Language 2";
    static final String LANGUAGE3 = "Language 3";

    static final String TAG = "tag1";
    static final String TAG2 = "tag2";

    static final String ADMIN_ROLE = "ADMIN";
    static final String USER_ROLE = "USER";

    public static final int SNIPPET_PAGE_SIZE = 6;
    public static final int TAG_PAGE_SIZE = 20;
    public static final int LANGUAGE_PAGE_SIZE = 15;

    private TestConstants() {}
}
