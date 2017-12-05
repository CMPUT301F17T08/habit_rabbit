package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.HashMap;

/**
 * The gps activity for map activity
 */


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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // show location button click event

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
            //checkDistance();


        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


    }
    //load markers onto map
    public void loadMapMarkers() {
        mainMap.clear();
        //checks if filter is on, if null there is no filter
        if (Global.filter == null) {
            LoginManager.getInstance().getCurrentUser().getHistory(new DatabaseManager.OnHabitEventsListener() {
                @Override
                public void onHabitEventsSuccess(HashMap<String, HabitEvent> habitEvents) {
                    habitEventsList = new ArrayList<HabitEvent>(habitEvents.values());
                    gps = new gpsTracker(gpsActivity.this);
                    if (gps.canGetLocation()) {
                        final double latitude = gps.getLatitude();
                        final double longitude = gps.getLongitude();


                        //get current location
                        final Location currentLocation = new Location("");
                        currentLocation.setLatitude(latitude);
                        currentLocation.setLongitude(longitude);
                        LatLng cur = new LatLng(latitude, longitude);

                        //mark current location
                        mainMap.addMarker(new MarkerOptions().position(cur)
                          .title("Current"));
                        Log.e("habitevent_Maps", "test success");
                        mainMap.moveCamera(CameraUpdateFactory.newLatLng(cur));


                        // go through habiteventList and get habit events
                        for (final HabitEvent habitEvent : habitEventsList) {
                            habitEvent.getHabit(new DatabaseManager.OnHabitsListener() {

                                @Override
                                public void onHabitsSuccess(HashMap<String, Habit> habits) {
                                    if (habitEvent.getLocation() != null) {
                                        //get the habitevent location
                                        Location habitEventLocation = new Location("");
                                        habitEventLocation.setLongitude(habitEvent.getLocation().getLongitude());

                                        habitEventLocation.setLatitude(habitEvent.getLocation().getLatitude());

                                        // compare the current location and habit event location to see if it less 5km, if it is mark it as a red dot and change comment to nearby
                                        if (currentLocation.distanceTo(habitEventLocation) < 5000.0) {
                                            // distanceList.add(habitEvent);
                                            MarkerOptions markerInfo = new MarkerOptions()
                                                    .position(new LatLng(habitEvent.getLocation().getLatitude(), habitEvent.getLocation().getLongitude()))
                                                    .title(((Habit) habits.values().toArray()[0]).getName())
                                                    .snippet("You are near to this habit event.")
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.reddot));

                                            Marker marker = mainMap.addMarker(markerInfo);
                                        }
                                        //else the habit event is not in a 5km radius of current location
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
                    Log.e("habitEvent_Maps", "Failed to get habit events of user!");

                }

            });
        }
        //else, filter is on, only mark habit event locations of filtered events
        else {
            LoginManager.getInstance().getCurrentUser().getHabits(new DatabaseManager.OnHabitsListener() {
                @Override
                public void onHabitsSuccess(HashMap<String, Habit> habits) {
                    final Habit selectedHabit = habits.get(Global.filter);
                    selectedHabit.getHabitEvents(new DatabaseManager.OnHabitEventsListener() {
                        @Override
                        public void onHabitEventsSuccess(HashMap<String, HabitEvent> habitEvents) {
                            mapList = habitEvents;
                            mapEventList = new ArrayList<HabitEvent>(mapList.values());
                            gps = new gpsTracker(gpsActivity.this);
                            if(gps.canGetLocation()) {
                                final double latitude = gps.getLatitude();
                                final double longitude = gps.getLongitude();

                                final Location currentLocation = new Location("");
                                currentLocation.setLatitude(latitude);
                                currentLocation.setLongitude(longitude);
                                for (HabitEvent event : mapEventList) {
                                    if (event.getLocation() != null) {

                                        Location habitEventLocation = new Location("");
                                        habitEventLocation.setLongitude(event.getLocation().getLongitude());

                                        habitEventLocation.setLatitude(event.getLocation().getLatitude());

                                        // Check the distance between the last known location and the mood event location
                                        // using the Location object's distanceTo function.
                                        if (currentLocation.distanceTo(habitEventLocation) < 5000.0) {
                                            // distanceList.add(habitEvent);
                                            MarkerOptions markerInfo = new MarkerOptions()
                                                    .position(new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude()))
                                                    .title(selectedHabit.getName())
                                                    .snippet("You are near to this habit event.")
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.reddot));

                                            Marker marker = mainMap.addMarker(markerInfo);
                                        }
                                        else {
                                            MarkerOptions markerInfo = new MarkerOptions()
                                                    .position(new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude()))
                                                    .title(selectedHabit.getName())
                                                    .snippet(event.getComment())
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bluedot));

                                            Marker marker = mainMap.addMarker(markerInfo);
                                        }

                                    }
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
                            Log.e("habitEvent_Maps", "Failed to get habit events of user!");

                        }
                    });
                }

                @Override
                public void onHabitsFailed(String message) {
                    Log.e("habit_Maps", "Failed to get habit  of user!");

                }
            });

        }
    }


}
