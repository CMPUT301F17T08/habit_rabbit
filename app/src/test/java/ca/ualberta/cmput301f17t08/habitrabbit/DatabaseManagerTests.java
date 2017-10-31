package ca.ualberta.cmput301f17t08.habitrabbit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DatabaseManagerTests {
    private DatabaseManager databaseManager;

    @Before
    public void setUp() throws Exception {
        databaseManager = DatabaseManager.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        //
    }

    @Test
    public void testAddUser() throws Exception {
        databaseManager.createUser("test user", new DatabaseManager.OnUserCreatedListener() {
            @Override
            public void onUserCreated(User user) {
                Assert.assertTrue(user != null);
            }
        });
    }
}
