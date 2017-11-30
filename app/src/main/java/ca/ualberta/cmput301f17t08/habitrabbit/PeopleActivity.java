package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.widget.Button;


import java.util.ArrayList;

/**
 * The activity for people option in the main menu
 */
public class PeopleActivity extends AppCompatActivity {

    private PeopleActivity activity = this;

    private ArrayList<String> followingList;
    private ArrayList<String> followerList;
    private PeopleAdapter cAdapt;
    private PeopleAdapter cAdapt2;
    private RecyclerView peopleFollowingRecyclerView;
    private RecyclerView peopleFollowerRecyclerView;
    private Button menuButton;
    private Button followPersonButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people);

        followingList = LoginManager.getInstance().getCurrentUser().getFollowing();
        followerList = LoginManager.getInstance().getCurrentUser().getFollowers();

        //create Recycleview for following
        peopleFollowingRecyclerView = (RecyclerView) findViewById(R.id.following_recyclerview);
        peopleFollowingRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        //create Recycleview for follower
        peopleFollowerRecyclerView = (RecyclerView) findViewById(R.id.follower_recyclerview);
        peopleFollowerRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        
        //set the adapter for the following list
        cAdapt = new PeopleAdapter(followingList);
        peopleFollowingRecyclerView.setAdapter(cAdapt);

        //set the adapter for the follower list
        cAdapt2 = new PeopleAdapter(followerList);
        peopleFollowerRecyclerView.setAdapter(cAdapt2);

        followPersonButton = (Button) findViewById(R.id.follow_person_button);
        followPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FollowUserActivity.class);
                startActivity(intent);
            }
        });

        menuButton = (Button) findViewById(R.id.menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MenuActivity.class);
                startActivity(intent);
            }
        });

    }
    // plus button clicked
    public void showFollowUserActivity(View v){
        Intent intent = new Intent(this, FollowUserActivity.class);
        startActivity(intent);
    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
