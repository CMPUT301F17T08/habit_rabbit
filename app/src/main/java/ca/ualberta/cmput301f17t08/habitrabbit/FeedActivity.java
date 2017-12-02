package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

/**
 * Feed activity in the main menu page
 */
public class FeedActivity extends AppCompatActivity {
    private FeedActivity activity = this;

    public RecyclerView feedRecyclerView;
    public ArrayList<HabitEvent> feedList;
    public ArrayList<String> followingList;
    private FeedAdapter cAdapt;
    private Button LocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);
        feedRecyclerView = (RecyclerView) findViewById(R.id.feed_recycle);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        LocationButton = (Button) findViewById(R.id.location_button);
        feedList = new ArrayList<HabitEvent>();



        //
        //LocationButton.setOnClickListener(new View.OnClickListener() {
        //@Override
        //    public void onClick(View view) {
        //        Intent intent = new Intent(activity, MaoActivity.class);
        //        startActivity(intent);
        //    }
        //});


        //
        //get the followers
        followingList = LoginManager.getInstance().getCurrentUser().getFollowing();

        String username;
        ArrayList<String> usernameList = new ArrayList<String>();


        // get the followers feed, and append them to the feedList
        for(int each = 0; each < followingList.size(); each++){
            username = followingList.get(each);
            usernameList.add(username);

            DatabaseManager.getInstance().getUserData(username, new DatabaseManager.OnUserDataListener() {
                @Override
                public void onUserData(User user) {
                    final User followingUser = user;
                    ArrayList<HabitEvent> followingFeedList = followingUser.getHistory();

                    for (int index = 0; index<followingFeedList.size();index++){
                        feedList.add(followingFeedList.get(index));
                    }
                }

                @Override
                public void onUserDataFailed(String message) {

                }
            });

        }

        Collections.sort(feedList, new Comparator<HabitEvent>() {
            public int compare(HabitEvent H1, HabitEvent H2) {
                return H1.getDateCompleted().compareTo(H2.getDateCompleted());
            }
        });

        // set up the adapter
        cAdapt = new FeedAdapter(usernameList, feedList,this);
        feedRecyclerView.setAdapter(cAdapt);
    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
