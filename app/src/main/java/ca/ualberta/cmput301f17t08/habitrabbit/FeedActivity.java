package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
/**
 * Feed activity in the main menu page
 */
public class FeedActivity extends AppCompatActivity {
    public RecyclerView feedRecyclerView;
    public ArrayList<HabitEvent> feedList;
    public ArrayList<String> followerList;
    private historyAdapter cAdapt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);
        feedRecyclerView = (RecyclerView) findViewById(R.id.feed_recycle);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        //get the current user's feed list
        feedList = LoginManager.getInstance().getCurrentUser().getHistory();
        //get the followers
        followerList = LoginManager.getInstance().getCurrentUser().getFollowers();

        //testing code
        followerList.add("Yuxuan");


        String username;
        ArrayList<HabitEvent> followerFeedList;

        // get the followers feed, and append them to the feedList
        for(int each = 0; each < followerList.size(); each++){
            username = followerList.get(each);
            followerFeedList = new User(username).getHistory();
            for (int index = 0; index<followerFeedList.size();index++){
                feedList.add(followerFeedList.get(index));
            }
        }


        // set up the adapter
        cAdapt = new historyAdapter(LoginManager.getInstance().getCurrentUser().getUsername(), feedList,this);
        feedRecyclerView.setAdapter(cAdapt);
    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
