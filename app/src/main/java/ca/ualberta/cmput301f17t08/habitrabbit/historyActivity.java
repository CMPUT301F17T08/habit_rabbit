package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by yuxuanzhao on 2017-11-07.
 */

public class historyActivity extends AppCompatActivity {
    //initialize the variables needed in the class
    private ArrayList<HabitEvent> historyList;
    private historyAdapter cAdapt;
    private RecyclerView historyRecyclerView;


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

        cAdapt = new historyAdapter(LoginManager.getInstance().getCurrentUser().getUsername(), historyList);
        historyRecyclerView.setAdapter(cAdapt);

    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
