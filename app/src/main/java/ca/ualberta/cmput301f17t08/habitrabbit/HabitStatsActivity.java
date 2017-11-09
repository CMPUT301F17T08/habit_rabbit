package ca.ualberta.cmput301f17t08.habitrabbit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

        int habit_id = (int)getIntent().getSerializableExtra("habit_id");
        habit = LoginManager.getInstance().getCurrentUser().getHabits().get(habit_id);

        streakCount = (TextView)findViewById(R.id.streak_count);
        completedPercentCount = (TextView)findViewById(R.id.percent_completed_count);
        daysCompletedCount = (TextView)findViewById(R.id.days_completed_count);
        averageTimeCount = (TextView)findViewById(R.id.avg_time_count);

        List<Object> statistics = habit.getStatistics();

        streakCount.setText("" + statistics.get(1));
        completedPercentCount.setText(Math.floor((Float)statistics.get(3) * 100) + "%");
        daysCompletedCount.setText("" + statistics.get(0));
        averageTimeCount.setText("" + statistics.get(2));
    }

}
