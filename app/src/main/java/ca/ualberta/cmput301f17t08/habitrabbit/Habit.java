package ca.ualberta.cmput301f17t08.habitrabbit;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by maharshmellow on 2017-10-23.
 */

public class Habit implements Serializable{
    private String name;
    private String reason;
    private Date startDate;
    private Date lastCompleted;
    private ArrayList<Integer> frequency;
    private int daysCompleted;
    private long averageTime;        // average time of day in milliseconds
    private int streak;
    private ArrayList<HabitEvent> habiteventlist;
    private String id;
    private Boolean synced;

    public Habit(){
        this.habiteventlist = new ArrayList<HabitEvent>();

        this.synced = false;
        this.id = null;
    }

    public Habit(String name, String reason, Date startDate, ArrayList<Integer> frequency){
        this.name = name;
        this.reason = reason;
        this.startDate = startDate;
        this.frequency = frequency;

        this.lastCompleted = null;
        this.daysCompleted = 0;
        this.averageTime = -1;
        this.streak = 0;

        this.habiteventlist = new ArrayList<HabitEvent>();
        this.synced = false;

        this.id = null;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setReason(String reason){
        this.reason = reason;
    }

    public void setDate(Date startDate){
        this.startDate = startDate;
    }

    public void setFrequency(ArrayList<Integer> frequency){
        this.frequency = frequency;
    }

    public void setHabitEvents(ArrayList<HabitEvent> habiteventlist){
        this.habiteventlist = (ArrayList<HabitEvent>)habiteventlist.clone();
    }

    public String getName(){
        return this.name;
    }

    public String getReason(){
        return this.reason;
    }

    public Date getDate(){
        return this.startDate;
    }
  
    public ArrayList<Integer> getFrequency(){
        return this.frequency;
    }

    public ArrayList<HabitEvent> getHabitEvents(){
        return this.habiteventlist;
    }

    // TODO: Separate this into various getters/setters, refactor formatting into calling class.
    // Firebase will not be able to save/retrieve without this.
    public List<Object> getStatistics(){

        // TODO need a way to get the days since start based on the frequency
        int daysSinceStart = 0;

        String averageTimeStr;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Edmonton"));

        if (this.averageTime == -1){
            averageTimeStr = "N/A";
        }else{
            averageTimeStr = sdf.format(this.averageTime);
        }

        System.out.println(this.averageTime + "average" + averageTimeStr);
        List<Object> statistics = new ArrayList<Object>();
        statistics.add(this.daysCompleted);
        statistics.add(this.streak);
        statistics.add(averageTimeStr);

        // % completed
        if (daysSinceStart != 0) {
            statistics.add((float)2/ daysSinceStart);
        }else{
            statistics.add((float)1);      // 100% completed by default
        }

        return statistics;
    }

    public void markDone(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        Date now = calendar.getTime();

        this.daysCompleted += 1;

        // update the average time of completion
        if (this.averageTime != -1){
            this.averageTime = averageTime + (1/this.daysCompleted)*(now.getTime() % 86400000 - averageTime);
        }else{
            this.averageTime = now.getTime() % 86400000;     // milliseconds elapsed until now today
        }

        // update the streak
        /*
        TODO we need to make the streak 0 somewhere if the difference between now and last completed is greater than 1 day
        TODO we can do this whenever the activity is loaded or right after login
        Note: we don't need to worry about this function being called multiple times in one day since
        the habit will disappear from the today page
         */

        if (this.lastCompleted != null && now.getTime() - this.lastCompleted.getTime() < 86400000){
            this.streak += 1;
        }else if (lastCompleted == null) {
            this.streak = 1;
        }


        this.lastCompleted = now;

        // TODO create habit event here and jump to the add to habit history activity

    }

    public void addHabitEvent(HabitEvent habitevent) {
        if (hasHabitEvent(habitevent))
            throw new IllegalArgumentException("HabitEvent already exists.");

        this.habiteventlist.add(habitevent);
        return;
    }

    private boolean hasHabitEvent(HabitEvent habitevent) {
        return this.habiteventlist.contains(habitevent);
    }

    public ArrayList<HabitEvent> filterHistoryByComment(String keyword) {

        ArrayList<HabitEvent> result = new ArrayList<>();

        for (HabitEvent habitevent : habiteventlist) {
            if (habitevent.getComment().contains(keyword)) {
                result.add(habitevent);
            }
        }

        return result;
    }

    public void removeHabitEvent(HabitEvent event){
        if (this.habiteventlist.contains(event)){
            this.habiteventlist.remove(event);
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
        DatabaseManager.getInstance().saveHabit(this, listener);
    }

    public void delete() {
        // TODO: destroy habit from DB (call DB manager)
    }
}
