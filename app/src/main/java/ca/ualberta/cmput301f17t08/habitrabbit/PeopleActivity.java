package ca.ualberta.cmput301f17t08.habitrabbit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * The activity for people option in the main menu
 */

public class PeopleActivity extends AppCompatActivity {

    private PeopleActivity activity = this;

    private ArrayList<User> followingList;
    private ArrayList<User> followerList;
    private peopleAdapter cAdapt;
    private peopleAdapter cAdapt2;
    private RecyclerView peopleFollowingRecyclerView;
    private RecyclerView peopleFollowerRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people);

        /*
        TODO: get the instance of the user's following list and follower list from firebase
         */
        //followingList = LoginManager.getInstance().getCurrentUser().getHabits();

        //create recycleview for following
        peopleFollowingRecyclerView = (RecyclerView) findViewById(R.id.people_recyclerview);
        peopleFollowingRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        //create recycleview for follower
        peopleFollowerRecyclerView = (RecyclerView) findViewById(R.id.people_recyclerview2);
        peopleFollowerRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        //set the adapter for the following list
        cAdapt = new peopleAdapter(followingList);
        peopleFollowingRecyclerView.setAdapter(cAdapt);

        //set the adapter for the follower list
        cAdapt2 = new peopleAdapter(followerList);
        peopleFollowingRecyclerView.setAdapter(cAdapt2);

    }

}
