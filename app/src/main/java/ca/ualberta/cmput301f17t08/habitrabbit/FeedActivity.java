package ca.ualberta.cmput301f17t08.habitrabbit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by yuxuanzhao on 2017-11-07.
 */

public class FeedActivity extends AppCompatActivity {
    private FeedActivity activity = this;

    private ArrayList<HabitEvent> feedList;
    private feedAdapter cAdapt;
    private RecyclerView feedRecyclerView;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);

        

        feedRecyclerView = (RecyclerView) findViewById(R.id.feed_recycle);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        cAdapt = new feedAdapter(feedList);
        feedRecyclerView.setAdapter(cAdapt);
    }



}
