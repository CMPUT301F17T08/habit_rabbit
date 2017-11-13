package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class LastCompleteActivity extends AppCompatActivity {
    private ArrayList<HabitEvent> lastCompleteList;//history list from user
    private ArrayList<HabitEvent> lastComplete;//history list that will be passed to the viewAdapter

    private historyAdapter cAdapt;
    private RecyclerView lastCompleteRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_complete_habit);

        String username = LoginManager.getInstance().getCurrentUser().getUsername();
        TextView usernameView = findViewById(R.id.last_complete_username);
        usernameView.setText(username);

        //set up the recyclerView for view
        lastCompleteRecyclerView = (RecyclerView) findViewById(R.id.last_complete_recycle);
        lastCompleteRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        // check if history list from user is empty, if no, then generate the item
        lastCompleteList = LoginManager.getInstance().getCurrentUser().getHistory();
        if (lastCompleteList.size() != 0){
        lastComplete = (ArrayList<HabitEvent>) lastCompleteList.subList(lastCompleteList.size()-1,lastCompleteList.size()-1);}
        cAdapt = new historyAdapter(username,lastComplete);
        lastCompleteRecyclerView.setAdapter(cAdapt);
    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
