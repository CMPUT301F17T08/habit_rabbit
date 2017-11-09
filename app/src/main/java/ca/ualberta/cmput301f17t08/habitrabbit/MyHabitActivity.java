package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

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
    private Button menuButton;
    private Button addHabitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit);

        habitList = LoginManager.getInstance().getCurrentUser().getHabits();

        menuButton = (Button) findViewById(R.id.menu_button);
        addHabitButton = (Button) findViewById(R.id.add_habit_button);
        habitsRecyclerView = (RecyclerView) findViewById(R.id.habit_recyclerview);
        habitsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        cAdapt = new HabitsAdapter(habitList, this);
        habitsRecyclerView.setAdapter(cAdapt);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MenuActivity.class);
                startActivity(intent);
            }
        });

        addHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AddHabitActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected  void onResume() {
        super.onResume();
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
}
