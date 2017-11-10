package ca.ualberta.cmput301f17t08.habitrabbit;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HabitUnitTest {
    ArrayList<Integer> frequency = new ArrayList<Integer>(Arrays.asList(new Integer[]{1,0,1,0,1,0,1}));
    ArrayList<Integer> frequency2 = new ArrayList<Integer>(Arrays.asList(new Integer[]{0,1,0,1,0,1,0}));


    @Test
    public void testHabitGetters() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        Date now = calendar.getTime();

        Habit habit = new Habit("Name", "Reason", now, frequency);

        // Test Habit getters:
        assertEquals("Name", habit.getName());
        assertEquals("Reason", habit.getReason());
        assertEquals(now, habit.getDate());
        assertTrue(frequency.equals(habit.getFrequency()));
    }

    @Test
    public void testHabitSetters() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        Date now = calendar.getTime();

        Habit habit = new Habit("Name", "Reason", now, frequency);

        habit.setName("New Name");
        assertEquals("New Name", habit.getName());

        habit.setReason("New Reason");
        assertEquals("New Reason", habit.getReason());

        habit.setDate(new Date(1234567));
        assertEquals(new Date(1234567), habit.getDate());

        habit.setFrequency(frequency2);
        assertTrue(frequency2.equals(habit.getFrequency()));
    }

    @Test
    public void testHabitStats() throws Exception{
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        Date now = calendar.getTime();

        Habit habit = new Habit("Name", "Reason", now, frequency);
        int daysCompleted1 = (int) habit.getStatistics().get(0);
        assertEquals(daysCompleted1, 0);

        habit.markDone();

        int daysCompleted2 = (int) habit.getStatistics().get(0);
        assertEquals(daysCompleted2, 1);

    }
}