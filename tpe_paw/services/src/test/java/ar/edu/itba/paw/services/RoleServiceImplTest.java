package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.RoleService;
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

import java.util.Arrays;
import java.util.Collections;

import static ar.edu.itba.paw.interfaces.service.SnippetService.FLAGGED_SNIPPET_REP_VALUE;
import static org.junit.Assert.*;

/*
@RunWith(MockitoJUnitRunner.class)
*/
public class RoleServiceImplTest {

    private static final String ROLE = "ADMIN";
    private static final long USER_ID = 2;

    @InjectMocks
    private RoleService roleService = new RoleServiceImpl();

   /* @Test
    public void testIsAdminTrue(){
        RoleService roleSpyService = Mockito.spy(roleService);
//        Mockito.doReturn(Collections.singletonList(ROLE)).when(roleSpyService).getUserRoles(USER_ID);
        Mockito.doReturn(ROLE).when(roleSpyService).getAdminRoleName();

//        boolean result = roleSpyService.isAdmin(USER_ID);
//
//        assertTrue(result);
    }

    @Test
    public void testIsAdminFalse(){
        RoleService roleSpyService = Mockito.spy(roleService);
//        Mockito.doReturn(Collections.singletonList("NOT ADMIN ROLE")).when(roleSpyService).getUserRoles(USER_ID);
        Mockito.doReturn(ROLE).when(roleSpyService).getAdminRoleName();

//        boolean result = roleSpyService.isAdmin(USER_ID);
//
//        assertFalse(result);
    }*/






}