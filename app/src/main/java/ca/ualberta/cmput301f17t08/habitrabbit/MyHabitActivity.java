package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * The activity for user's habit when user select it
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
        setContentView(R.layout.my_habits);

        habitList = new ArrayList<Habit>();

        menuButton = (Button) findViewById(R.id.menu_button);
        addHabitButton = (Button) findViewById(R.id.add_habit_button);

        habitsRecyclerView = (RecyclerView) findViewById(R.id.habit_recyclerview);
        habitsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

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


        final MyHabitActivity self = this;
        LoginManager.getInstance().getCurrentUser().getHabits(new DatabaseManager.OnHabitsListener() {
            @Override
            public void onHabitsSuccess(ArrayMap<String, Habit> habits) {
                Log.e("Here!", "Here!");

                habitList = new ArrayList<Habit>(habits.values());
                cAdapt = new HabitsAdapter(habitList, self);
                habitsRecyclerView.setAdapter(cAdapt);
                cAdapt.notifyDataSetChanged();
            }

            @Override
            public void onHabitsFailed(String message) {
                Log.e("MyHabitActivity", "Failed to get habits of user!");
            }
        });

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }
}
