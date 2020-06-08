package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Locale;

public final class TestMethods {
    private TestMethods() {}

    static Snippet insertSnippet(EntityManager em, User user, String title, String description, String code, Timestamp dateCreated, Language language, Collection<Tag> tags, boolean flagged, boolean deleted) {
        Snippet snippet = new Snippet(user, code, title, description, dateCreated, language, tags, flagged, deleted);
        em.persist(snippet);
        return snippet;
    }

    static User insertUser(EntityManager em, String username, String password, String email, Timestamp dateJoined, Locale locale, boolean verified) {
        User user = new User(username, password, email, dateJoined, locale, verified);
        em.persist(user);
        return user;
    }

    static Language insertLanguage(EntityManager em, String name) {
        Language lang = new Language(name);
        em.persist(lang);
        return lang;
    }

    static Role insertRole(EntityManager em, String name) {
        Role role = new Role(name);
        em.persist(role);
        return role;
    }

    static Tag insertTag(EntityManager em, String name) {
        Tag tag = new Tag(name);
        em.persist(tag);
        return tag;
    }

    static Vote insertVote(EntityManager em, User user, Snippet snippet, boolean isPositive) {
        Vote vote = new Vote(user, snippet, isPositive);
        em.persist(vote);
        return vote;
    }

    static void setUserFollowingTags(EntityManager em, User user, Collection<Tag> tags) {
        user.setFollowedTags(tags);
        em.persist(user);
    }

    static void setUserFavoriteSnippets(EntityManager em, User user, Collection<Snippet> snippets) {
        user.setFavorites(snippets);
        em.persist(user);
    }

// TODO REMOVE

//    static void insertSnippetTagIntoDb(SimpleJdbcInsert jdbcInsert, long snippet, long tag){
//        final Map<String, Object> map = new HashMap<String,Object>();
//        map.put("snippet_id", snippet);
//        map.put("tag_id",tag);
//
//        jdbcInsert.execute(map);
//    }
//
//    static void insertVotesForIntoDb(SimpleJdbcInsert jdbcInsert, long snippetId, long userId,int type){
//        final Map<String, Object> map = new HashMap<String,Object>();
//        map.put("snippet_id", snippetId);
//        map.put("user_id",userId);
//        map.put("type", type);
//
//        jdbcInsert.execute(map);
//    }
//
//    static void insertFavoriteIntoDb(SimpleJdbcInsert jdbcInsert, long snippetId, long userId){
//        final Map<String, Object> map = new HashMap<String,Object>();
//        map.put("snippet_id", snippetId);
//        map.put("user_id",userId);
//        jdbcInsert.execute(map);
//    }
//    static void insertFollowingIntoDb(SimpleJdbcInsert jdbcInsert, long tagId, long userId){
//        final Map<String, Object> map = new HashMap<String,Object>();
//        map.put("tag_id", tagId);
//        map.put("user_id",userId);
//
//        jdbcInsert.execute(map);
//    }
//
//    static void insertUserRoleIntoDb(SimpleJdbcInsert jdbcInsert, long roleid, long userid){
//        final Map<String, Object> map = new HashMap<String,Object>();
//        map.put("role_id", roleid);
//        map.put("user_id",userid);
//
//        jdbcInsert.execute(map);
//    }
}
