package ar.edu.itba.paw.services;

import static org.junit.Assert.assertTrue;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UserServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest
{
    private static final String PASSWORD = "passwordpassword";
    private static final String USERNAME = "username";
    private static final String EMAIL = "email@email.com";
    private static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


    @InjectMocks
    private UserServiceImpl userService = new UserServiceImpl();
    @Mock
    private UserDao mockDao;

    @Test
    public void testCreate() {
        // 1. Setup!
        Mockito.when(mockDao.createUser(
                Mockito.eq(USERNAME),
                Mockito.eq(PASSWORD),
                Mockito.eq(EMAIL),
                Mockito.eq(""),
                Mockito.eq(0),
                Mockito.eq(DATE.format(Calendar.getInstance().getTime().getTime()))))
                .thenReturn((new User(1, USERNAME, PASSWORD, EMAIL, "", 0, DATE.format(Calendar.getInstance().getTime().getTime()), null)).getId());

        // 2. "ejercito" la class under test
        long user = userService.createUser(USERNAME, PASSWORD, EMAIL, "", 0, DATE.format(Calendar.getInstance().getTime().getTime()));

        // 3. Asserts!
//        Assert.assertNotNull(user);
//        Assert.assertEquals(USERNAME, user.getUsername());
//        Assert.assertEquals(PASSWORD, user.getPassword());
//        Assert.assertEquals(EMAIL, user.getEmail());
//        Assert.assertEquals(DATE, user.getDateJoined());

    }
}
