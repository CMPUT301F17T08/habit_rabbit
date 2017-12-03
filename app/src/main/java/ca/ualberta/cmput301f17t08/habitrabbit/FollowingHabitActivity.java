package ca.ualberta.cmput301f17t08.habitrabbit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class FollowingHabitActivity extends AppCompatActivity {

    private RecyclerView habitsRecyclerView;
    private FollowingHabitAdapter cAdapt;
    private TextView userNameField;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.following_habit);
        final String username = getIntent().getStringExtra("FollowingName");
        userNameField = (TextView) findViewById(R.id.following_username);
        userNameField.setText(username);
        habitsRecyclerView = (RecyclerView) findViewById(R.id.following_habit_list);

        habitsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));


        DatabaseManager.getInstance().getUserData(username, new DatabaseManager.OnUserDataListener() {
            @Override
            public void onUserData(User user) {
                User followUser = user;
                followUser.getFollowRequests().add(LoginManager.getInstance().getCurrentUser().getUsername());
                LoginManager.getInstance().getCurrentUser().getHabits(new DatabaseManager.OnHabitsListener() {
                    @Override
                    public void onHabitsSuccess(HashMap<String, Habit> habits) {
                        ArrayList<Habit> habitList = new ArrayList<Habit>(habits.values());

                        ArrayList<String> habitNameList = new ArrayList<String>();

                        for (Habit habit : habitList){
                            habitNameList.add(habit.getName());
                        }
                        cAdapt = new FollowingHabitAdapter(habitNameList , FollowingHabitActivity.this,username);
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
}
