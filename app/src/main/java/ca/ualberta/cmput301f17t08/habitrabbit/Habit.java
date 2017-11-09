package ca.ualberta.cmput301f17t08.habitrabbit;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by maharshmellow on 2017-10-23.
 */

public class Habit {
    private String name;
    private String reason;
    private Date startDate;
    private Date lastCompleted;
    private List<Integer> frequency;
    private int daysCompleted;
    private long averageTime;        // average time of day in milliseconds
    private int streak;

    public Habit(){

    }

    public Habit(String name, String reason, Date startDate, List<Integer> frequency){
        this.name = name;
        this.reason = reason;
        this.startDate = startDate;
        this.frequency = frequency;

        this.lastCompleted = null;
        this.daysCompleted = 0;
        this.averageTime = -1;
        this.streak = 0;
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

    public void setFrequency(List<Integer> frequency){
        this.frequency = frequency;
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

    public List<Integer> getFrequency(){
        return this.frequency;
    }


    // TODO: Separate this into various getters/setters, refactor formatting into calling class.
    // Firebase will not be able to save/retrieve without this.
    public List<Object> getStatistics(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        Date now = calendar.getTime();

        int daysSinceStart = (int) Math.abs(now.getTime() - this.startDate.getTime())/(24 * 60 * 60 * 1000);

        String averageTimeStr = new SimpleDateFormat("hh:mm a").format(this.averageTime);

        List<Object> statistics = new ArrayList<Object>();
        statistics.add(this.daysCompleted);
        statistics.add(this.streak);
        statistics.add(averageTimeStr);

        // % completed
        if (daysSinceStart != 0) {
            statistics.add((float)this.daysCompleted / daysSinceStart);
        }else{
            statistics.add(1);      // 100% completed by default
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


}
