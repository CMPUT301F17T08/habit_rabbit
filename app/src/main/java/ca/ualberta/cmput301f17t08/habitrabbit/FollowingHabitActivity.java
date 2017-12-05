package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

/**
 * The activity for following a user
 */

public class FollowingHabitActivity extends AppCompatActivity {

    private RecyclerView habitsRecyclerView;
    private FollowingHabitAdapter cAdapt;
    private TextView userNameField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.following_habit);

        //get the person user clicked from the Notification page
        final String username = getIntent().getStringExtra("FollowingName");
        userNameField = (TextView) findViewById(R.id.following_username);
        userNameField.setText(username);

        //set up the recyclerview
        habitsRecyclerView = (RecyclerView) findViewById(R.id.following_habit_list);
        habitsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        //get the user data from the database
        DatabaseManager.getInstance().getUserData(username, new DatabaseManager.OnUserDataListener() {
            @Override
            public void onUserData(User followUser) {

                followUser.getFollowRequests().add(new FollowNotification(LoginManager.getInstance().getCurrentUser().getUsername(), new Date()));

                //get the user's habit from database
                followUser.getHabits(new DatabaseManager.OnHabitsListener() {

                    @Override
                    public void onHabitsSuccess(HashMap<String, Habit> habits) {

                        ArrayList<Habit> habitList = new ArrayList<Habit>(habits.values());
                        ArrayList<String> habitNameList = new ArrayList<String>();

//                        //put all habit name into one list
//                        for (Habit habit : habitList){
//                            habitNameList.add(habit.getName());
//                        }
//
//                        // sort the array
//                        Collections.sort(habitNameList, String.CASE_INSENSITIVE_ORDER);
                        Collections.sort(habitList, new Comparator<Habit>() {
                            public int compare(Habit H1, Habit H2) {
                                return H1.getName().compareTo(H2.getName());
                            }
                        });

                        cAdapt = new FollowingHabitAdapter(habitList , FollowingHabitActivity.this,username);
                        habitsRecyclerView.setAdapter(cAdapt);
                    }

                    @Override
                    public void onHabitsFailed(String message) {

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
