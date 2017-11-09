package ca.ualberta.cmput301f17t08.habitrabbit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by micah on 01/11/17.
 */

public class MyHabitActivity extends AppCompatActivity {

    private MyHabitActivity activity = this;

    private ArrayList<Habit> habitList;
    private HabitsAdapter cAdapt;
    private RecyclerView habitsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit);

        habitList = LoginManager.getInstance().getCurrentUser().getHabits();

        System.out.println("SIZE");
        System.out.println(habitList.size());
        System.out.println("SIZE");
        ArrayList<Integer> frequency = new ArrayList<Integer>(Arrays.asList(new Integer[]{1,0,1,0,1,0,1}));
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        Date now = calendar.getTime();
        for(int counter=0; counter < habitList.size(); counter++){
            habitList.set(counter,new Habit("Yuxuan","hdkhfajk",now,frequency));
        }



        habitsRecyclerView = (RecyclerView) findViewById(R.id.habit_recyclerview);
        habitsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        cAdapt = new HabitsAdapter(habitList);
        habitsRecyclerView.setAdapter(cAdapt);

    }

}
