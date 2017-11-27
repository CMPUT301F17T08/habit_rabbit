package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by yuxuanzhao on 2017-11-07.
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
        habitList = LoginManager.getInstance().getCurrentUser().getHabits();

        //get the day of week in terms of index in frequency
        Date now = new Date();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));

        calendar.setTime(now);
        int current_day = calendar.get(Calendar.DAY_OF_WEEK);

        //convert the date index from calendar class to frenquency list
        int [] day_convert = {0,6,0,1,2,3,4,5};
        current_day = day_convert[current_day];

        //set up an arraylist used to store the today's habit
        ArrayList<Habit> todayHabit = new ArrayList<Habit>();

        //check if the day of week is in frequency list
        for(int index = 0; index < habitList.size(); index++){
            if(habitList.get(index).getFrequency().get(current_day) == 1){
                todayHabit.add(habitList.get(index));
            }
        }
        // set up the adapter
        cAdapt = new TodayAdapter( todayHabit,this);
        habitRecyclerView.setAdapter(cAdapt);

    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
