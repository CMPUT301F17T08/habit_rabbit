package ca.ualberta.cmput301f17t08.habitrabbit;

import android.location.Location;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

    public void removeLocation(Location location){
        locations.remove(location);
    }

}
