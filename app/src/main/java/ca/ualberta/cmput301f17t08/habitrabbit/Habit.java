package ca.ualberta.cmput301f17t08.habitrabbit;

import android.util.ArrayMap;
import android.util.Log;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * The class for a habit, has all the properties for a habit
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
    private HashSet<String> habitEventKeyList;
    private HashMap<String, HabitEvent> habiteventlist;
    private String id;
    private Boolean synced;
    private Boolean habitEventsLoaded;


    public Habit(){
        this.habitEventKeyList = new HashSet<String>();
        this.habiteventlist = new HashMap<String, HabitEvent>();

        this.synced = false;
        this.id = null;
        this.habitEventsLoaded = false;
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

        this.habitEventKeyList = new HashSet<String>();
        this.habiteventlist = new HashMap<String, HabitEvent>();
        this.synced = false;

        this.id = null;
        this.habitEventsLoaded = false;
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

    public void setHabitEvents(HashMap<String, HabitEvent> habiteventlist){
        this.habiteventlist = habiteventlist;
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

    public ArrayList<String> getHabitEventKeys(){
        if(habitEventsLoaded){
            return new ArrayList<String>(this.habiteventlist.keySet());
        }else{
            return new ArrayList<String>(this.habitEventKeyList);
        }
    }

    public void setHabitEventKeys(ArrayList<String> habits){
        habitEventKeyList = new HashSet<String>(habits);
        habitEventsLoaded = false;
        habiteventlist.clear();
    }

    @Exclude
    public void getHabitEvents(final DatabaseManager.OnHabitEventsListener listener){
        if(this.habitEventsLoaded){
            listener.onHabitEventsSuccess(habiteventlist);
            return;
        }

        DatabaseManager.getInstance().getHabitEventsInSet(this.habitEventKeyList, new DatabaseManager.OnHabitEventsListener() {
            @Override
            public void onHabitEventsSuccess(HashMap<String, HabitEvent> habitEvents) {
                habiteventlist = habitEvents;
                habitEventsLoaded = true;
                listener.onHabitEventsSuccess(habitEvents);
            }

            @Override
            public void onHabitEventsFailed(String message) {
                listener.onHabitEventsFailed(message);
            }
        });
    }

    public void resetStreak(){ this.streak = 0; }

    public Date getLastCompleted() { return this.lastCompleted; }

    public int getDaysCompleted(){
        return this.daysCompleted;
    }

    public int getStreak(){
        return this.streak;
    }

    public long getAverageTime(){
        return this.averageTime;
    }

    public String getAverageTimeStr(){
        String averageTimeStr;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        long averageTime = getAverageTime();

        if (averageTime <= 0){
            averageTimeStr = "N/A";
        }else{
            averageTimeStr = sdf.format(averageTime);
        }

        return averageTimeStr;
    }

    public float getPercentCompleted(){
        float percentComplete;

        // count the total days since the start that the user was supposed to complete this habit
        int daysSinceStart = 0;

        Calendar currentCalendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.MILLISECOND, 0);
        currentCalendar.add(Calendar.DATE, 1);


        System.out.println("Current Date" + currentCalendar.getTime());

        Calendar tempCalendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));

        tempCalendar.setTime(this.startDate);
        tempCalendar.set(Calendar.HOUR_OF_DAY, 0);
        tempCalendar.set(Calendar.MINUTE, 0);
        tempCalendar.set(Calendar.SECOND, 0);
        tempCalendar.set(Calendar.MILLISECOND, 0);

        while (tempCalendar.getTime().before(currentCalendar.getTime())){

            int tempDayIndex = tempCalendar.get(Calendar.DAY_OF_WEEK);

            // converts between the built in day index to the frequency array indices
            int [] conversion_table = {0, 6, 0, 1, 2, 3, 4, 5};

            // check if the user is following the habit on this day
            if (frequency.get(conversion_table[tempDayIndex]) == 1){
                daysSinceStart += 1;
            }

            tempCalendar.add(Calendar.DATE, 1);

        }

        System.out.println(daysCompleted);
        System.out.println(daysSinceStart);
        System.out.println(this.startDate);
        System.out.println(this.lastCompleted);

        // % completed
        if (daysSinceStart != 0) {
            percentComplete = (float) daysCompleted / daysSinceStart;
        }else{
            percentComplete = (float)0;      // 100% completed by default
        }

        return (float) Math.min(1.0,percentComplete);
    }

    public void markDone(){

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        Date now = calendar.getTime();

        this.daysCompleted += 1;

        // update the average time of completion
        if (this.averageTime > 0){
            this.averageTime = (long) (this.averageTime + ((float)1/this.daysCompleted)*(now.getTime() % 86400000 - this.averageTime));
        }else{
            this.averageTime = now.getTime() % 86400000;     // milliseconds elapsed until now today
        }

        // update the streak
        /*
        Note: we don't need to worry about this function being called multiple times in one day since
        the habit will disappear from the today page
         */

        // TODO need to check if there was a day between lastCompleted and now that the habit was supposed to be completed


        if (this.lastCompleted != null){
            this.streak += 1;

        }else if (lastCompleted == null) {
            this.streak = 1;
        }

        this.lastCompleted = now;

    }

    public void addHabitEvent(final HabitEvent habitEvent, final DatabaseManager.OnSaveListener listener) {
        if (hasHabitEvent(habitEvent))
            throw new IllegalArgumentException("HabitEvent already exists.");

        final Habit self = this;

        if(habitEvent.getSynced()){
            this.sync(new DatabaseManager.OnSaveListener() {
                @Override
                public void onSaveSuccess() {
                    self.habiteventlist.put(habitEvent.getId(), habitEvent);
                    self.sync(listener); // Sync final list
                }

                @Override
                public void onSaveFailure(String message) {
                    listener.onSaveFailure(message);
                }
            }); // Sync initial list

        }else{

            habitEvent.sync(new DatabaseManager.OnSaveListener() {
                @Override
                public void onSaveSuccess() {
                    self.sync(new DatabaseManager.OnSaveListener() {
                        @Override
                        public void onSaveSuccess() {
                            self.habiteventlist.put(habitEvent.getId(), habitEvent);
                            self.sync(listener); // Sync final HabitEvents list
                        }

                        @Override
                        public void onSaveFailure(String message) {
                            listener.onSaveFailure(message);
                        }
                    }); // Sync initial HabitEvents list
                }

                @Override
                public void onSaveFailure(String message) {
                    listener.onSaveFailure(message);
                }
            });
        }
    }

    private boolean hasHabitEvent(HabitEvent habitevent) {
        return this.habiteventlist.containsKey(habitevent.getId());
    }

    public ArrayList<HabitEvent> filterHistoryByComment(String keyword) {

        ArrayList<HabitEvent> result = new ArrayList<>();

        for (HabitEvent habitevent : habiteventlist.values()) {
            if (habitevent.getComment().contains(keyword)) {
                result.add(habitevent);
            }
        }

        return result;
    }

    public void removeHabitEvent(HabitEvent event){
        // TODO: save this
        if (this.habiteventlist.containsKey(event.getId())){
            this.habiteventlist.remove(event.getId());
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

    public void sync(final DatabaseManager.OnSaveListener listener){

        final Habit self = this;

        DatabaseManager.getInstance().saveHabit(this, new DatabaseManager.OnSaveListener() {
            @Override
            public void onSaveSuccess() {
                self.getHabitEvents(new DatabaseManager.OnHabitEventsListener() {
                    @Override
                    public void onHabitEventsSuccess(HashMap<String, HabitEvent> habitEvents) {
                        listener.onSaveSuccess();
                    }

                    @Override
                    public void onHabitEventsFailed(String message) {
                        listener.onSaveFailure(message);
                    }
                });
            }

            @Override
            public void onSaveFailure(String message) {
                listener.onSaveFailure(message);
            }
        });
    }

    public void delete() {
        // TODO: destroy habit from DB (call DB manager)
    }
}
