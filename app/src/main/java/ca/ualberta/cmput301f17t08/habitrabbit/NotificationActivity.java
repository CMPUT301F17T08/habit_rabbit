package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private NotificationActivity activity = this;
    private NotificationAdapter cAdapt;

    private RecyclerView LikesRecyclerView;
    private RecyclerView peopleFollowerRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        /*
        TODO: get the instance of the user's likes and followrequest lists from firebase
         */
        User currentUser = LoginManager.getInstance().getCurrentUser();
        ArrayList<User> likeList = currentUser.getLikeList();
        ArrayList<String> pendingFollower = currentUser.getFollowRequests();

        //put like and follow request lists together
        for(User user: likeList){
            pendingFollower.add(user.getUsername());
        }

        //create recycleview for likes
        LikesRecyclerView = (RecyclerView) findViewById(R.id.likes_recyclerview);
        LikesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

       //TODO: still have to incorporate follow requests in recyclerview

        //set the adapter for the following list
        cAdapt = new NotificationAdapter(this,pendingFollower);
        LikesRecyclerView.setAdapter(cAdapt);

    }

    @Override
    protected void onRestart() {
        //when the activity restarts
        super.onRestart();
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

}
