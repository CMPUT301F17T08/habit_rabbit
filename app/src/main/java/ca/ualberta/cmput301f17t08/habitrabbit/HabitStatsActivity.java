package ca.ualberta.cmput301f17t08.habitrabbit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by micah on 01/11/17.
 */

public class HabitStatsActivity extends AppCompatActivity {

    private HabitStatsActivity activity = this;
    private Habit habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_stats);

        int habit_id = (int)getIntent().getSerializableExtra("habit_id");

        habit = LoginManager.getInstance().getCurrentUser().getHabits().get(habit_id);

        Log.e("stats test",habit.getName());

    }

}
