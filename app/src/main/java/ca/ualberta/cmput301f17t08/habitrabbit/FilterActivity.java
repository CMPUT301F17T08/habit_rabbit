package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Filter activity for filter page, could filter habits by name and comment
 */
public class FilterActivity extends AppCompatActivity {
    private FilterActivity activity = this;

    private TextView title;
    private EditText filter;
    private RecyclerView habit_list_view;
    private Button menu_button;

    private FilterAdapter cAdapt;
    private ArrayList<Habit> habitList;
    private ArrayList<Habit> habitList_display;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //init and create everything for listview
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        menu_button = (Button) findViewById(R.id.menu_button);
        filter = (EditText) findViewById(R.id.filter);
        title = (TextView) findViewById(R.id.title);
        title.setText("FILTER");


        habit_list_view = (RecyclerView) findViewById(R.id.habit_list);
        habitList = LoginManager.getInstance().getCurrentUser().getHabits(); // get the user's habits list that contain all habits
        habit_list_view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        // end of initialization

        habitList_display = new ArrayList<>();  // set the list to display, at the very beginning, it's the copy of the habitlist
        cAdapt = new FilterAdapter(habitList_display, this); // set adapter


        habitList_display.addAll(habitList); // copy the habitlist and display it
        habit_list_view.setAdapter(cAdapt);

        cAdapt.notifyDataSetChanged(); // TODO adapter is not working

        // set menu button
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MenuActivity.class);
                startActivity(intent);
            }
        });
        // end of setting the menu button

        // set the filter edit text listner
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // before the text change, do nothing

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //once user type some letter in the edit text, it will be return to this function as CharSequence s

                habitList_display = new ArrayList<>(); // set the display list as the new empty list
                cAdapt.notifyDataSetChanged(); //TODO adapter is not working
                for (Habit habit : habitList){ // check every habit in habitlist
                    if (habit.getName().contains(s)){ // for every habit, if the name of the habit contains the Char, then add it to the display list
                        habitList_display.add(habit);
                        cAdapt.notifyDataSetChanged();
                    }
                    for (HabitEvent habitevent : habit.getHabitEvents()){
                        if (habitevent.getComment().contains(s) && !habit.getName().contains(s)){ // for every habit event, if the comment contain the char, then dispay it
                            habitList_display.add(habit);
                            cAdapt.notifyDataSetChanged();
                        }
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){ // if nothing entered into the edit text, then display the orginal habitlist
                    habitList_display.addAll(habitList);
                    cAdapt.notifyDataSetChanged();


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





