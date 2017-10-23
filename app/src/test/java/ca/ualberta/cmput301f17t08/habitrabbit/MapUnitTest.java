package ca.ualberta.cmput301f17t08.habitrabbit;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MapUnitTest {

    Location location1, location2;
    Map map;

    @Before
    public void setUp() {

        location1 = new Location("");
        location2 = new Location("");

        location1.setLatitude(10);
        location1.setLongitude(10);

        location2.setLatitude(20);
        location2.setLongitude(20);

        map = new Map();
    }

    @Test
    public void testMapAddLocation() throws Exception {
        assertFalse(map.getLocations().contains(location1));
        assertFalse(map.getLocations().contains(location2));

        map.addLocation(location1);

        assertTrue(map.getLocations().contains(location1));
        assertFalse(map.getLocations().contains(location2));

        map.addLocation(location2);

        assertTrue(map.getLocations().contains(location1));
        assertTrue(map.getLocations().contains(location2));
    }

    @Test
    public void testMapRemoveLocation() throws Exception {

        map.addLocation(location1);
        map.addLocation(location2);

        assertTrue(map.getLocations().contains(location1));
        assertTrue(map.getLocations().contains(location2));

        map.removeLocation(location1);

        assertFalse(map.getLocations().contains(location1));
        assertTrue(map.getLocations().contains(location2));

        map.removeLocation(location2);

        assertFalse(map.getLocations().contains(location1));
        assertFalse(map.getLocations().contains(location2));
    }

}