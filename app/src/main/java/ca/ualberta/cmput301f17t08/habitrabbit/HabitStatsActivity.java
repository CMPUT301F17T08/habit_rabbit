package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micah on 01/11/17.
 */

public class HabitStatsActivity extends AppCompatActivity {

    private HabitStatsActivity activity = this;
    private Habit habit;

    private TextView streakCount;
    private TextView completedPercentCount;
    private TextView daysCompletedCount;
    private TextView averageTimeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_stats);

        streakCount = (TextView) findViewById(R.id.streak_count);
        completedPercentCount = (TextView)findViewById(R.id.percent_completed_count);
        daysCompletedCount = (TextView)findViewById(R.id.days_completed_count);
        averageTimeCount = (TextView)findViewById(R.id.avg_time_count);

        final String habit_id = (String)getIntent().getSerializableExtra("habit_id");
        LoginManager.getInstance().getCurrentUser().getHabits(
            new DatabaseManager.OnHabitsListener() {
                @Override
                public void onHabitsSuccess(ArrayMap<String, Habit> habits) {

                    habit = habits.get(habit_id);
                    // TODO: Clean up getStatistics to create several different getters/setters for each member.
                    // Firebase does not properly save and retrieve these due to this.
                    List<Object> statistics = habit.getStatistics();

                    streakCount.setText("" + statistics.get(1));
                    completedPercentCount.setText(Math.round(Math.floor((Float)statistics.get(3) * 100)) + "%");
                    daysCompletedCount.setText("" + statistics.get(0));
                    averageTimeCount.setText("" + statistics.get(2));
                }

                @Override
                public void onHabitsFailed(String message) {
                    Log.e("HabitStatsActivity", "Failed to get habit: " + message);
                    finish();
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
    }

    public void showEditHabitActivity(View v){
        Intent intent = new Intent(this, EditHabitActivity.class);
        intent.putExtra("habit", habit);
        startActivityForResult(intent, 0);
    }

}
