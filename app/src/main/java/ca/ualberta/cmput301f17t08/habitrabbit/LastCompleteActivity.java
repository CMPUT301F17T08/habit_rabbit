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
    private ArrayList<HabitEvent> lastCompleteList;//history list from user
    private ArrayList<HabitEvent> lastComplete;//history list that will be passed to the viewAdapter

    private historyAdapter cAdapt;
    private RecyclerView lastCompleteRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_complete_habit);

        final String username = getIntent().getStringExtra("TheFollowName");
        TextView usernameView = findViewById(R.id.last_complete_username);
        usernameView.setText(username);

        //set up the recyclerView for view
        lastCompleteRecyclerView = (RecyclerView) findViewById(R.id.last_complete_recycle);
        lastCompleteRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        final LastCompleteActivity self = this;



        DatabaseManager.getInstance().getUserData(username, new DatabaseManager.OnUserDataListener() {
            @Override
            public void onUserData(User user) {
                User followUser = user;
                followUser.getHistory(new DatabaseManager.OnHabitEventsListener() {
                    @Override
                    public void onHabitEventsSuccess(HashMap<String, HabitEvent> habitEvents) {
                        lastCompleteList = new ArrayList<HabitEvent>(habitEvents.values());

                        if (lastCompleteList.size() != 0){
                            
                            lastComplete = new ArrayList<HabitEvent> ();
                            lastComplete.add(lastCompleteList.get(lastCompleteList.size()-1));
                            System.out.println(lastComplete.toString());
                        }

                        cAdapt = new historyAdapter(username,lastComplete,self);
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
