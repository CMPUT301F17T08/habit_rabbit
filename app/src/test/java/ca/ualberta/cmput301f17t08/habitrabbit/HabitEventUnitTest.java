package ca.ualberta.cmput301f17t08.habitrabbit;

import android.graphics.Bitmap;
import android.location.Location;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class HabitEventUnitTest {

    String comment1, comment2;
    Bitmap picture1, picture2;
    Location location1, location2;
    Date dateCompleted;
    Habit habit;

    @Before
    public void setUp() {

        ArrayList<Integer> frequency = new ArrayList<Integer>(Collections.nCopies(7, 0));
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        Date startDate = calendar.getTime();

        habit = new Habit("name","reason",startDate, frequency);

        comment1 = "comment 1";
        comment1 = "comment 2";

        // create the two unique images
        picture1 = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        picture2 = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);

        // TODO needs to be completed when we have a proper way to get the location
        location1 = null;
        location2 = null;

    }

    @Test
    public void testHabitEventGetters() throws Exception {



        HabitEvent habitEvent = new HabitEvent(habit.getId(),"Yuxuan",dateCompleted,comment1, location1, picture1);

        // Test getters:
        assertEquals(habit.getId(), habitEvent.getHabitKey());
        assertEquals(dateCompleted,habitEvent.getDateCompleted());
        assertEquals(comment1, habitEvent.getComment());
        assertEquals(picture1, habitEvent.getPicture());
    }

    @Test
    public void testHabitEventSetters() throws Exception {
        HabitEvent habitEvent = new HabitEvent(habit.getId(),"Yuxuan",dateCompleted,comment2, location2, picture2);

        habitEvent.setComment(comment1);
        habitEvent.setLocation(location1);
        habitEvent.setPicture(picture1);
        habitEvent.setDateCompleted(dateCompleted);

        assertEquals(dateCompleted,habitEvent.getDateCompleted());
        assertEquals(comment1, habitEvent.getComment());
        assertEquals(location1, habitEvent.getLocation());
        assertEquals(picture1, habitEvent.getPicture());
//        assertTrue(location1.distanceTo(habitEvent.getLocation()) < 10);

    }
}