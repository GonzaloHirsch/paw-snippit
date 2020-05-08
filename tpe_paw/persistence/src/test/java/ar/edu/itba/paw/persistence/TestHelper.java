package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TestHelper {

    public static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static final String SNIPPETS_TABLE = "snippets";
    public static final String USERS_TABLE = "users";
    public static final String LANGUAGES_TABLE = "languages";
    public static final String TAGS_TABLE = "tags";
    public static final String SNIPPET_TAGS_TABLE = "snippet_tags";
    public static final String VOTES_FOR_TABLE = "votes_for";
    public static final String FAVORITES_TABLE = "favorites";
    public static final String FOLLOWS_TABLE = "follows";
    public static final String ROLES_TABLE = "roles";
    public static final String ROLES_USER_TABLE = "user_roles";

    public static final String PASSWORD = "Password";
    public static final String USERNAME = "Username";
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD2 = "Password2";
    public static final String USERNAME2 = "Username2";
    public static final String EMAIL2 = "email2@email.com";
    public static final Locale LOCALE_EN = new Locale("en");
    public static final Locale LOCALE_ES = new Locale("es");

    public static final String TITLE = "Snippet Title";
    public static final String TITLE2 = "Snippet Title 2";
    public static final String DESCR = "Description";
    public static final String CODE = "Snippet Code";

    public static final String LANGUAGE = "Language 1";
    public static final String LANGUAGE2 = "Language 2";
    public static final String LANGUAGE3 = "Language 3";

    public static final String TAG = "tag1";
    public static final String TAG2 = "tag2";

    public static final String ADMIN_ROLE = "ADMIN";
    public static final String USER_ROLE = "USER";

    public static final int PAGE_SIZE = 6;

    public static long insertSnippetIntoDb(SimpleJdbcInsert jdbcInsert, long userId, String title, String description, String code, long languageId,int flagged){
        final Map<String, Object> snippetDataMap = new HashMap<String,Object>(){{
            put("user_id", userId);
            put("title", title);
            put("description",description);
            put("code",code);
            put("date_created", DATE.format(Calendar.getInstance().getTime().getTime()));
            put("language_id", languageId);
            put("flagged",flagged);
        }};
        return jdbcInsert.executeAndReturnKey(snippetDataMap).longValue();
    }

    public static User insertUserIntoDb(SimpleJdbcInsert jdbcInsertUser, String username, String password, String email,String description, Locale locale){
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("username", username);
            put("password", password);
            put("email", email);
            put("reputation", 0);
            put("date_joined", DATE.format(Calendar.getInstance().getTime().getTime()));
            put("lang", "en");
            put("region", "US");
            put("verified", 0);
        }};
        long userId = jdbcInsertUser.executeAndReturnKey(map).longValue();
        return new User(userId, username, password, email, description, 0, DATE.format(Calendar.getInstance().getTime().getTime()), null,locale , false);

    }

    public static long insertLanguageIntoDb(SimpleJdbcInsert jdbcInsertLanguage, String name){
        final Map<String, Object> map = new HashMap<String,Object>(){{
            put("name", name);
        }};
        return jdbcInsertLanguage.executeAndReturnKey(map).longValue();
    }

    public static long insertTagIntoDb(SimpleJdbcInsert jdbcInsertTag, String name){
        final Map<String, Object> map = new HashMap<String,Object>(){{
            put("name", name);
        }};
        return jdbcInsertTag.executeAndReturnKey(map).longValue();
    }

    public static void insertSnippetTagIntoDb(SimpleJdbcInsert jdbcInsert, long snippet, long tag){
        final Map<String, Object> map = new HashMap<String,Object>(){{
            put("snippet_id", snippet);
            put("tag_id",tag);
        }};

        jdbcInsert.execute(map);
    }

    public static void insertVotesForIntoDb(SimpleJdbcInsert jdbcInsert, long snippetId, long userId,int type){
        final Map<String, Object> map = new HashMap<String,Object>(){{
            put("snippet_id", snippetId);
            put("user_id",userId);
            put("type", type);
        }};

        jdbcInsert.execute(map);
    }

    public static void insertFavoriteIntoDb(SimpleJdbcInsert jdbcInsert, long snippetId, long userId){
        final Map<String, Object> map = new HashMap<String,Object>(){{
            put("snippet_id", snippetId);
            put("user_id",userId);
        }};

        jdbcInsert.execute(map);
    }
    public static void insertFollowingIntoDb(SimpleJdbcInsert jdbcInsert, long tagId, long userId){
        final Map<String, Object> map = new HashMap<String,Object>(){{
            put("tag_id", tagId);
            put("user_id",userId);
        }};

        jdbcInsert.execute(map);
    }

    public static long insertRoleIntoDb(SimpleJdbcInsert jdbcInsert, String role){
        final Map<String, Object> map = new HashMap<String,Object>(){{
            put("role", role);
        }};

        return jdbcInsert.executeAndReturnKey(map).longValue();
    }

    public static void insertUserRoleIntoDb(SimpleJdbcInsert jdbcInsert, long roleid, long userid){
        final Map<String, Object> map = new HashMap<String,Object>(){{
            put("role_id", roleid);
            put("user_id",userid);
        }};

        jdbcInsert.execute(map);
    }


}
