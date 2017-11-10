package ca.ualberta.cmput301f17t08.habitrabbit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class LastCompleteActivity extends AppCompatActivity {
    private ArrayList<HabitEvent> lastCompleteList;
    private feedAdapter cAdapt;
    private RecyclerView lastCompleteRecyclerView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_complete_habit);
        String username = LoginManager.getInstance().getCurrentUser().getUsername();
        TextView usernameView = findViewById(R.id.last_complete_username);
        usernameView.setText(username);
        lastCompleteRecyclerView = (RecyclerView) findViewById(R.id.feed_recycle);
        lastCompleteRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//        lastCompleteList = LoginManager.getInstance().getCurrentUser().get


    }


}
