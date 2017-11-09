package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ViewHabitDetail extends AppCompatActivity {
    private TextView Streak;
    private TextView Completed;
    private TextView Days_Completed;
    private TextView Average_Time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit_detail);

        Button BackButton = (Button) findViewById(R.id.back);
        Button EditButton = (Button) findViewById(R.id.edit);

        Streak = (TextView) findViewById(R.id.streak);
        Completed = (TextView) findViewById(R.id.completed);
        Days_Completed = (TextView) findViewById(R.id.dayscompleted);
        Average_Time = (TextView) findViewById(R.id.averagetime);

        //TODO Need to delete after merged
        // just for now, before merge everthing, don't know what will be passed into this activity.
        int[] frequency = {1,0,1,0,1,0,1};
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        Date now = calendar.getTime();

        Habit habit = new Habit("Name", "Reason", now, frequency);
        //Streak.setText(Integer.toString(habit.getstreak()));
        //Completed.setText(Integer.toString(habit.getcompleted()));
        //Days_Completed.setText(Integer.toString(habit.getdaysCompleted()));
        //Average_Time.setText(Integer.toString(habit.getaverageTime()));



        BackButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent backIntent = new Intent(ViewHabitDetail.this, MainActivity.class);
                //TODO Need to change to whichever the page will go back to
                startActivity(backIntent);
            }
        });

        EditButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent editIntent = new Intent(ViewHabitDetail.this, Edithabit.class);
                //startActivityForResult(editIntent, Edit_habit);
            }
        });

    }
}
