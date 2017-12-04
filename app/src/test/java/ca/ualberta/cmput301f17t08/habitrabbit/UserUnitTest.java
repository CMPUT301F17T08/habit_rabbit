package ca.ualberta.cmput301f17t08.habitrabbit;

import android.location.Location;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.xml.transform.Source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserUnitTest {

    User user1, user2;
    Habit habit1, habit2, habit3, habit4;

    @Before
    public void setUp() {

        user1 = new User("Test User 1");
        user2 = new User("Test User 2");

        ArrayList<Integer> frequency = new ArrayList<Integer>(Arrays.asList(new Integer[]{1,0,1,0,1,0,1}));
        habit1 = new Habit("Name 1", "Reason 1", new Date(), frequency);
        habit2 = new Habit("Name 2", "Reason 2", new Date(), frequency);
        habit3 = new Habit("Name 3", "Reason 3", new Date(), frequency);
        habit4 = new Habit("Name 4", "Reason 4", new Date(), frequency);

    }

    @Test
    public void testUserAddFollower() throws Exception {
        assertFalse(user1.getFollowers().contains(user2.getUsername()));

        user1.addFollower(user2);
        assertTrue(user1.getFollowers().contains(user2.getUsername()));
    }

    @Test
    public void testUserRemoveFollower() throws Exception {
        user1.addFollower(user2);
        assertTrue(user1.getFollowers().contains(user2.getUsername()));

        user1.removeFollower(user2);
        assertFalse(user1.getFollowers().contains(user2.getUsername()));
    }

    @Test
    public void testUserFilterHabitEventByComment() throws Exception {

    }

}