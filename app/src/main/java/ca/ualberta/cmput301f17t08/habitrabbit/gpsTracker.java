package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class gpsTracker extends Service implements LocationListener {

    protected LocationManager locationManager;
    Location location;

    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;

    public gpsTracker(Context context) {
        locationManager = (LocationManager) context
                .getSystemService(LOCATION_SERVICE);
    }

    /**
     * Checks permissions and gets the current location. The current location is stored
     * as latitude and longitude double values in the main model class.
     */
    public void getCurrentLocation() {
        // Get the current location.
        // http://stackoverflow.com/questions/32491960/android-check-permission-for-locationmanager
        // Check if we have proper permissions to get the coarse lastKnownLocation.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.
                ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.i("HABBIT RABBIT", "Requesting coarse permission.");
            // Request the permission.
            // Dummy request code 8 used.
            ActivityCompat.requestPermissions(context,
                    new String[] {android.Manifest.permission.ACCESS_COARSE_LOCATION}, 8);
        }

        // Check if we have proper permissions to get the fine lastKnownLocation.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.
                ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            Log.i("debugMaps","Requesting fine permission");
            // Request the permission.
            // Dummy request code 8 used.
            ActivityCompat.requestPermissions(this,
                    new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, 8);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}