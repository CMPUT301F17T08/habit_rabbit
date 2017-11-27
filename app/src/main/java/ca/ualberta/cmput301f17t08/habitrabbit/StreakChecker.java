package ca.ualberta.cmput301f17t08.habitrabbit;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by yuxuanzhao on 2017-11-26.
 */

public class StreakChecker extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //OnReceive will get called once a minute has passed
        User current_user = LoginManager.getInstance().getCurrentUser();
        ArrayList<Habit> habit_list = current_user.getHabits();

        //get the current time
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        Date now = calendar.getTime();

        if (habit_list.size() > 0) {
           for (Habit habit : habit_list) {
               Date last_date = habit.getLastCompleted();
               if (last_date != null) {
                   long diff = now.getTime() - last_date.getTime();
                   // convert millisecond to hours
                   long diffHours = diff / (60 * 60 * 1000);
                   //see if 24 hrs has passed since user has completed the habit
                   if (diffHours >= 24) {
                       //reset the streak of the habit to 0
                       habit.resetStreak();
                   }
               }
           }
        }
    }

}
