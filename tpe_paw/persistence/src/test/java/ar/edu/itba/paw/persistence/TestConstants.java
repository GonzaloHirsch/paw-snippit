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

    static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").withLocale(Locale.UK).withZone(ZoneId.systemDefault());
    static final String SNIPPETS_TABLE = "snippets";
    static final String USERS_TABLE = "users";
    static final String LANGUAGES_TABLE = "languages";
    static final String TAGS_TABLE = "tags";
    static final String SNIPPET_TAGS_TABLE = "snippet_tags";
    static final String VOTES_FOR_TABLE = "votes_for";
    static final String FAVORITES_TABLE = "favorites";
    static final String FOLLOWS_TABLE = "follows";
    static final String ROLES_TABLE = "roles";
    static final String ROLES_USER_TABLE = "user_roles";

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

    static final long LANGUAGE_ID = 1;
    static final String LANGUAGE = "Language 1";
    static final long LANGUAGE2_ID = 2;
    static final String LANGUAGE2 = "Language 2";
    static final long LANGUAGE3_ID = 3;
    static final String LANGUAGE3 = "Language 3";

    static final String TAG = "tag1";
    static final String TAG2 = "tag2";

    static final String ADMIN_ROLE = "ADMIN";
    static final String USER_ROLE = "USER";

    static final int PAGE_SIZE = 6;

    private TestConstants() {}
}
