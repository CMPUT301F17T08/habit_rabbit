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
    private String picture;
    private ArrayList<String> likes;
    private String username;
    private String id;
    private Boolean synced;
    private double lat ;
    private double lng ;

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
        this.picture = bitmapToString(picture);
        this.likes = new ArrayList<String>();

        if(location != null) {
            this.lat = location.getLatitude();
            this.lng = location.getLongitude();
        }else{
            this.lat = -1;
            this.lng = -1;
        }

        this.synced = false;
        this.id = null;
        if(!Double.isNaN(lat) && !Double.isNaN(lng)) {
            this.lat = lat;
            this.lng = lng;
        }
    }


    /**
     * set the habitkey for each habit
     * @param habitKey the key for each habit
     */
    public void setHabitKey(String habitKey) {
        this.habitKey = habitKey;
    }

    /**
     * get the habit key from habit
     * @return the habit key
     */
    public String getHabitKey(){
        return habitKey;
    }


    @Exclude
    public void getHabit(DatabaseManager.OnHabitsListener listener){
        HashSet<String> habit = new HashSet<String>();
        habit.add(this.habitKey);

        DatabaseManager.getInstance().getHabitsInSet(habit, listener);
    }

    /**
     * get the username correpsonding to the habit
     * @return the username as a string
     */
    public String getUsername(){ return username;}

    /**
     * get the complete date of the habitevent
     * @return the complete date of the habitevent as Date object
     */
    public Date getDateCompleted() {return dateCompleted;}

    /**
     * set the complete date for the habit
     * @param dateCompleted the date object represents the date completes
     */
    public void setDateCompleted(Date dateCompleted) {this.dateCompleted = dateCompleted;}

    /**
     * set the comment for the habitevent
     * @param comment string comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * set the location for the habitevent
     * @param location location object
     */
    public void setLocation(Location location) {
        this.lat = location.getLatitude();
        this.lng = location.getLongitude();
    }

    /**
     * set the picture for habitevent
     * @param picture bitmap file
     */
    public void setPicture(Bitmap picture) {
        this.picture = bitmapToString(picture);
    }

    /**
     * set the string for picture
     * @param picture
     */
    public void setPictureString(String picture){
        this.picture = picture;
    }

    /**
     * get the comment of habitevent
     * @return the string comment
     */
    public String getComment(){
        return comment;
    }

    @Exclude
    public Location getLocation() {
        Location l = new Location("");
        l.setLatitude(this.lat);
        l.setLongitude(this.lng);
        return l;
    }

    /**
     *  For Firebase serialization
      */
    public ArrayList<Double> getLocationPair() {
        ArrayList<Double> locationPair = new ArrayList<Double>();

        locationPair.add(this.lat);
        locationPair.add(this.lng);

        return locationPair;

    }

    /**
     * set the location
     * @param locationPair
     */
    public void setLocationPair(ArrayList<Double> locationPair){
        this.lat = locationPair.get(0);
        this.lng = locationPair.get(1);
    }

    @Exclude
    public Bitmap getPicture(){
        return stringToBitmap(this.picture);
    }

    public String getPictureString() {
        return this.picture;
    }

    public void like(String username){
        if (!this.likes.contains(username)){
            this.likes.add(username);
        }else{
            this.likes.remove(username);
        }
    }

    /**
     * get the collection of usernames that represents the users used to like it
     * @return arraylist likes
     */
    public ArrayList<String> getLikes(){
        return likes;
    }

    /**
     * get the number of likes
     * @return the number of likes
     */
    public int getLikeCount(){
        return this.likes.size();
    }

    /**
     * conver the picture to string to store in the database
     * @param picture
     * @return
     */
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

    /**
     * get the id
      * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * set the id
     * @param id the id of habitevent
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * get the status of synchronization
     * @return boolean if it has synced
     */
    public Boolean getSynced() {
        return synced;
    }

    /**
     * set the status of synchronization
     * @param synced
     */
    public void setSynced(Boolean synced) {
        this.synced = synced;
    }

    /**
     * check if it has synchronized properly from db
     * @param listener get from the db
     */
    public void sync(DatabaseManager.OnSaveListener listener){
        DatabaseManager.getInstance().saveHabitEvent(this, listener);
    }

    public void delete() {
        DatabaseManager.getInstance().deleteHabitEvent(this);
    }
}
