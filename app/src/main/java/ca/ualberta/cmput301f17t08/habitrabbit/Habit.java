package ca.ualberta.cmput301f17t08.habitrabbit;

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
    String name;
    String reason;
    Date startDate;
    Date lastCompleted;
    int[] frequency;
    int daysCompleted;
    long averageTime;        // average time of day in milliseconds
    int streak;

    public Habit(String name, String reason, Date startDate, int[] frequency){
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

    public void setFrequency(int[] frequency){
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

    public int[] getFrequency(){
        return this.frequency;
    }


    public List<Object> getStatistics(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        Date now = calendar.getTime();

        int daysSinceStart = (int) Math.abs(now.getTime() - this.startDate.getTime())/(24 * 60 * 60 * 1000);

        // formats the average time
        String averageTimeStr = new SimpleDateFormat("hh:mm a").format(now);

        List<Object> statistics = new ArrayList<Object>();
        statistics.add(this.daysCompleted);
        statistics.add(this.streak);
        statistics.add(averageTimeStr);

        if (daysSinceStart != 0) {
            statistics.add(this.daysCompleted / daysSinceStart);  // % completed
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
