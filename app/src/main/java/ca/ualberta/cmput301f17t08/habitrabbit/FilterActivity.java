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
 * Created by zhipengchang on 2017-11-23.
 */
public class FilterActivity extends AppCompatActivity {
    private FilterActivity activity = this;

    private TextView title;
    private EditText filter;
    private RecyclerView habit_list_view;
    private Button menu_button;
    private Button search_button;

    private FilterAdapter cAdapt;
    private ArrayList<Habit> habitList;
    private ArrayList<Habit> habitList_display;
    private ArrayAdapter<String> adapter;
    public ArrayList<String> habit_name_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        menu_button = (Button) findViewById(R.id.menu_button);
        Button search_button = (Button) findViewById(R.id.search_button);
        filter = (EditText) findViewById(R.id.filter);
        title = (TextView) findViewById(R.id.title);
        title.setText("FILTER");


        habit_list_view = (RecyclerView) findViewById(R.id.habit_list);
        habitList = LoginManager.getInstance().getCurrentUser().getHabits();
        habit_list_view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        habitList_display = new ArrayList<>();
        cAdapt = new FilterAdapter(habitList_display, this);
        habit_list_view.setAdapter(cAdapt);


        habitList_display.addAll(habitList);
        cAdapt.notifyDataSetChanged();


        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MenuActivity.class);
                startActivity(intent);
            }
        });


        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                habitList_display = new ArrayList<>();
                cAdapt.notifyDataSetChanged();
                for (Habit habit : habitList){
                    if (habit.getName().contains(s)){
                        habitList_display.add(habit);
                        cAdapt.notifyDataSetChanged();
                    }
                    for (HabitEvent habitevent : habit.getHabitEvents()){
                        if (habitevent.getComment().contains(s)){
                            habitList_display.add(habit);
                            cAdapt.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // do stuff
                if (s.toString().isEmpty()){
                    habitList_display.clear();
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





