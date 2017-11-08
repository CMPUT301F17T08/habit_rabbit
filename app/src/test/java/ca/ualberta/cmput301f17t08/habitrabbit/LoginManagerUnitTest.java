package ca.ualberta.cmput301f17t08.habitrabbit;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginManagerUnitTest {

    User user1, user2;
    LoginManager loginManager;

    @Before
    public void setUp() {
        DatabaseManager.getInstance().createUser("Test User 1", new DatabaseManager.OnUserDataListener() {
            @Override
            public void onUserData(User user) {
                user1 = user;
            }

            @Override
            public void onUserDataFailed(String message) {

            }
        });

        DatabaseManager.getInstance().createUser("Test User 2", new DatabaseManager.OnUserDataListener() {
            @Override
            public void onUserData(User user) {
                user1 = user;
            }

            @Override
            public void onUserDataFailed(String message) {

            }
        });

        LoginManager loginManager =  LoginManager.getInstance();

    }

    @Test
    public void testLoginManagerCurrentUser() throws Exception {
        loginManager.login("Test User 1", new LoginManager.OnLoginCompleteListener() {
            @Override
            public void onLoginComplete() {

            }

            @Override
            public void onLoginFailed(String message) {

            }
        });

        assertTrue(loginManager.getCurrentUser() == user1);

        loginManager.login("Test User 2", new LoginManager.OnLoginCompleteListener() {
            @Override
            public void onLoginComplete() {

            }

            @Override
            public void onLoginFailed(String message) {

            }
        });

        assertTrue(loginManager.getCurrentUser() == user2);
    }

}