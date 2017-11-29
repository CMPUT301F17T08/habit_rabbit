package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        habitStartDate = (TextView)findViewById(R.id.habit_start_date);
        CardView percentCardView = (CardView) findViewById(R.id.percentage_complete);

        // TODO: Clean up getStatistics to create several different getters/setters for each member.
        // Firebase does not properly save and retrieve these due to this.
        List<Object> statistics = habit.getStatistics();

        streakCount.setText("" + statistics.get(1));
        completedPercentCount.setText(Math.round(Math.floor((Float)statistics.get(3) * 100)) + "%");
        daysCompletedCount.setText("" + statistics.get(0));
        averageTimeCount.setText("" + statistics.get(2));

        //format the date
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        String strDate = dateFormatter.format(habit.getDate());
        habitStartDate.setText(strDate);

        //color list for the background of percentage block
        String[] colorList = {"#ff0000","#ff4000","#ff8000","#ffB000","#fff000","#F0FF00","#E0FF00","#D0FF00","#B0FF00","#00FF00"};

        //pick the color based on the percentage
        int colorIndex = (int)Math.round(Math.floor((Float)statistics.get(3) * 100))/10 - 1;
        percentCardView.setCardBackgroundColor(Color.parseColor(colorList[colorIndex]));

    }


    public void showEditHabitActivity(View v){
        Intent intent = new Intent(this, EditHabitActivity.class);
        intent.putExtra("habit", habit);
        startActivity(intent);
    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

}
