package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class gpsActivity extends AppCompatActivity implements OnMapReadyCallback {


    Button btnShowLocation;

    // GPSTracker class
    gpsTracker gps;

    private gpsActivity activity = this;
    private GoogleMap mainMap;
    LatLng lastKnownLocation;
    ArrayList<HabitEvent> habitEventFeed = new ArrayList<>();
    ArrayList<HabitEvent> habitEventsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        btnShowLocation = (Button) findViewById(R.id.btnGPSShowLocation);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        LoginManager.getInstance().getCurrentUser().getHistory(new DatabaseManager.OnHabitEventsListener() {
            @Override
            public void onHabitEventsSuccess(ArrayMap<String, HabitEvent> habitEvents) {
                habitEventsList = new ArrayList<HabitEvent>(habitEvents.values());
            }

            @Override
            public void onHabitEventsFailed(String message) {
                Log.e("habitEvent_Maps", "Failed to get habit events of user!");

            }


        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        gps = new gpsTracker(gpsActivity.this);
        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            loadMapMarkers();

            LatLng sydney = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(sydney)
                    .title("Current"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }



    }
    //TODO: once refactored version of habitevent is done, fix code so that it can go through all the habit events and mark their location
    public void loadMapMarkers(){

        for(HabitEvent habitEvent: habitEventsList){
            Habit habitEventName =habitEvent.getHabit();


            //https://developers.google.com/maps/documentation/android-api/marker
            //accessed on nov 30/17

             MarkerOptions markerInfo = new MarkerOptions()
                    .position(new LatLng(habitEvent.getLat(), habitEvent.getLng()))
                    .title(habitEventName.getName())
                    .snippet(habitEvent.getComment())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bluedot));

             Marker marker = mainMap.addMarker(markerInfo);
        }


    }
}