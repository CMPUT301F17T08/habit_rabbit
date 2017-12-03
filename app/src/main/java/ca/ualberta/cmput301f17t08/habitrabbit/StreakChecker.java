package ca.ualberta.cmput301f17t08.habitrabbit;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.ArrayMap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * The Streak class to calculate habits' streak
 */

public class StreakChecker extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //OnReceive will get called once a minute has passed
        User current_user = LoginManager.getInstance().getCurrentUser();
        current_user.getHabits(new DatabaseManager.OnHabitsListener() {
            @Override
            public void onHabitsSuccess(HashMap<String, Habit> habits) {
                //get the current time
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
                Date now = calendar.getTime();

//                if (habits.size() > 0) {
//                    for (Habit habit : habits.values()) {
//                        Date last_date = habit.getLastCompleted();
//                        if (last_date != null) {
//                            long diff = now.getTime() - last_date.getTime();
//                            // convert millisecond to hours
//                            long diffHours = diff / (60 * 60 * 1000);
//                            //see if 24 hrs has passed since user has completed the habit
//                            if (diffHours >= 24) {
//                                //reset the streak of the habit to 0
//                                habit.resetStreak();
//                                habit.sync(new DatabaseManager.OnSaveListener() {
//                                    @Override
//                                    public void onSaveSuccess() {
//                                        // No need to handle save success
//                                    }
//
//                                    @Override
//                                    public void onSaveFailure(String message) {
//                                        // No need to handle save failure
//                                    }
//                                });
//                            }
//                        }
//                    }
//                }
                if (habits.size() > 0){
                    for (Habit habit : habits.values()){
                        if (habit.getLastCompleted() != null){

                            // make the streak 0 if a day was missed in the middle
                            int [] conversion_table = {0, 6, 0, 1, 2, 3, 4, 5};

                            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
                            c.setTime(habit.getLastCompleted());
                            c.add(c.DATE, 1);

                            while (c.getTime().before(now)){
                                int tempDayIndex = c.get(Calendar.DAY_OF_WEEK);
                                if (habit.getFrequency().get(conversion_table[tempDayIndex]) == 1){
                                    habit.resetStreak();

                                    habit.sync(new DatabaseManager.OnSaveListener() {
                                        @Override
                                        public void onSaveSuccess() {
                                            // No need to handle save success
                                        }

                                        @Override
                                        public void onSaveFailure(String message) {
                                            // No need to handle save failure
                                        }
                                    });

                                    break;
                                }
                            }

                        }
                    }
                }


            }

            @Override
            public void onHabitsFailed(String message) {
                Log.e("StreakChecker", "Failed to get habits!");
            }
        });

    }

}
