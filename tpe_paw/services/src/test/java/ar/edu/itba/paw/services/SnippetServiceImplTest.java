package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.interfaces.service.VoteService;
import ar.edu.itba.paw.models.Snippet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;

import static ar.edu.itba.paw.interfaces.service.SnippetService.FLAGGED_SNIPPET_REP_VALUE;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class SnippetServiceImplTest {

    private static final long SNIPPET_ID = 1;
    private static final long USER_ID = 2;
    private static final Integer VOTES = 30;
    public static final String TITLE = "Snippet Title";
    public static final String DESCR = "Description";
    public static final String CODE = "Snippet Code";

    @InjectMocks
    private SnippetService snippetService = new SnippetServiceImpl();

    @Mock
    private VoteService mockVoteService;
    @Mock
    private UserService mockUserService;
    @Mock
    private SnippetDao mockSnippetDao;

    private Snippet defaultSnippet = new Snippet(SNIPPET_ID,null,TITLE,DESCR,null,null,null,null,VOTES,false);

    @Test
    public void testGetReputationImportanceBalance(){
        Mockito.when(mockVoteService.getVoteBalance(SNIPPET_ID)).thenReturn(java.util.Optional.of(VOTES));
        SnippetService snippetSpyService = Mockito.spy(snippetService);
        Mockito.doReturn(false).when(defaultSnippet.isFlagged());

        int result = (defaultSnippet.getVoteCount() * -1) + (defaultSnippet.isFlagged() ? FLAGGED_SNIPPET_REP_VALUE : 0);

        assertEquals(VOTES*-1,result);
    }

    @Test
    public void testGetReputationImportanceBalanceFlagged(){
        Mockito.when(mockVoteService.getVoteBalance(SNIPPET_ID)).thenReturn(java.util.Optional.of(VOTES));
        SnippetService snippetSpyService = Mockito.spy(snippetService);
        Mockito.doReturn(true).when(defaultSnippet.isFlagged());

        int result = (defaultSnippet.getVoteCount() * -1) + (defaultSnippet.isFlagged() ? FLAGGED_SNIPPET_REP_VALUE : 0);
        assertEquals((VOTES*-1)+FLAGGED_SNIPPET_REP_VALUE,result);
    }





}
