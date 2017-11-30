package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.sql.BatchUpdateException;
import java.util.ArrayList;

/**
 * The activity for history page
 */

public class historyActivity extends AppCompatActivity {
    //initialize the variables needed in the class
    private ArrayList<HabitEvent> historyList;
    private historyAdapter cAdapt;
    private RecyclerView historyRecyclerView;
    private Button filter_button;
    private historyActivity activity = this;
    private Button map_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // get the element from the Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        historyRecyclerView = (RecyclerView) findViewById(R.id.recycle);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        //get the current user's history list
        historyList = LoginManager.getInstance().getCurrentUser().getHistory();
        // set up the adapter

        cAdapt = new historyAdapter(LoginManager.getInstance().getCurrentUser().getUsername(), historyList,this);
        historyRecyclerView.setAdapter(cAdapt);



        filter_button = (Button) findViewById(R.id.filter_button);
        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FilterActivity.class);
                startActivity(intent);
            }
        });
        map_button = (Button) findViewById(R.id.map_button);
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, gpsActivity.class);
                startActivity(intent);
            }
        });


    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }
}
