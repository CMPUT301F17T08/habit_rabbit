package ca.ualberta.cmput301f17t08.habitrabbit;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HabitUnitTest {

    int frequency[] = {1,0,1,0,1,0,1};
    int frequency2[] = {0,1,0,1,0,1,0};

    @Test
    public void testHabitGetters() throws Exception {
        Date date = new Date();
        Habit habit = new Habit("Name", "Reason", date, frequency);

        // Test Habit getters:
        assertEquals("Name", habit.getName());
        assertEquals("Reason", habit.getReason());
        assertEquals(date, habit.getDate());
        assertTrue(Arrays.equals(frequency, habit.getFrequency()));
    }

    @Test
    public void testHabitSetters() throws Exception {
        Date date = new Date();
        Habit habit = new Habit("Name", "Reason", date, frequency);

        habit.setName("New Name");
        assertEquals("New Name", habit.getName());

        habit.setReason("New Reason");
        assertEquals("New Reason", habit.getReason());

        habit.setDate(new Date(1234567));
        assertEquals(new Date(1234567), habit.getDate());

        habit.setFrequency(frequency2);
        assertTrue(Arrays.equals(frequency2, habit.getFrequency()));
    }
}