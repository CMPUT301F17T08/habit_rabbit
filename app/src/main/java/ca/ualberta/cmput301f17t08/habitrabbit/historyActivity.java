package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * The activity for history page
 */

public class historyActivity extends AppCompatActivity {
    //initialize the variables needed in the class
    private HashMap<String, HabitEvent> historyList;
    private ArrayList<HabitEvent> historyListDisplay;
    private HabitEventListAdapter cAdapt;
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

        reloadData();
    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    private void reloadData(){
        //get the current user's history list

        if (Global.filter == null) {
            LoginManager.getInstance().getCurrentUser().getHistory(new DatabaseManager.OnHabitEventsListener() {
                @Override
                public void onHabitEventsSuccess(HashMap<String, HabitEvent> habitEvents) {
                    historyList = habitEvents;
                    historyListDisplay = new ArrayList<HabitEvent>(historyList.values());


                    Collections.sort(historyListDisplay, new Comparator<HabitEvent>() {
                        public int compare(HabitEvent H1, HabitEvent H2) {
                            return H1.getDateCompleted().compareTo(H2.getDateCompleted());
                        }
                    });
                    Collections.reverse(historyListDisplay);


                    cAdapt = new HabitEventListAdapter(historyListDisplay, activity);
                    historyRecyclerView.setAdapter(cAdapt);

                    cAdapt.notifyDataSetChanged();
                }

                @Override
                public void onHabitEventsFailed(String message) {

                }
            });

        }
        else {


            LoginManager.getInstance().getCurrentUser().getHabits(new DatabaseManager.OnHabitsListener() {
                @Override
                public void onHabitsSuccess(HashMap<String, Habit> habits) {
                    Habit selectedHabit = habits.get(Global.filter);
                    selectedHabit.getHabitEvents(new DatabaseManager.OnHabitEventsListener() {
                        @Override
                        public void onHabitEventsSuccess(HashMap<String, HabitEvent> habitEvents) {
                            historyList = habitEvents;
                            historyListDisplay = new ArrayList<HabitEvent>(historyList.values());


                            Collections.sort(historyListDisplay, new Comparator<HabitEvent>() {
                                public int compare(HabitEvent H1, HabitEvent H2) {
                                    return H1.getDateCompleted().compareTo(H2.getDateCompleted());
                                }
                            });

                            Collections.reverse(historyListDisplay);

                            cAdapt = new HabitEventListAdapter(historyListDisplay, activity);
                            historyRecyclerView.setAdapter(cAdapt);

                            cAdapt.notifyDataSetChanged();
                        }

                        @Override
                        public void onHabitEventsFailed(String message) {
                            Log.e("HistoryActivity", "Failed to get habit events for filtered habit!");
                            // TODO: handle this better!
                            finish();
                        }
                    });
                }

                @Override
                public void onHabitsFailed(String message) {
                    Log.e("MyHabitActivity", "Failed to get habits of user!");
                }
            });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        reloadData();
    }
}
