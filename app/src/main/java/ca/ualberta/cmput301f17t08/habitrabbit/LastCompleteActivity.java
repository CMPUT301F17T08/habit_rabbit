package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The activity for the the feed function, it shows the most recent completed activity
 */
public class LastCompleteActivity extends AppCompatActivity {

    private ArrayList<HabitEvent> lastCompleteList;
    private ArrayList<HabitEvent> lastComplete;

    private HabitEventListAdapter cAdapt;
    private RecyclerView lastCompleteRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_complete_habit);

        //get the username from Following Habits Adapter, the person that user clicks
        final String username = getIntent().getStringExtra("TheFollowName");
        TextView usernameView = findViewById(R.id.last_complete_username);
        usernameView.setText(username);

        //set up the recyclerView for view
        lastCompleteRecyclerView = (RecyclerView) findViewById(R.id.last_complete_recycle);
        lastCompleteRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        //Activty context
        final LastCompleteActivity self = this;



        //get the userdata from the db
        DatabaseManager.getInstance().getUserData(username, new DatabaseManager.OnUserDataListener() {
            @Override
            //getting the userdata from the db
            public void onUserData(User user) {
                User followUser = user;
                //get the user history from db
                followUser.getHistory(new DatabaseManager.OnHabitEventsListener() {
                    @Override
                    public void onHabitEventsSuccess(HashMap<String, HabitEvent> habitEvents) {
                        lastCompleteList = new ArrayList<HabitEvent>(habitEvents.values());

                        //check if lastcomplete list is empty, if it is not, put last element in the adapter
                        if (lastCompleteList.size() != 0){
                            lastComplete = new ArrayList<HabitEvent> ();
                            lastComplete.add(lastCompleteList.get(lastCompleteList.size()-1));
                            System.out.println(lastComplete.toString());
                        }

                        cAdapt = new HabitEventListAdapter(lastComplete,self);
                        lastCompleteRecyclerView.setAdapter(cAdapt);
                    }

                    @Override
                    public void onHabitEventsFailed(String message) {

                    }
                });

            }

            @Override
            public void onUserDataFailed(String message) {

            }
        });


    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}