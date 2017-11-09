package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

        habitsRecyclerView = (RecyclerView) findViewById(R.id.habit_recyclerview);
        habitsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        cAdapt = new HabitsAdapter(habitList);
        habitsRecyclerView.setAdapter(cAdapt);

        Intent intent = new Intent(this, PeopleActivity.class);
        startActivity(intent);
    }

}
