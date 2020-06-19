package ar.edu.itba.paw.services;
import java.time.Instant;
import java.util.Locale;

import ar.edu.itba.paw.interfaces.service.RoleService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.User;

@RunWith (MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email@gmail.com";
    private static final Locale LOCALE = Locale.ENGLISH;
    private static final Instant DATE = Instant.now();

    @InjectMocks
    private UserServiceImpl userService = new UserServiceImpl();

    @Mock
    private UserDao mockUserDao;

    @Mock
    private RoleService mockRoleService;

    @Test
    public void testCreateUser() {
        // 1. Setup!
        Mockito.when(mockUserDao.createUser(Mockito.eq(USERNAME), Mockito.eq(PASSWORD), Mockito.eq(EMAIL), Mockito.eq(DATE), Mockito.eq(LOCALE))).thenReturn(new User(1L, USERNAME, PASSWORD, EMAIL, DATE, LOCALE, false));
        Mockito.when(mockRoleService.assignUserRole(Mockito.eq(1L))).thenReturn(true);

        // 2. Call
        User user = userService.register(USERNAME, PASSWORD, EMAIL, DATE, LOCALE);

        // 3. Asserts!
        Assert.assertNotNull(user);
        Assert.assertEquals(USERNAME, user.getUsername());
        Assert.assertEquals(PASSWORD, user.getPassword());
    }
}
