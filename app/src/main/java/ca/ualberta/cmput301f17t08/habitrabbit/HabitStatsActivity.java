package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * The activity for habit statistic
 */

public class HabitStatsActivity extends AppCompatActivity {

    private HabitStatsActivity activity = this;
    private Habit habit;

    private TextView streakCount;
    private TextView completedPercentCount;
    private TextView daysCompletedCount;
    private TextView averageTimeCount;
    private TextView habitStartDate;
    private TextView habitLastCompletedDate;
    private CardView percentCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_stats);

        streakCount = (TextView) findViewById(R.id.streak_count);
        completedPercentCount = (TextView)findViewById(R.id.percent_completed_count);
        daysCompletedCount = (TextView)findViewById(R.id.days_completed_count);
        averageTimeCount = (TextView)findViewById(R.id.avg_time_count);
        habitStartDate = (TextView)findViewById(R.id.habit_start_date);
        habitLastCompletedDate = (TextView)findViewById(R.id.last_completed_date);
        percentCardView = (CardView) findViewById(R.id.percentage_complete);

        final String habit_id = (String)getIntent().getSerializableExtra("habit_id");
        LoginManager.getInstance().getCurrentUser().getHabits(
            new DatabaseManager.OnHabitsListener() {
                @Override
                public void onHabitsSuccess(HashMap<String, Habit> habits) {

                    habit = habits.get(habit_id);

                    // TODO: Clean up getStatistics to create several different getters/setters for each member.
                    // Firebase does not properly save and retrieve these due to this.
                    streakCount.setText("" + habit.getStreak());
                    completedPercentCount.setText(Math.round(Math.floor(habit.getPercentCompleted() * 100)) + "%");
                    daysCompletedCount.setText("" + habit.getDaysCompleted());
                    averageTimeCount.setText(habit.getAverageTimeStr());

                    //format the date
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
                    String startDateStr = dateFormatter.format(habit.getDate());
                    habitStartDate.setText(startDateStr);

                    String lastCompletedDateStr;
                    if (habit.getLastCompleted() != null){
                        lastCompletedDateStr = dateFormatter.format(habit.getLastCompleted());
                    }else{
                        lastCompletedDateStr = "N/A";
                    }

                    habitLastCompletedDate.setText(lastCompletedDateStr);

                    //color list for the background of percentage block
                    String[] colorList = {"#ff0000","#ff4000","#ff8000","#ffB000","#fff000","#F0FF00","#E0FF00","#D0FF00","#B0FF00","#00FF00","#00FF00"};

                    //pick the color based on the percentage

                    int colorIndex = (int)Math.round(Math.floor(habit.getPercentCompleted() * 100))/10;
                    percentCardView.setCardBackgroundColor(Color.parseColor(colorList[colorIndex]));
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

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

}
