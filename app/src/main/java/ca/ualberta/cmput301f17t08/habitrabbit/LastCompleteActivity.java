package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * The activity for the the feed function, it shows the most recent completed activity
 */
public class LastCompleteActivity extends AppCompatActivity {

    private ArrayList<HabitEvent> selectedHabitEvents;

    private HabitEventListAdapter cAdapt;
    private RecyclerView lastCompleteRecyclerView;
    private TextView Streak;
    private TextView averageTime;
    private TextView percentCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_complete_habit);

        //get the username from Following Habits Adapter, the person that user clicks
        final String username = getIntent().getStringExtra("TheFollowName");
        final String habitId = getIntent().getStringExtra("habitId");
        final Habit habit = (Habit) getIntent().getSerializableExtra("habitObject");


        final TextView streak = findViewById(R.id.last_complete_streak);
        final TextView percentage = findViewById(R.id.last_complete_percent);
        final TextView averageTime= findViewById(R.id.last_complete_time);

        TextView usernameView = findViewById(R.id.last_complete_username);
        usernameView.setText(username);



        //set up the recyclerView for view
        lastCompleteRecyclerView = (RecyclerView) findViewById(R.id.last_complete_recycle);
        lastCompleteRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        //Activty context
        final LastCompleteActivity self = this;


        //get the userdata from the db
        DatabaseManager.getInstance().getUserData(username, new DatabaseManager.OnUserDataListener() {
            @Override
            //getting the userdata from the db
            public void onUserData(User followUser) {



                //get the user history from db
                followUser.getHistory(new DatabaseManager.OnHabitEventsListener() {
                    @Override
                    public void onHabitEventsSuccess(HashMap<String, HabitEvent> habitEvents) {

                        selectedHabitEvents = new ArrayList<HabitEvent>();

                        for(final HabitEvent event : habitEvents.values()){
                            if (event.getHabitKey().equals(habitId)){
                                selectedHabitEvents.add(event);
                            }
                        }

                        Collections.sort(selectedHabitEvents, new Comparator<HabitEvent>() {
                            public int compare(HabitEvent H1, HabitEvent H2) {
                                return H1.getDateCompleted().compareTo(H2.getDateCompleted());
                            }
                        });
                        Collections.reverse(selectedHabitEvents);


                        ArrayList<HabitEvent> lastCompleted = new ArrayList<HabitEvent>();
                        if (selectedHabitEvents.size() != 0){
                            lastCompleted.add(selectedHabitEvents.get(0));
                        }

                        cAdapt = new HabitEventListAdapter(lastCompleted,self);
                        lastCompleteRecyclerView.setAdapter(cAdapt);

                        streak.setText(""+habit.getStreak());
                        percentage.setText(Float.toString(habit.getPercentCompleted()*100)+"%");
                        averageTime.setText(habit.getAverageTimeStr());
                    }

                    @Override
                    public void onHabitEventsFailed(String message) {

                    }
                });

            }

            @Override
            public void onUserDataFailed(String message) {

            }
        });


    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}