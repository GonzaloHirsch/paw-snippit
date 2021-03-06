package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.time.Instant;
import java.util.*;

public final class TestMethods {
    private TestMethods() {
        throw new AssertionError();
    }

    static Snippet insertSnippet(EntityManager em, User user, String title, String description, String code, Instant dateCreated, Language language, Collection<Tag> tags, boolean flagged, boolean deleted) {
        Snippet snippet = new Snippet(user, code, title, description, dateCreated, language, tags, flagged, deleted);
        em.persist(snippet);
        return snippet;
    }

    static User insertUser(EntityManager em, String username, String password, String email, Instant dateJoined, Locale locale, boolean verified) {
        User user = new User(username, password, email, dateJoined, locale, verified);
        em.persist(user);
        return user;
    }

    static Language insertLanguage(EntityManager em, String name) {
        Language lang = new Language(name);
        em.persist(lang);
        return lang;
    }

    static Report insertReport(EntityManager em, User reportedBy, Snippet snippet, String detail, boolean ownerDismissed) {
        Report report = new Report(reportedBy, snippet, detail, ownerDismissed);
        em.persist(report);
        return report;
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

    static int countRows(EntityManager em, String tableName) {
        return ((BigInteger) em.createNativeQuery("SELECT COUNT(*) FROM " + tableName).getSingleResult()).intValue();
    }

    static void setUserFollowingTags(EntityManager em, User user, Collection<Tag> tags) {
        user.setFollowedTags(tags);
        em.persist(user);
    }

    static void setUserFavoriteSnippets(EntityManager em, User user, Collection<Snippet> snippets) {
        user.setFavorites(snippets);
        em.persist(user);
    }

    static void setSnippetsToLanguage(EntityManager em, Language lang, Collection<Snippet> snippets) {
        lang.setSnippetsUsing(snippets);
        em.persist(lang);
    }

    static void setUserRoles(EntityManager em, User user, Collection<Role> roles) {
        user.setRoles(roles);
        em.persist(user);
    }

    static void setUserReputation(EntityManager em, User user, int reputation) {
        user.setReputation(reputation);
        em.persist(user);
    }

    static Map<String, Tag> tagsCreation(EntityManager em) {
        return tagsCreation(em, TestMethods.insertTag(em, TestConstants.TAG));
    }

    static Map<String, Tag> tagsCreation(EntityManager em, Tag existingTag) {
        Map<String, Tag> data = new HashMap<>();

        /* TAGS */
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        Tag tag3 = TestMethods.insertTag(em, TestConstants.TAG3);
        data.put(existingTag.getName(), existingTag);
        data.put(tag2.getName(), tag2);
        data.put(tag3.getName(), tag3);
        return data;
    }

    static Map<String, Language> languageCreation(EntityManager em) {
        return languageCreation(em, TestMethods.insertLanguage(em, TestConstants.LANGUAGE));
    }

    static Map<String, Language> languageCreation(EntityManager em, Language existingLang) {
        Map<String, Language> data = new HashMap<>();
        Language language2 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        Language language3 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE3);
        data.put(existingLang.getName(), existingLang);
        data.put(language2.getName(), language2);
        data.put(language3.getName(), language3);
        return data;
    }

    static Map<String, User> userCreation(EntityManager em) {
        return userCreation(em, TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED));
    }

    static Map<String, User> userCreation(EntityManager em, User existingUser) {
        Map<String, User> data = new HashMap<>();

        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);
        User user2 = TestMethods.insertUser(em, TestConstants.USER_USERNAME3, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL3, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);
        data.put(existingUser.getUsername(), existingUser);
        data.put(user.getUsername(), user);
        data.put(user2.getUsername(), user2);
        return data;
    }

    static Map<String, Snippet> snippetCreation(
            EntityManager em,
            Map<String, Tag> tagMap,
            Map<String, Language> langMap,
            Map<String, User> userMap
    ) {
        Map<String, Snippet> data = new HashMap<>();

        User user = userMap.get(TestConstants.USER_USERNAME2);
        User user2 = userMap.get(TestConstants.USER_USERNAME3);
        User user3 = userMap.get(TestConstants.USER_USERNAME);

        Language language = langMap.get(TestConstants.LANGUAGE);
        Language language2 = langMap.get(TestConstants.LANGUAGE2);
        Language language3 = langMap.get(TestConstants.LANGUAGE3);

        Tag tag = tagMap.get(TestConstants.TAG);
        Tag tag2 = tagMap.get(TestConstants.TAG2);
        Tag tag3 = tagMap.get(TestConstants.TAG3);

        Snippet snip1 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag2), false, TestConstants.SNIPPET_DELETED);
        Snippet snip2 = TestMethods.insertSnippet(em, user2, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE2, Instant.now(), language3, Arrays.asList(tag, tag2, tag3), TestConstants.SNIPPET_FLAGGED, false);
        Snippet snip3 = TestMethods.insertSnippet(em, user3, TestConstants.SNIPPET_TITLE3, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE3, Instant.now(), language, Collections.singletonList(tag2), TestConstants.SNIPPET_FLAGGED, TestConstants.SNIPPET_DELETED);
        Snippet snip4 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE4, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language2, Arrays.asList(tag3, tag2), false, false);
        Snippet snip5 = TestMethods.insertSnippet(em, user2, TestConstants.SNIPPET_TITLE5, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language2, Collections.singletonList(tag), false, false);
        Snippet snip6 = TestMethods.insertSnippet(em, user3, TestConstants.SNIPPET_TITLE6, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE3, Instant.now(), language3, Collections.singletonList(tag2), TestConstants.SNIPPET_FLAGGED, false);
        Snippet snip7 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE7, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE2, Instant.now(), language, Arrays.asList(tag, tag2, tag3), false, false);
        Snippet snip8 = TestMethods.insertSnippet(em, user2, TestConstants.SNIPPET_TITLE8, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE2, Instant.now(), language2, Collections.singletonList(tag3), false, false);
        Snippet snip9 = TestMethods.insertSnippet(em, user3, TestConstants.SNIPPET_TITLE9, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language2, Collections.emptyList(), TestConstants.SNIPPET_FLAGGED, false);
        data.put(snip1.getTitle(), snip1);
        data.put(snip2.getTitle(), snip2);
        data.put(snip3.getTitle(), snip3);
        data.put(snip4.getTitle(), snip4);
        data.put(snip5.getTitle(), snip5);
        data.put(snip6.getTitle(), snip6);
        data.put(snip7.getTitle(), snip7);
        data.put(snip8.getTitle(), snip8);
        data.put(snip9.getTitle(), snip9);

        return data;
    }

    /* DEEP SEARCH */

    static Map<String, User> userCreationDeepSearch(EntityManager em, User existingUser) {
        Map<String, User> data = new HashMap<>();

        User user2 = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, TestConstants.USER_VERIFIED);
        User user3 = TestMethods.insertUser(em, TestConstants.USER_USERNAME3, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL3, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);
        User user4 = TestMethods.insertUser(em, TestConstants.USER_USERNAME4, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL4, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);
        User user5 = TestMethods.insertUser(em, TestConstants.USER_USERNAME5, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL5, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);
        User user6 = TestMethods.insertUser(em, TestConstants.USER_USERNAME6, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL6, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);
        User user7 = TestMethods.insertUser(em, TestConstants.USER_USERNAME7, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL7, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);
        User user8 = TestMethods.insertUser(em, TestConstants.USER_USERNAME8, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL8, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);
        User user9 = TestMethods.insertUser(em, TestConstants.USER_USERNAME9, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL9, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);
        User user10 = TestMethods.insertUser(em, TestConstants.USER_USERNAME10, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL10, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);

        data.put(existingUser.getUsername(), existingUser);
        data.put(user2.getUsername(), user2);
        data.put(user3.getUsername(), user3);
        data.put(user4.getUsername(), user4);
        data.put(user5.getUsername(), user5);
        data.put(user6.getUsername(), user6);
        data.put(user7.getUsername(), user7);
        data.put(user8.getUsername(), user8);
        data.put(user9.getUsername(), user9);
        data.put(user10.getUsername(), user10);
        return data;
    }

    static Map<String, Snippet> dataForSearchByDeepCriteria (
        EntityManager em,
        Map<String, User> users,
        Tag tag,
        Language language
    ) {
        Map<String, Snippet> data = new HashMap<>();

        User owner = users.get(TestConstants.USER_USERNAME);
        User user2 = users.get(TestConstants.USER_USERNAME2);
        User user3 = users.get(TestConstants.USER_USERNAME3);
        User user4 = users.get(TestConstants.USER_USERNAME4);
        User user5 = users.get(TestConstants.USER_USERNAME5);
        User user6 = users.get(TestConstants.USER_USERNAME6);
        User user7 = users.get(TestConstants.USER_USERNAME7);
        User user8 = users.get(TestConstants.USER_USERNAME8);
        User user9 = users.get(TestConstants.USER_USERNAME9);
        User user10 = users.get(TestConstants.USER_USERNAME10);

        /* SNIPPETS */
        Snippet snip1 = TestMethods.insertSnippet(em, user10, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, TestConstants.DATE_1, language, Collections.singletonList(tag), false, false);
        Snippet snip2 = TestMethods.insertSnippet(em, user2, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE2, TestConstants.DATE_2, language, Collections.singletonList(tag), false, false);
        Snippet snip3 = TestMethods.insertSnippet(em, user3, TestConstants.SNIPPET_TITLE3, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE3, TestConstants.DATE_3, language, Collections.singletonList(tag), false, false);
        Snippet snip4 = TestMethods.insertSnippet(em, user4, TestConstants.SNIPPET_TITLE4, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, TestConstants.DATE_4, language, Collections.singletonList(tag), false, false);
        Snippet snip5 = TestMethods.insertSnippet(em, user5, TestConstants.SNIPPET_TITLE5, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, TestConstants.DATE_5, language, Collections.singletonList(tag), false, false);
        Snippet snip6 = TestMethods.insertSnippet(em, user6, TestConstants.SNIPPET_TITLE6, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE3, TestConstants.DATE_6, language, Collections.emptyList(), false, false);
        Snippet snip7 = TestMethods.insertSnippet(em, user7, TestConstants.SNIPPET_TITLE7, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE2, TestConstants.DATE_7, language, Collections.emptyList(), false, false);
        Snippet snip8 = TestMethods.insertSnippet(em, user8, TestConstants.SNIPPET_TITLE8, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE2, TestConstants.DATE_8, language, Collections.emptyList(), false, false);
        Snippet snip9 = TestMethods.insertSnippet(em, user9, TestConstants.SNIPPET_TITLE9, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, TestConstants.DATE_9, language, Collections.emptyList(), false, false);
        data.put(snip1.getTitle(), snip1);
        data.put(snip2.getTitle(), snip2);
        data.put(snip3.getTitle(), snip3);
        data.put(snip4.getTitle(), snip4);
        data.put(snip5.getTitle(), snip5);
        data.put(snip6.getTitle(), snip6);
        data.put(snip7.getTitle(), snip7);
        data.put(snip8.getTitle(), snip8);
        data.put(snip9.getTitle(), snip9);

        /* VOTES */

        TestMethods.insertVote(em, user2, snip1, true);
        TestMethods.insertVote(em, user3, snip1, true);
        TestMethods.insertVote(em, user4, snip1, true);
        TestMethods.insertVote(em, user5, snip1, true);
        TestMethods.insertVote(em, user6, snip1, true);

        TestMethods.insertVote(em, user2, snip2, false);
        TestMethods.insertVote(em, user3, snip2, false);
        TestMethods.insertVote(em, user4, snip2, false);
        TestMethods.insertVote(em, user5, snip2, false);
        TestMethods.insertVote(em, user6, snip2, false);

        TestMethods.insertVote(em, user2, snip3, true);
        TestMethods.insertVote(em, user3, snip3, false);

        TestMethods.insertVote(em, user2, snip4, false);
        TestMethods.insertVote(em, user3, snip4, false);
        TestMethods.insertVote(em, user4, snip4, false);
        TestMethods.insertVote(em, user5, snip4, false);
        TestMethods.insertVote(em, user6, snip4, false);
        TestMethods.insertVote(em, user7, snip4, true);
        TestMethods.insertVote(em, user8, snip4, true);
        TestMethods.insertVote(em, user9, snip4, true);
        TestMethods.insertVote(em, user10, snip4, true);
        TestMethods.insertVote(em, owner, snip4, true);

        TestMethods.insertVote(em, user2, snip5, true);
        TestMethods.insertVote(em, user3, snip5, true);
        TestMethods.insertVote(em, user4, snip5, true);
        TestMethods.insertVote(em, user5, snip5, true);
        TestMethods.insertVote(em, user6, snip5, true);
        TestMethods.insertVote(em, user7, snip5, true);
        TestMethods.insertVote(em, user8, snip5, true);
        TestMethods.insertVote(em, user9, snip5, true);
        TestMethods.insertVote(em, user10, snip5, true);
        TestMethods.insertVote(em, owner, snip5, true);

        /* REPUTATION UPDATE */
        // Note: this is here because hsql will not respond to update_reputation_trigger
        // and the way reputations are obtained is irrelevant to these (SnippetDao) tests

        TestMethods.setUserReputation(em, user10, 5);
        TestMethods.setUserReputation(em, user2, -5);
        TestMethods.setUserReputation(em, user3, 0);
        TestMethods.setUserReputation(em, user4, 0);
        TestMethods.setUserReputation(em, user5, 10);

        return data;
    }
}
