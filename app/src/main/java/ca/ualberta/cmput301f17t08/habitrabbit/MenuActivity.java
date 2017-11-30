package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
/**
 * The main menu page after user loged into the app, has six different options
 */
public class MenuActivity extends AppCompatActivity {
    private MenuActivity activity = this;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        Button menuButton = findViewById(R.id.menu_button);
        Button feedButton = findViewById(R.id.feed_button);
        Button todayButton = findViewById(R.id.today_button);
        Button peopleButton = findViewById(R.id.people_button);
        Button habitsButton = findViewById(R.id.habits_button);
        Button historyButton = findViewById(R.id.history_button);
        Button notificationsButton = findViewById(R.id.notifications_button);

        // TODO switch to the respective activities based on the button that is clicked
        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FeedActivity.class);
                startActivity(intent);

            }
        });

        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TodayActivity.class);
                startActivity(intent);
            }
        });

        peopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PeopleActivity.class);
                startActivity(intent);
            }
        });

        habitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MyHabitActivity.class);
                startActivity(intent);
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, historyActivity.class);
                startActivity(intent);
            }
        });

        notificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, LikesActivity.class);
                startActivity(intent);
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // close the menu button
                finish();
            }
        });
    }
}
