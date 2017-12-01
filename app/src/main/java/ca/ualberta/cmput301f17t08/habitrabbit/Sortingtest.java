package ca.ualberta.cmput301f17t08.habitrabbit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Sortingtest extends AppCompatActivity {

    private ArrayList<feedtestunit> feedList;
    private ListView listView;

    private ArrayAdapter<feedtestunit> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sortingtest);
        feedList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.test);


        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        Date now = calendar.getTime();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // create a new calendar
        Calendar cal2 = Calendar.getInstance();

        // print the next time
        Date d = cal2.getTime();
        feedtestunit testunit1 = new feedtestunit("user1",now);
        feedtestunit testunit2= new feedtestunit("user2",d);
        feedtestunit testunit3 = new feedtestunit("user3",now);
        feedtestunit testunit4 = new feedtestunit("user4",now);
        feedList.add(testunit1);
        feedList.add(testunit2);
        feedList.add(testunit3);
        feedList.add(testunit4);


        adapter = new ArrayAdapter<feedtestunit>(this, R.layout.list_item, feedList); // set up the adapter for the list view
        listView.setAdapter(adapter);





    }
}
