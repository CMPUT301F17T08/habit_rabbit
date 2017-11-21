package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by micah on 01/11/17.
 */

public class PeopleActivity extends AppCompatActivity {

    private PeopleActivity activity = this;

    private ArrayList<String> followingList;
    private ArrayList<String> followerList;
    private PeopleAdapter cAdapt;
    private PeopleAdapter cAdapt2;
    private RecyclerView peopleFollowingRecyclerView;
    private RecyclerView peopleFollowerRecyclerView;

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
        peopleFollowingRecyclerView.setAdapter(cAdapt2);

    }
    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

}
