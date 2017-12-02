package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.Activity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class gpsActivity extends Activity {

    Button btnShowLocation;

    // GPSTracker class
    gpsTracker gps;
    private ArrayList<HabitEvent> habitEventsList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        btnShowLocation = (Button) findViewById(R.id.btnGPSShowLocation);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new gpsTracker(gpsActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });
        DatabaseManager.OnHabitEventListener() {
            @Override
            public void onHabitEventsSuccess(ArrayMap<String, HabitEvent> habitEvents) {
                Log.e("Here!", "Here!");

                habitEventsList = new ArrayList<HabitEvent>(habitEvents.values());

            }

            @Override
            public void onHabitEventsFailed(String message) {
                Log.e("habitEvent_Maps", "Failed to get habit events of user!");
            }
        });
    }

}