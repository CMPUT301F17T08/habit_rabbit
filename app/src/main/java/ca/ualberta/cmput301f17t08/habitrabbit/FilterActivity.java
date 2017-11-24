package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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

    private TextView title;
    private EditText filter;
    private RecyclerView habit_list_view;
    private Button menu_button;
    private Button search_button;

    private HabitsAdapter cAdapt;
    private ArrayList<Habit> habitList;
    private FilterActivity activity = this;
    private ArrayAdapter<Habit> adapter;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Button menu_button = (Button) findViewById(R.id.menu_button);
        Button search_button = (Button) findViewById(R.id.search_button);

        filter = (EditText) findViewById(R.id.filter);
        title = (TextView) findViewById(R.id.title);
        habit_list_view = (RecyclerView) findViewById(R.id.habit_list);

        habitList = LoginManager.getInstance().getCurrentUser().getHabits();
        habit_list_view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        cAdapt = new HabitsAdapter(habitList, this);
        habit_list_view.setAdapter(cAdapt);


        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MenuActivity.class);
                startActivity(intent);
            }
        });

        final String keywordstring = filter.getText().toString();// get the comment string


        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Habit> result = new ArrayList<>();
                for(Habit habit :habitList) {
                    if (habit.getName() == keywordstring) {
                        result.add(habit);
                    }
                }
            }
        });





    }




}





