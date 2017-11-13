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

    public Habit(){
        this.habiteventlist = new ArrayList<HabitEvent>();
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
        List <Integer> frequency_tracker = new ArrayList<Integer>();

        for(int index =0; index < this.frequency.size(); index ++){
            if(this.frequency.get(index).equals(1)){
                if(index != 6) {
                    frequency_tracker.add(index + 2);
                }
                else {
                    frequency_tracker.add(1);
                }
            }

        }
        frequency_tracker.add(3);
        frequency_tracker.add(1);
        System.out.println(frequency_tracker.toString());
        int daysSinceStart = 0;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        Date now = calendar.getTime();
        System.out.println(now.toString());
        System.out.println("start date"+startDate.toString());
        Calendar timeNow = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        timeNow.setTime(now);
        Calendar startDay = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        startDay.setTime(startDate);
        System.out.println(frequency_tracker.toString());

//        System.out.println(timeNow.after(startDay));
        long time_counter = startDay.getTimeInMillis();
        System.out.println(time_counter);
        System.out.println(timeNow.getTimeInMillis());
        while (time_counter < timeNow.getTimeInMillis()) {
            System.out.println("loop going");
            for (int each = 0; each < frequency_tracker.size(); each++) {
                System.out.println("hsdh");

                if (startDay.get(Calendar.DAY_OF_WEEK) == frequency_tracker.get(each)) {
                    daysSinceStart += 1;

                }
                System.out.println(daysSinceStart);
                time_counter += 24*60*60*1000;
//                startDay.add(Calendar.DATE, 1);
                System.out.println("loop");
            }
        }


        String averageTimeStr = new SimpleDateFormat("HH:mm").format(this.averageTime);
        List<Object> statistics = new ArrayList<Object>();
        statistics.add(this.daysCompleted);
        statistics.add(this.streak);
        statistics.add(averageTimeStr);

        // TODO this is incorrect - need to take into account which days the user said they would complete

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
            this.averageTime = now.getTime() % 8640000;     // milliseconds elapsed until now today
        }
        // update the streak if the last update was yesterday
        if (this.lastCompleted != null && now.getTime() - this.lastCompleted.getTime() > 86400000){
            this.streak += 1;
        }

        this.lastCompleted = new Date();

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


}
