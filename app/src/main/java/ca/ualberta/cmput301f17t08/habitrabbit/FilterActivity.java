package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * Filter activity for filter page, could filter habits by name and comment
 */
public class FilterActivity extends AppCompatActivity {

    private FilterActivity activity = this;
    private TextView title;
    private EditText filter;
    private RecyclerView habitListView;
    private Button menuButton;
    private FilterAdapter cAdapt;
    private ArrayList<Habit> habitList;
    private ArrayList<Habit> habitListDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //init and create everything for listview
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        menuButton = (Button) findViewById(R.id.menu_button);
        filter = (EditText) findViewById(R.id.filter);
        title = (TextView) findViewById(R.id.title);
        title.setText("FILTER");

        
        habitListView = (RecyclerView) findViewById(R.id.habit_list);
        habitList = new ArrayList<Habit>();


        habitListView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));



        // habitlist used for displaying
        habitListDisplay = new ArrayList<>();

        // get the user's habits list that contain all habits
        LoginManager.getInstance().getCurrentUser().getHabits(new DatabaseManager.OnHabitsListener() {
            @Override
            public void onHabitsSuccess(ArrayMap<String, Habit> habits) {
                habitList = new ArrayList<Habit>(habits.values());

            }

            @Override
            public void onHabitsFailed(String message) {

            }
        });

        cAdapt = new FilterAdapter(habitList,FilterActivity.this );
        habitListView.setAdapter(cAdapt);

        //before user type in anything to search, display all the habit options


        // set menu button
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MenuActivity.class);
                startActivity(intent);
            }
        });


        // set the filter edit text listner
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // before the text change, do nothing

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //once user type some letter in the edit text, it will be return to this function as CharSequence s

                habitListDisplay.clear(); // set the display list as the new empty list

                for (Habit habit : habitList){ // check every habit in habitlist
                    if (habit.getName().contains(s)){// for every habit, if the name of the habit contains the Char, then add it to the display list
                        habitListDisplay.add(habit);
                    }
                    for (HabitEvent habitevent : habit.getHabitEvents()){
                        if (habitevent.getComment().contains(s)){// for every habit event, if the comment contain the char, then display it
                            if(!habitListDisplay.contains(habit)) {
                                habitListDisplay.add(habit);
                            }
                        }
                    }
                }
                cAdapt.notifyDataSetChanged();
                cAdapt = new FilterAdapter(habitListDisplay,FilterActivity.this );
                habitListView.setAdapter(cAdapt);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){ // if nothing entered into the edit text, then display the orginal habitlist

                    //remove the duplicate element in the filter list
                    Set<Habit> hs = new HashSet<>();
                    hs.addAll(habitList);
                    habitListDisplay.clear();
                    habitListDisplay.addAll(hs);

                    //set up the adapter
                    cAdapt = new FilterAdapter(habitListDisplay,FilterActivity.this );
                    habitListView.setAdapter(cAdapt);
                }
            }
        });
    }

    protected void onRestart() {
        super.onRestart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }

}





