package ca.ualberta.cmput301f17t08.habitrabbit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

public class Sortingtest extends AppCompatActivity {

    // TODO for testing soring purpose only, must remove this activity, the xml related to this activity and the list_item xml after the testing is done
    private Sortingtest activity = this;
    private ArrayList<feedtestunit> feedList;
    private ArrayList<feedtestunit> SortedfeedList;
    private ListView sortedlistView;


    private ListView listView;

    private ArrayAdapter<feedtestunit> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sortingtest);
        feedList = new ArrayList<>();
        SortedfeedList = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listview);
        sortedlistView = (ListView) findViewById(R.id.sortedlistview);



        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
        Date now = calendar.getTime();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // create a new calendar
        Calendar cal1 = Calendar.getInstance();

        // print the next time
        Date d1 = cal1.getTime();


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // create a new calendar
        Calendar cal2 = Calendar.getInstance();

        // print the next time
        Date d2 = cal2.getTime();


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // create a new calendar
        Calendar cal3 = Calendar.getInstance();

        // print the next time
        Date d3 = cal3.getTime();

        feedtestunit testunit1 = new feedtestunit("user1",d3);
        feedtestunit testunit2= new feedtestunit("user2",d2);
        feedtestunit testunit3 = new feedtestunit("user3",now);
        feedtestunit testunit4 = new feedtestunit("user4",d1);
        feedList.add(testunit1);
        feedList.add(testunit2);
        feedList.add(testunit3);
        feedList.add(testunit4);


        adapter = new ArrayAdapter<feedtestunit>(this, R.layout.list_item, feedList); // set up the adapter for the list view
        listView.setAdapter(adapter);




        Collections.sort(feedList, new Comparator<feedtestunit>() {
            public int compare(feedtestunit o1, feedtestunit o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });



        adapter = new ArrayAdapter<feedtestunit>(this, R.layout.list_item, feedList); // set up the adapter for the list view
        sortedlistView.setAdapter(adapter);






    }
}
