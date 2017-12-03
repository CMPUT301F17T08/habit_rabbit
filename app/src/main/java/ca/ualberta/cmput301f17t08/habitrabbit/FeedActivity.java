package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Feed activity in the main menu page
 */
public class FeedActivity extends AppCompatActivity {
    public RecyclerView feedRecyclerView;
    public ArrayList<HabitEvent> feedList;

    public ArrayList<String> followerList;
    private Button map_button;
    private FeedActivity activity = this;


    public ArrayList<String> followingList;
    private FeedAdapter cAdapt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);
        feedRecyclerView = (RecyclerView) findViewById(R.id.feed_recycle);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        feedList = new ArrayList<HabitEvent>();
        //get the followers
        followingList = LoginManager.getInstance().getCurrentUser().getFollowing();

        map_button = (Button) findViewById(R.id.location_button);
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, gpsActivity.class);
                startActivity(intent);
            }
        });

        reloadData();

    }

    private void reloadData(){
        String username;
        final ArrayList<String> usernameList = new ArrayList<String>();

        // get the followers feed, and append them to the feedList
        for(int each = 0; each < followingList.size(); each++) {
            username = followingList.get(each);
            usernameList.add(username);
            DatabaseManager.getInstance().getUserData(username, new DatabaseManager.OnUserDataListener() {
                @Override
                public void onUserData(User user) {
                    final User eventOwner = user;
                    eventOwner.getHabits(new DatabaseManager.OnHabitsListener() {
                        @Override
                        public void onHabitsSuccess(HashMap<String, Habit> habits) {
                            ArrayList<Habit>habitList = new ArrayList<Habit>(habits.values());

                            for (Habit habit:habitList){
                                habit.getHabitEvents(new DatabaseManager.OnHabitEventsListener() {
                                    @Override
                                    public void onHabitEventsSuccess(HashMap<String, HabitEvent> habitEvents) {
                                        ArrayList<HabitEvent> feedList = new ArrayList<HabitEvent>(habitEvents.values());


                                        Collections.sort(feedList, new Comparator<HabitEvent>() {
                                            public int compare(HabitEvent H1, HabitEvent H2) {
                                                return H1.getDateCompleted().compareTo(H2.getDateCompleted());
                                            }
                                        });


                                        Collections.reverse(feedList);
                                        cAdapt = new FeedAdapter(usernameList, feedList, FeedActivity.this);
                                        feedRecyclerView.setAdapter(cAdapt);

                                        cAdapt.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onHabitEventsFailed(String message) {
                                        Log.e("HistoryActivity", "Failed to get habit events for filtered habit!");
                                        finish();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onHabitsFailed(String message) {
                            Log.e("MyHabitActivity", "Failed to get habits of user!");
                        }
                    });
                }

                @Override
                public void onUserDataFailed(String message) {

                }
            });
    }
        }

    @Override
    protected void onResume() {
        super.onResume();

        reloadData();
    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
