package ca.ualberta.cmput301f17t08.habitrabbit;

import android.location.Location;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class HabitEvent {

    private Date dateCompleted;
    private String comment;
    private Location location;
    private File picture;
    private Habit habit;


    public HabitEvent( Habit habit,Date dateCompleted,String comment, Location location, File picture) {
        this.habit = habit;
        this.dateCompleted =dateCompleted;
        this.comment = comment;
        this.location = location;
        this.picture = picture;
    }

    public Habit getHabit(){ return habit;}

    public Date getDateCompleted() {return dateCompleted;}

    public void setDateCompleted(Date dateCompleted) {this.dateCompleted = dateCompleted;}

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
