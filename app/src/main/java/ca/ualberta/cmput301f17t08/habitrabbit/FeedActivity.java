package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
/**
 * Feed activity in the main menu page
 */
public class FeedActivity extends AppCompatActivity {
    public RecyclerView feedRecyclerView;
    public ArrayList<HabitEvent> feedList;

    public ArrayList<String> followerList;
    private historyAdapter cAdapt;
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

        //testing code

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



        // set up the adapter
        cAdapt = new FeedAdapter(usernameList, feedList,this);
        feedRecyclerView.setAdapter(cAdapt);
        map_button = (Button) findViewById(R.id.map_button);
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, gpsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
