package ca.ualberta.cmput301f17t08.habitrabbit;

import java.util.Date;
import java.util.List;

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
    int averageTime;
    int streak;

    public Habit(String name, String reason, Date startDate, int[] frequency){
        this.name = name;
        this.reason = reason;
        this.startDate = startDate;
        this.frequency = frequency;

        this.lastCompleted = null;
        this.daysCompleted = 0;
        this.averageTime = 0;
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


    public int[] getStatistics(){

        Date today = new Date();

        int daysSinceStart = (int) Math.abs(today.getTime() - startDate.getTime())/(24 * 60 * 60 * 1000);
        // returns days completed, streak, % completed in decimal, average time of completion
        int[] statistics = {this.daysCompleted, this.streak, this.daysCompleted/daysSinceStart, this.averageTime};

        return null;
    }


}
