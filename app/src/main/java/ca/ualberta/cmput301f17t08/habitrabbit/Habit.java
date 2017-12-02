package ca.ualberta.cmput301f17t08.habitrabbit;

import android.util.ArrayMap;

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
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

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
    private ArrayMap<String, HabitEvent> habiteventlist;
    private String id;
    private Boolean synced;
    private Boolean habitEventsLoaded;

    public Habit(){
        this.habitEventKeyList = new HashSet<String>();
        this.habiteventlist = new ArrayMap<String, HabitEvent>();

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
        this.habiteventlist = new ArrayMap<String, HabitEvent>();
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

    public void setHabitEvents(ArrayMap<String, HabitEvent> habiteventlist){
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
            public void onHabitEventsSuccess(ArrayMap<String, HabitEvent> habitEvents) {
                habitEvents = habitEvents;
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

    // TODO: Separate this into various getters/setters, refactor formatting into calling class.
    // Firebase will not be able to save/retrieve without this.
    public List<Object> getStatistics(){
        
        // count the total days since the start that the user was supposed to complete this habit
        int daysSinceStart = 0;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        Date currentDate = calendar.getTime();
        Date tempDate = this.startDate;

        System.out.println("Start: " + startDate);
        while (tempDate.before(currentDate)){
            calendar.setTime(tempDate);
            int tempDayIndex = calendar.get(Calendar.DAY_OF_WEEK);

            // converts between the built in day index to the frequency array indices
            int [] conversion_table = {0, 6, 0, 1, 2, 3, 4, 5};

            // check if the user is following the habit on this day
            if (frequency.get(conversion_table[tempDayIndex]) == 1){
                daysSinceStart += 1;
            }

            // increment the temp date by 1 day
            calendar.add(Calendar.DATE, 1);
            tempDate = calendar.getTime();

        }

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
            statistics.add((float) daysCompleted / daysSinceStart);
        }else{
            statistics.add((float)0);      // 100% completed by default
        }

        return statistics;
    }

    public void markDone(){

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        Date now = calendar.getTime();

        this.daysCompleted += 1;

        // update the average time of completion
        if (this.averageTime != -1){
            this.averageTime = (long) (this.averageTime + ((float)1/this.daysCompleted)*(now.getTime() % 86400000 - this.averageTime));
        }else{
            this.averageTime = now.getTime() % 86400000;     // milliseconds elapsed until now today
        }

        // update the streak
        /*
        Note: we don't need to worry about this function being called multiple times in one day since
        the habit will disappear from the today page
         */

        if (this.lastCompleted != null && now.getTime() - this.lastCompleted.getTime() < 86400000){
            this.streak += 1;
        }else if (lastCompleted == null) {
            this.streak = 1;
        }

        System.out.println("Updated last completed to" + now);
        this.lastCompleted = now;

    }

    public void addHabitEvent(final HabitEvent habitEvent, final DatabaseManager.OnSaveListener listener) {
        if (hasHabitEvent(habitEvent))
            throw new IllegalArgumentException("HabitEvent already exists.");

        if(habitEvent.getSynced()){
            this.habiteventlist.put(habitEvent.getId(), habitEvent);
            this.sync(new DatabaseManager.OnSaveListener() {
                @Override
                public void onSaveSuccess() {
                    listener.onSaveSuccess();
                }

                @Override
                public void onSaveFailure(String message) {
                    listener.onSaveFailure(message);
                }
            });
        }else{
            final Habit self = this;

            habitEvent.sync(new DatabaseManager.OnSaveListener() {
                @Override
                public void onSaveSuccess() {
                    self.habiteventlist.put(habitEvent.getId(), habitEvent);
                    self.sync(new DatabaseManager.OnSaveListener() {
                        @Override
                        public void onSaveSuccess() {
                            listener.onSaveSuccess();
                        }

                        @Override
                        public void onSaveFailure(String message) {
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

    public void sync(DatabaseManager.OnSaveListener listener){
        DatabaseManager.getInstance().saveHabit(this, listener);
    }

    public void delete() {
        // TODO: destroy habit from DB (call DB manager)
    }
}
