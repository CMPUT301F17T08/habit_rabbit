package ca.ualberta.cmput301f17t08.habitrabbit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

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
                System.out.println("Feed");
            }
        });

        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Today");
            }
        });

        peopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("People");
            }
        });

        habitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Habits");
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("History");
            }
        });

        notificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Notifications");
            }
        });
    }
}
