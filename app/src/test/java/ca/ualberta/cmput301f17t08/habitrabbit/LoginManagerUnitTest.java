package ca.ualberta.cmput301f17t08.habitrabbit;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginManagerUnitTest {

    User user1, user2;
    LoginManager loginManager;

    @Before
    public void setUp() {
        user1 = new User();
        user2 = new User();

        LoginManager = new LoginManager();

    }

    @Test
    public void testLoginManagerCurrentUser() throws Exception {
        loginManager.setCurrentUser(user1);

        assertTrue(loginManager.getCurrentUser() == user1);

        loginManager.setCurrentUser(user2);

        assertTrue(loginManager.getCurrentUser() == user2);
    }

}