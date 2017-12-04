package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class feedMapActivity extends AppCompatActivity implements OnMapReadyCallback {


    Button btnShowLocation;

    // GPSTracker class
    gpsTracker gps;

    private feedMapActivity activity = this;
    private GoogleMap mainMap;
    LatLng lastKnownLocation;
    ArrayList<HabitEvent> habitEventFeed = new ArrayList<>();
    ArrayList<HabitEvent> habitEventsList;
    public ArrayList<String> followingList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        followingList = LoginManager.getInstance().getCurrentUser().getFollowing();

        btnShowLocation = (Button) findViewById(R.id.btnGPSShowLocation);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new gpsTracker(feedMapActivity.this);

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

    }

    @Override
    public void onMapReady( GoogleMap googleMap) {
        mainMap=googleMap;
        mainMap.clear();
        mainMap.setMinZoomPreference(12.0f);
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        gps = new gpsTracker(feedMapActivity.this);
        // check if GPS enabled
        if(gps.canGetLocation()){

            final double latitude = gps.getLatitude();
            final double longitude = gps.getLongitude();

            LatLng sydney = new LatLng(latitude, longitude);

            mainMap.addMarker(new MarkerOptions().position(sydney)
                    .title("Current"));
            Log.e("habitevent_Maps", "test success");
            mainMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            reloadData();

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }



    }
    public void loadMapMarkers( ){

        LoginManager.getInstance().getCurrentUser().getHistory(new DatabaseManager.OnHabitEventsListener() {
            @Override
            public void onHabitEventsSuccess(HashMap<String, HabitEvent> habitEvents) {
                habitEventsList = new ArrayList<HabitEvent>(habitEvents.values());


                for(final HabitEvent habitEvent: habitEventsList) {
                    habitEvent.getHabit(new DatabaseManager.OnHabitsListener() {
                        @Override
                        public void onHabitsSuccess(HashMap<String, Habit> habits) {
                            //https://developers.google.com/maps/documentation/android-api/marker
                            //accessed on nov 30/17

                            if(habitEvent.getLocation() != null) {
                                MarkerOptions markerInfo = new MarkerOptions()
                                        .position(new LatLng(habitEvent.getLocation().getLatitude(), habitEvent.getLocation().getLongitude()))
                                        .title(((Habit) habits.values().toArray()[0]).getName())
                                        .snippet(habitEvent.getComment())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bluedot));

                                Marker marker = mainMap.addMarker(markerInfo);
                            }

                        }

                        @Override
                        public void onHabitsFailed(String message) {
                            Log.e("habit_Maps", "Failed to get habit  of user!");
                        }

                    });
                }

            }

            @Override
            public void onHabitEventsFailed(String message) {
                Log.e("habitEvent_Maps", "Failed to get habit events of user!");

            }

        });

    }
    private void reloadData(){
        String username;
        final ArrayList<String> usernameList = new ArrayList<String>();

        // get the followers feed, and append them to the feedList
        for(int each = 0; each < followingList.size(); each++){
            username = followingList.get(each);
            usernameList.add(username);
            Log.e("reloadData: ", username);
            DatabaseManager.getInstance().getUserData(username, new DatabaseManager.OnUserDataListener() {
                @Override
                public void onUserData(User user) {
                    final User followingUser = user;
                    followingUser.getHistory(new DatabaseManager.OnHabitEventsListener() {


                        @Override
                        public void onHabitEventsSuccess(HashMap<String, HabitEvent> habitEvents) {
                            habitEventsList = new ArrayList<HabitEvent>(habitEvents.values());
                            gps = new gpsTracker(feedMapActivity.this);
                            if (gps.canGetLocation()) {
                                final double latitude = gps.getLatitude();
                                final double longitude = gps.getLongitude();

                                final Location currentLocation = new Location("");
                                currentLocation.setLatitude(latitude);
                                currentLocation.setLongitude(longitude);
                                LatLng cur = new LatLng(latitude, longitude);

                                mainMap.addMarker(new MarkerOptions().position(cur)
                                        .title("Current"));
                                Log.e("habitevent_Maps", "test success");
                                mainMap.moveCamera(CameraUpdateFactory.newLatLng(cur));
                                for (final HabitEvent habitEvent : habitEventsList) {
                                    habitEvent.getHabit(new DatabaseManager.OnHabitsListener() {

                                        @Override
                                        public void onHabitsSuccess(HashMap<String, Habit> habits) {
                                            if (habitEvent.getLocation() != null) {
                                                Location habitEventLocation = new Location("");
                                                habitEventLocation.setLongitude(habitEvent.getLocation().getLongitude());

                                                habitEventLocation.setLatitude(habitEvent.getLocation().getLatitude());

                                                // Check the distance between the last known location and the mood event location
                                                // using the Location object's distanceTo function.
                                                if (currentLocation.distanceTo(habitEventLocation) < 5000.0) {
                                                    // distanceList.add(habitEvent);
                                                    MarkerOptions markerInfo = new MarkerOptions()
                                                            .position(new LatLng(habitEvent.getLocation().getLatitude(), habitEvent.getLocation().getLongitude()))
                                                            .title(((Habit) habits.values().toArray()[0]).getName())
                                                            .snippet("You are near to this habit event.")
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.reddot));

                                                    Marker marker = mainMap.addMarker(markerInfo);
                                                }
                                                else {
                                                    MarkerOptions markerInfo = new MarkerOptions()
                                                            .position(new LatLng(habitEvent.getLocation().getLatitude(), habitEvent.getLocation().getLongitude()))
                                                            .title(((Habit) habits.values().toArray()[0]).getName())
                                                            .snippet(habitEvent.getComment())
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bluedot));

                                                    Marker marker = mainMap.addMarker(markerInfo);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onHabitsFailed(String message) {
                                            Log.e("habit_Maps", "Failed to get habit  of user!");

                                        }
                                    });
                                }

                            }else {
                                // can't get location
                                // GPS or Network is not enabled
                                // Ask user to enable GPS/network in settings
                                gps.showSettingsAlert();
                            }


                            }

                        @Override
                        public void onHabitEventsFailed(String message) {
                            Log.e("habitEvent_Maps", "Failed to get habit events of user!");

                        }
                    });
                }

                @Override
                public void onUserDataFailed(String message) {
                    Log.e("habitEvent_Maps", "Failed to get data of user!");

                }
            });

        }
    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
