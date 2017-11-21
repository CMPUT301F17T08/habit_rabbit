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

/**
 * Created by yuxuanzhao on 2017-11-07.
 */

public class TodayActivity extends AppCompatActivity {
    //initialize the variables needed in the class
    private ArrayList<Habit> habitList;
    private HabitsAdapter cAdapt;
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int current_day = calendar.get(Calendar.DAY_OF_WEEK);
        if (current_day == 0){
            current_day = 6;
        }
        else{
            current_day -= 2;
        }


        //set up an arraylist used to store the today's habit
        ArrayList<Habit> todayHabit = new ArrayList<Habit>();

        //check if the day of week is in frequency list
        for(int index=0;index <habitList.size();index++){
            if(habitList.get(index).getFrequency().get(current_day) == 1){
                todayHabit.add(habitList.get(index));
            }
        }
        // set up the adapter
        cAdapt = new HabitsAdapter( todayHabit,this);
        habitRecyclerView.setAdapter(cAdapt);

    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}