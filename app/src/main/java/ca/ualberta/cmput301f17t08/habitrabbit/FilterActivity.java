package ca.ualberta.cmput301f17t08.habitrabbit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private HabitsAdapter cAdapt;
    private ArrayList<Habit> habitList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Button menu_button = (Button) findViewById(R.id.menu_button);
        filter = (EditText) findViewById(R.id.filter);
        title = (TextView) findViewById(R.id.title);
        habit_list_view = (RecyclerView) findViewById(R.id.habit_list);

        habitList = LoginManager.getInstance().getCurrentUser().getHabits();

        habit_list_view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        cAdapt = new HabitsAdapter(habitList, this);
        habit_list_view.setAdapter(cAdapt);



    }
}
