package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
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
import java.util.HashMap;
import java.util.HashSet;

public class gpsActivity extends AppCompatActivity implements OnMapReadyCallback {


    private Button btnShowLocation;

    // GPSTracker class
    private gpsTracker gps;

    private gpsActivity activity = this;
    private GoogleMap mainMap;
    private ArrayList<HabitEvent> habitEventsList;
    private HashMap<String, HabitEvent> mapList;
    private ArrayList<HabitEvent> mapEventList;
    private ArrayList<HabitEvent> distanceList;
    private Location currentLocation = new Location("");


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
                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();


                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });

    }

    public void showMenu(View v) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mainMap = googleMap;
        mainMap.clear();

        mainMap.setMinZoomPreference(12.0f);
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        gps = new gpsTracker(gpsActivity.this);
        // check if GPS enabled
        if (gps.canGetLocation()) {

            final double latitude = gps.getLatitude();
            final double longitude = gps.getLongitude();

            final Location currentLocation = new Location("");
            currentLocation.setLatitude(latitude);
            currentLocation.setLongitude(longitude);

            LatLng sydney = new LatLng(latitude, longitude);

            mainMap.addMarker(new MarkerOptions().position(sydney)
                    .title("Current"));
            Log.e("habitevent_Maps", "test success");
            mainMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            loadMapMarkers();
            checkDistance();


        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


    }

    public void loadMapMarkers() {
        mainMap.clear();
        if (Global.filter == null) {
            LoginManager.getInstance().getCurrentUser().getHistory(new DatabaseManager.OnHabitEventsListener() {
                @Override
                public void onHabitEventsSuccess(HashMap<String, HabitEvent> habitEvents) {
                    habitEventsList = new ArrayList<HabitEvent>(habitEvents.values());


                    for (final HabitEvent habitEvent : habitEventsList) {
                        habitEvent.getHabit(new DatabaseManager.OnHabitsListener() {
                            @Override
                            public void onHabitsSuccess(HashMap<String, Habit> habits) {
                                //https://developers.google.com/maps/documentation/android-api/marker
                                //accessed on nov 30/17

                                if (habitEvent.getLocation() != null) {


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
        } else {
            LoginManager.getInstance().getCurrentUser().getHabits(new DatabaseManager.OnHabitsListener() {
                @Override
                public void onHabitsSuccess(HashMap<String, Habit> habits) {
                    final Habit selectedHabit = habits.get(Global.filter);
                    selectedHabit.getHabitEvents(new DatabaseManager.OnHabitEventsListener() {
                        @Override
                        public void onHabitEventsSuccess(HashMap<String, HabitEvent> habitEvents) {
                            mapList = habitEvents;
                            mapEventList = new ArrayList<HabitEvent>(mapList.values());

                            for (HabitEvent event : mapEventList) {
                                if (event.getLocation() != null) {

                                    MarkerOptions markerInfo = new MarkerOptions()
                                            .position(new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude()))
                                            .title(selectedHabit.getName())
                                            .snippet(event.getComment())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.reddot));

                                    Marker marker = mainMap.addMarker(markerInfo);

                                }
                            }
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

    public void checkDistance() {
        LoginManager.getInstance().getCurrentUser().getHistory(new DatabaseManager.OnHabitEventsListener() {

            @Override
            public void onHabitEventsSuccess(HashMap<String, HabitEvent> habitEvents) {
                habitEventsList = new ArrayList<HabitEvent>(habitEvents.values());
                gps = new gpsTracker(gpsActivity.this);
                // check if GPS enabled
                if (gps.canGetLocation()) {

                    final double latitude = gps.getLatitude();
                    final double longitude = gps.getLongitude();

                    final Location currentLocation = new Location("");
                    currentLocation.setLatitude(latitude);
                    currentLocation.setLongitude(longitude);
           /* mainMap.addMarker(new MarkerOptions().position(lastKnownLocation)
                    .title("Current"));
            Log.e("habitevent_Maps", "test success");
            mainMap.moveCamera(CameraUpdateFactory.newLatLng(lastKnownLocation));

            loadMapMarkers();*/
                    for (final HabitEvent habitEvent : habitEventsList) {
                        habitEvent.getHabit(new DatabaseManager.OnHabitsListener() {

                            @Override
                            public void onHabitsSuccess(HashMap<String, Habit> habits) {
                                Location habitEventLocation = new Location("");
                                habitEventLocation.setLatitude(habitEvent.getLocation().getLatitude());
                                habitEventLocation.setLongitude(habitEvent.getLocation().getLongitude());

                                // Check the distance between the last known location and the mood event location
                                // using the Location object's distanceTo function.
                                if (currentLocation.distanceTo(habitEventLocation) < 5000.0) {
                                    distanceList.add(habitEvent);
                                    MarkerOptions markerInfo = new MarkerOptions()
                                            .position(new LatLng(habitEvent.getLocation().getLatitude(), habitEvent.getLocation().getLongitude()))
                                            .title(((Habit) habits.values().toArray()[0]).getName())
                                            .snippet("nearby")
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.reddot));

                                    Marker marker = mainMap.addMarker(markerInfo);
                                }
                            }

                            @Override
                            public void onHabitsFailed(String message) {

                            }


                        });
                    }
                }
                else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }

            @Override
            public void onHabitEventsFailed(String message) {

            }
        });
    }
}
