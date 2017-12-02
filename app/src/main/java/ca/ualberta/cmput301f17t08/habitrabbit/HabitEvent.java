package ca.ualberta.cmput301f17t08.habitrabbit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Base64;

import com.google.firebase.database.Exclude;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

/**
 * The class for habit event, it has date and other properties for habit event
 */

public class HabitEvent implements Serializable {

    private String habitKey;
    private Habit habit;
    private Date dateCompleted;
    private String comment;
    private Location location;
    private String picture;
    private ArrayList<String> likes;
    private String username;
    private String id;
    private Boolean synced;

    public HabitEvent(){
        this.likes = new ArrayList<String>();

        this.synced = false;
        this.id = null;
    }

    public HabitEvent(String habitKey, String username, Date dateCompleted, String comment, Location location, Bitmap picture) {
        this.habitKey = habitKey;
        this.username = username;
        this.dateCompleted = dateCompleted;
        this.comment = comment;
        this.location = location;
        this.picture = bitmapToString(picture);
        this.likes = new ArrayList<String>();

        this.synced = false;
        this.id = null;
    }

    public void setHabitKey(String habitKey) {
        this.habitKey = habitKey;
    }

    public String getHabitKey(){
        return habitKey;
    }

    @Exclude
    public void getHabit(DatabaseManager.OnHabitsListener listener){
        HashSet<String> habit = new HashSet<String>();
        habit.add(this.habitKey);

        DatabaseManager.getInstance().getHabitsInSet(habit, listener);
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getSynced() {
        return synced;
    }

    public void setSynced(Boolean synced) {
        this.synced = synced;
    }

    public void sync(DatabaseManager.OnSaveListener listener){
        DatabaseManager.getInstance().saveHabitEvent(this, listener);
    }

    public void delete() {
        // TODO: destroy habit from DB (call DB manager)
    }
}
