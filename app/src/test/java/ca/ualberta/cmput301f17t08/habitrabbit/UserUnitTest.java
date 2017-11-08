package ca.ualberta.cmput301f17t08.habitrabbit;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Map;

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

        int frequency[] = {0,1,0,1,0,1,0};
        habit1 = new Habit("Name 1", "Reason 1", new Date(), frequency);
        habit2 = new Habit("Name 2", "Reason 2", new Date(), frequency);
        habit3 = new Habit("Name 3", "Reason 3", new Date(), frequency);
        habit4 = new Habit("Name 4", "Reason 4", new Date(), frequency);

    }

    @Test
    public void testUserAddFollower() throws Exception {
        assertFalse(user1.getFollowers().contains(user2));

        user1.addFollower(user2);

        assertTrue(user1.getFollowers().contains(user2));
    }

    @Test
    public void testUserRemoveFollower() throws Exception {
        user1.addFollower(user2);

        assertTrue(user1.getFollowers().contains(user2));

        user1.removeFollower(user2);

        assertFalse(user1.getFollowers().contains(user2));
    }

    @Test
    public void testUserAddHabit() throws Exception {
        assertFalse(user1.getHabits().contains(habit1));

        user1.addHabit(habit1);

        assertTrue(user1.getHabits().contains(habit1));
    }

    @Test
    public void testUserRemoveHabit() throws Exception {
        user1.addHabit(habit1);

        assertTrue(user1.getHabits().contains(habit1));

        user1.removeHabit(habit1);

        assertFalse(user1.getHabits().contains(habit1));
    }

    @Test
    public void testUserFilterHabitbytype() throws Exception {
        user1.addHabit(habit1);
        user1.addHabit(habit2);
        user1.addHabit(habit3);
        user1.addHabit(habit4);

        user2.addHabit(habit4);

        assertTrue(user1.filterHistoryBytype("Name 2").contains(habit2));
        assertTrue(user1.filterHistoryBytype("Name 3").contains(habit3));
        assertTrue(user1.filterHistoryBytype("Name 4").contains(habit4));

        assertFalse(user1.filterHistoryBytype("Name 4").contains(habit1));


        assertFalse(user2.filterHistoryBytype("Name 4").contains(habit1));

    }


    @Test
    public void testUserFilterHabiteventbycomment() throws Exception {

    }

}