package cs4330.cs.utep.edu.happypaw.Services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import cs4330.cs.utep.edu.happypaw.Model.CustomLocation;
import cs4330.cs.utep.edu.happypaw.Model.Trip;
import cs4330.cs.utep.edu.happypaw.TripDBHelper;

public class LocationMonitoringService extends Service implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = LocationMonitoringService.class.getSimpleName();
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();
    public static final String ACTION_LOCATION_BROADCAST = LocationMonitoringService.class.getName() + "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";
    TripDBHelper dbHandler;
    int tripID;
    Trip currTrip;
    long currentTrip;
    Location mCurrentLocation;

    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);

        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
        mLocationRequest.setPriority(priority);
        mLocationClient.connect();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(Bundle dataBundle){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED){
            Log.d(TAG,"== Error onConnected() no permissions");
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient,mLocationRequest,this);
        dbHandler = new TripDBHelper(this);
        tripID = dbHandler.getUniqueID();
        currTrip = new Trip(tripID);
        currentTrip = dbHandler.createTrip(currTrip);
        Log.d(TAG,"COnnected to google api");
    }

    @Override
    public void onConnectionSuspended(int i){
        Log.d(TAG,"Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to Google API");

    }

    @Override
    public void onLocationChanged(Location location) {



        if (location != null) {
            Log.i(TAG, "== location != null");
            addLocationtoDatabase(location);

            //TripDBHelper dbHelper= new TripDBHelper(this);

        }
    }

    @Override
    public void onDestroy(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mLocationClient,this);
        mLocationClient.disconnect();

    }
    public void addLocationtoDatabase(Location location) {

        if (mCurrentLocation == null) {
            mCurrentLocation = location;
            CustomLocation loc = new CustomLocation(location.getLatitude(), location.getLongitude());
            dbHandler.createLocation(loc, currentTrip);
            return;
        } else if ((mCurrentLocation.getLatitude() == location.getLatitude()) && (mCurrentLocation.getLongitude() == location.getLongitude())) {
            return;
        } else {
            CustomLocation loc = new CustomLocation(location.getLatitude(), location.getLongitude());
            long loc_id = dbHandler.createLocation(loc, currentTrip);
            mCurrentLocation = location;
        }
    }

}
