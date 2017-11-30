package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Today activity for today page
 */

public class TodayActivity extends AppCompatActivity {
    //initialize the variables needed in the class
    private ArrayList<Habit> habitList;
    private TodayAdapter cAdapt;
    private RecyclerView habitRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // get the element from the Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today);
        habitRecyclerView = (RecyclerView) findViewById(R.id.recycle);
        habitRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        //get the current user's history list
        habitList = new ArrayList<Habit>();

        final TodayActivity self = this;

        LoginManager.getInstance().getCurrentUser().getHabits(new DatabaseManager.OnHabitsListener() {
            @Override
            public void onHabitsSuccess(ArrayMap<String, Habit> habits) {

                habitList = new ArrayList<Habit>(habits.values());

                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
                Date now = calendar.getTime();

                // set to midnight to make it easier to check if the habit was completed before today
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                int current_day = calendar.get(Calendar.DAY_OF_WEEK);

                //convert the date index from calendar class to frenquency list
                int [] day_convert = {0,6,0,1,2,3,4,5};
                current_day = day_convert[current_day];

                //set up an arraylist used to store the today's habit
                ArrayList<Habit> todayHabit = new ArrayList<Habit>();

                //check if the day of week is in frequency list
                for(int index = 0; index < habitList.size(); index++){
                    Habit tempHabit = habitList.get(index);
                    if(tempHabit.getDate().before(now) && tempHabit.getFrequency().get(current_day) == 1){

                        // don't display the habit if it was already completed today
                        if (tempHabit.getLastCompleted() == null ||
                                (tempHabit.getLastCompleted() != null && calendar.getTime().after(tempHabit.getLastCompleted()))){
                            todayHabit.add(tempHabit);
                        }

                    }
                }

                // set up the adapter
                cAdapt = new TodayAdapter( todayHabit, self);
                habitRecyclerView.setAdapter(cAdapt);
                cAdapt.notifyDataSetChanged();
            }

            @Override
            public void onHabitsFailed(String message) {
                Log.e("TodayActivity", "Failed to retrieve habits from user!");
                self.finish();
            }
        });

    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }
    // pass the result from the add habit event activity to the adapter since the habit is located there
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        cAdapt.onActivityResult(requestCode, resultCode, data);
    }
}
