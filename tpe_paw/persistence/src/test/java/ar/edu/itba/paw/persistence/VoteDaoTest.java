package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.VoteDao;
import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.Vote;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class VoteDaoTest {

    @Autowired
    private VoteDao voteDao;

    @PersistenceContext
    private EntityManager em;

    @Before
    public void setUp() {

    }

    @Test
    public void testGetVotePositiveExists(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, TestConstants.USER_VERIFIED);
        Language language = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        Snippet snip1 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, TestConstants.DATE_1, language, Collections.emptyList(), false, false);

        TestMethods.insertVote(em, user, snip1, true);

        Optional<Vote> vote = voteDao.getVote(user.getId(), snip1.getId());

        Assert.assertTrue(vote.isPresent());
        Assert.assertEquals(user, vote.get().getUser());
        Assert.assertEquals(snip1, vote.get().getSnippet());
    }

    @Test
    public void testGetVoteNegativeExists(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, TestConstants.USER_VERIFIED);
        Language language = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        Snippet snip1 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, TestConstants.DATE_1, language, Collections.emptyList(), false, false);

        TestMethods.insertVote(em, user, snip1, false);

        Optional<Vote> vote = voteDao.getVote(user.getId(), snip1.getId());

        Assert.assertTrue(vote.isPresent());
        Assert.assertEquals(user, vote.get().getUser());
        Assert.assertEquals(snip1, vote.get().getSnippet());
    }


    @Test
    public void testGetVoteNotExists(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, TestConstants.USER_VERIFIED);
        Language language = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        Snippet snip1 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, TestConstants.DATE_1, language, Collections.emptyList(), false, false);

        Optional<Vote> vote = voteDao.getVote(user.getId(), snip1.getId());

        Assert.assertFalse(vote.isPresent());
    }


    @Test
    public void testGetUserVotes(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, TestConstants.USER_VERIFIED);
        Language language = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        Snippet snip1 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, TestConstants.DATE_1, language, Collections.emptyList(), false, false);
        Snippet snip2 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE2, TestConstants.DATE_2, language, Collections.emptyList(), false, false);

        Vote vote1 = TestMethods.insertVote(em, user, snip1, true);
        Vote vote2 = TestMethods.insertVote(em, user, snip2, false);
        user.setVotes(Arrays.asList(vote1, vote2));

        Collection<Vote> votes = voteDao.getUserVotes(user.getId());

        Assert.assertEquals(2, votes.size());
    }

    @Test
    public void testGetUserVotesDeletedSnippet(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, TestConstants.USER_VERIFIED);
        Language language = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        Snippet snip1 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, TestConstants.DATE_1, language, Collections.emptyList(), false, true);
        Snippet snip2 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE2, TestConstants.DATE_2, language, Collections.emptyList(), false, false);

        Vote vote1 = TestMethods.insertVote(em, user, snip1, true);
        Vote vote2 = TestMethods.insertVote(em, user, snip2, false);

        user.setVotes(Arrays.asList(vote1, vote2));

        Collection<Vote> votes = voteDao.getUserVotes(user.getId());

        Assert.assertEquals(2, votes.size());
    }

    @Test
    public void testGetUserVotesEmpty(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, TestConstants.USER_VERIFIED);

        Collection<Vote> votes = voteDao.getUserVotes(user.getId());

        Assert.assertTrue(votes.isEmpty());
    }

    @Test
    public void testGetVoteBalance(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, TestConstants.USER_VERIFIED);
        User user2 = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL, TestConstants.USER_DATE, TestConstants.LOCALE_ES, TestConstants.USER_VERIFIED);
        User user3 = TestMethods.insertUser(em, TestConstants.USER_USERNAME3, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL3, TestConstants.USER_DATE, TestConstants.LOCALE_ES, TestConstants.USER_VERIFIED);
        Language language = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        Snippet snip1 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, TestConstants.DATE_1, language, Collections.emptyList(), false, false);

        Vote vote1 = TestMethods.insertVote(em, user, snip1, true);
        Vote vote2 = TestMethods.insertVote(em, user2, snip1, false);
        Vote vote3 = TestMethods.insertVote(em, user3, snip1, false);
        snip1.setVotes(Arrays.asList(vote1, vote2, vote3));

        int voteBalance = voteDao.getVoteBalance(snip1.getId());

        Assert.assertEquals(-1, voteBalance);
    }

    @Test
    public void testGetVotePositiveBalance(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, TestConstants.USER_VERIFIED);
        User user2 = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL, TestConstants.USER_DATE, TestConstants.LOCALE_ES, TestConstants.USER_VERIFIED);
        User user3 = TestMethods.insertUser(em, TestConstants.USER_USERNAME3, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL3, TestConstants.USER_DATE, TestConstants.LOCALE_ES, TestConstants.USER_VERIFIED);
        Language language = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        Snippet snip1 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, TestConstants.DATE_1, language, Collections.emptyList(), false, false);

        Vote vote1 = TestMethods.insertVote(em, user, snip1, true);
        Vote vote2 = TestMethods.insertVote(em, user2, snip1, true);
        Vote vote3 = TestMethods.insertVote(em, user3, snip1, true);
        snip1.setVotes(Arrays.asList(vote1, vote2, vote3));

        int voteBalance = voteDao.getVoteBalance(snip1.getId());

        Assert.assertEquals(3, voteBalance);
    }

    @Test
    public void testGetVoteBalanceNoVotes() {
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, TestConstants.USER_VERIFIED);
        Language language = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        Snippet snip1 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, TestConstants.DATE_1, language, Collections.emptyList(), false, false);
        
        Assert.assertEquals(0, voteDao.getVoteBalance(snip1.getId()));
    }

    @Test
    public void testGetVoteBalanceNoneExistingSnippet() {
        Assert.assertEquals(0, voteDao.getVoteBalance(TestConstants.SNIPPET_INVALID_ID));
    }

}


