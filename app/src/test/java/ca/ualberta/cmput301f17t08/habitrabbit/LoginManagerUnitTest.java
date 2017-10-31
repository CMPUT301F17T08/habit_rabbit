package ca.ualberta.cmput301f17t08.habitrabbit;

import android.provider.ContactsContract;

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
        DatabaseManager.getInstance().createUser("Test User 1", new DatabaseManager.OnUserCreatedListener() {
            @Override
            public void onUserCreated(User user) {
                user1 = user;
            }
        });

        DatabaseManager.getInstance().createUser("Test User 2", new DatabaseManager.OnUserCreatedListener() {
            @Override
            public void onUserCreated(User user) {
                user1 = user;
            }
        });

        LoginManager loginManager = new LoginManager();

    }

    @Test
    public void testLoginManagerCurrentUser() throws Exception {
        loginManager.login("Test User 1");

        assertTrue(loginManager.getCurrentUser() == user1);

        loginManager.login("Test User 2");

        assertTrue(loginManager.getCurrentUser() == user2);
    }

}