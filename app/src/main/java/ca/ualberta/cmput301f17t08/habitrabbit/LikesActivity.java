package ca.ualberta.cmput301f17t08.habitrabbit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class LikesActivity extends AppCompatActivity {

    private LikesActivity activity = this;

    private ArrayList<User> likes;
    private ArrayList<User> followRequests;
    private LikesAdapter cAdapt;

    private RecyclerView LikesRecyclerView;
    private RecyclerView peopleFollowerRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        /*
        TODO: get the instance of the user's likes and followrequest lists from firebase
         */
        //followingList = LoginManager.getInstance().getCurrentUser().getHabits();

        //create recycleview for likes
        LikesRecyclerView = (RecyclerView) findViewById(R.id.likes_recyclerview);
        LikesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

       //TODO: still have to incorporate follow requests in recyclerview

        //set the adapter for the following list
        cAdapt = new LikesAdapter(likes);
        LikesRecyclerView.setAdapter(cAdapt);

       

    }

}
