package ca.ualberta.cmput301f17t08.habitrabbit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by yuxuanzhao on 2017-11-07.
 */

public class FeedActivity extends AppCompatActivity {


    private ArrayList<HabitEvent> feedList;
    private feedAdapter cAdapt;
    private RecyclerView feedRecyclerView;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);



        feedRecyclerView = (RecyclerView) findViewById(R.id.feed_recycle);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        feedList = LoginManager.getInstance().getCurrentUser().getHistory();

        ArrayList<Integer> frequency = new ArrayList<Integer>(Arrays.asList(new Integer[]{1,0,1,0,1,0,1}));
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        Date now = calendar.getTime();

        for(int counter=0; counter < 2; counter++){
            feedList.add(counter,new HabitEvent(new Habit("singing","hdkhfajk",now,frequency),now,"what the hell",null,null));
        }
        cAdapt = new feedAdapter(LoginManager.getInstance().getCurrentUser().getUsername(),feedList);
        feedRecyclerView.setAdapter(cAdapt);


    }



}
