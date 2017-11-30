package ca.ualberta.cmput301f17t08.habitrabbit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
/**
 * The class for habit event, it has date and other properties for habit event
 */

public class HabitEvent implements Serializable {

    private Date dateCompleted;
    private String comment;
    private Location location;
    private String picture;
    private Habit habit;
    private ArrayList<String> likes;
    private String username;

    public HabitEvent(Habit habit, String username, Date dateCompleted, String comment, Location location, Bitmap picture) {
        this.habit = habit;
        this.username = username;
        this.dateCompleted = dateCompleted;
        this.comment = comment;
        this.location = location;
        this.picture = bitmapToString(picture);
        this.likes = new ArrayList();
    }

    public Habit getHabit(){ return habit;}

    public String getUsername(){ return username;}

    public Date getDateCompleted() {return dateCompleted;}

    public void setDateCompleted(Date dateCompleted) {this.dateCompleted = dateCompleted;}

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public void setPicture(Bitmap picture) {
        this.picture = bitmapToString(picture);
    }

    public String getComment(){
        return comment;
    }

    public Location getLocation() {
        return location;
    }

    public Bitmap getPicture(){
        return stringToBitmap(this.picture);
    }

    public void like(String username){
        if (!this.likes.contains(username)){
            this.likes.add(username);
        }else{
            this.likes.remove(username);
        }
    }
    public ArrayList<String> getLikes(){
        return likes;
    }

    public int getLikeCount(){
        return this.likes.size();
    }

    private String bitmapToString(Bitmap picture) {

        // https://goo.gl/fuKG3n
        // convert the bitmap object to a string because bitmap isn't serializable (causes problems
        // with intents)
        try {
            ByteArrayOutputStream ByteStream = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.PNG, 100, ByteStream);
            byte[] b = ByteStream.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        }catch(Exception e){
            return null;
        }
    }

    private Bitmap stringToBitmap(String picture){
        try{
            byte [] encodeByte=Base64.decode(picture,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;

        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
