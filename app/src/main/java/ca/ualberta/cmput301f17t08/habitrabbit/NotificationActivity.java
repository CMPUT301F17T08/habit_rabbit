package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

/**
 * The activity for the notification, could send and receive a notification
 */


public class NotificationActivity extends AppCompatActivity {

    private NotificationActivity activity = this;
    private NotificationAdapter cAdapt;

    private RecyclerView LikesRecyclerView;
    private RecyclerView peopleFollowerRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);
        reloadData();

    }

    @Override
    protected void onResume() {
        super.onResume();

        reloadData();
    }

    private void reloadData(){
        // Required to get from DB, since current user isn't updated with likes from current session.
        DatabaseManager.getInstance().getUserData(LoginManager.getInstance().getCurrentUser().getUsername(), new DatabaseManager.OnUserDataListener() {
            @Override
            public void onUserData(User user) {
                ArrayList<LikeNotification> likeList = user.getLikes();
                ArrayList<FollowNotification> pendingFollower = user.getFollowRequests();

                ArrayList<Notification> notifications = new ArrayList<Notification>();

                //put like and follow request lists together
                for(LikeNotification notification: likeList){
                    notifications.add(notification);
                }

                for(FollowNotification notification: pendingFollower){
                    if (!(notification.getUsername().equals(LoginManager.getInstance().getCurrentUser().getUsername()) )) {
                        notifications.add(notification);
                    }
                }

                //create recycleview for likes
                LikesRecyclerView = (RecyclerView) findViewById(R.id.likes_recyclerview);
                LikesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                //set the adapter for the following list
                cAdapt = new NotificationAdapter(activity, notifications);
                LikesRecyclerView.setAdapter(cAdapt);
                cAdapt.notifyDataSetChanged();
            }

            @Override
            public void onUserDataFailed(String message) {

            }
        });

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
