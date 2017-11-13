package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Feed activity in the main menu page
 */

public class FeedActivity extends AppCompatActivity {
    //initialize the variables needed in the class
    private ArrayList<HabitEvent> feedList;
    private feedAdapter cAdapt;
    private RecyclerView feedRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // get the element from the Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);
        feedRecyclerView = (RecyclerView) findViewById(R.id.feed_recycle);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        //get the current user's feed list
        feedList = LoginManager.getInstance().getCurrentUser().getHistory();
        // set up the adapter
        cAdapt = new feedAdapter(LoginManager.getInstance().getCurrentUser().getUsername(),feedList);
        feedRecyclerView.setAdapter(cAdapt);
    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
