package ca.ualberta.cmput301f17t08.habitrabbit;

import android.location.Location;

import java.io.File;
import java.io.IOException;

public class HabitEvent {

    private String comment;
    private Location location;
    private File picture;

    public HabitEvent(String comment, Location location, File picture) {
        this.comment = comment;
        this.location = location;
        this.picture = picture;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public void setPicture(File picture) throws IOException {
        this.picture = picture.getCanonicalFile();
    }

    public String getComment(){
        return comment;
    }

    public Location getLocation() {
        return location;
    }

    public File getPicture(){
        return picture;
    }
}
