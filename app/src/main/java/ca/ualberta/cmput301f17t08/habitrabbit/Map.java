package ca.ualberta.cmput301f17t08.habitrabbit;

import android.location.Location;

import java.lang.reflect.Array;
import java.util.ArrayList;
/**
 * The class map, it could add, remove, and get location
 */
public class Map {

    private ArrayList<Location> locations;

    public Map(){
        locations = new ArrayList<Location>();
    }

    public void addLocation(Location location){
        locations.add(location);
    }

    public ArrayList<Location> getLocations(){
        return locations;
    }
    public Location getLocation(){
        return null;
    }

    public void removeLocation(Location location){
        locations.remove(location);
    }

}
