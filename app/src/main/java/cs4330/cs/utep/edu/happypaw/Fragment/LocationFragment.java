package cs4330.cs.utep.edu.happypaw.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.ArrayList;

import cs4330.cs.utep.edu.happypaw.Model.CustomLocation;
import cs4330.cs.utep.edu.happypaw.Model.Trip;
import cs4330.cs.utep.edu.happypaw.R;
import cs4330.cs.utep.edu.happypaw.ServiceNotification;
import cs4330.cs.utep.edu.happypaw.Services.LocationMonitoringService;
import cs4330.cs.utep.edu.happypaw.TripDBHelper;

import static android.content.Context.LOCATION_SERVICE;


public class LocationFragment extends Fragment implements OnMapReadyCallback {

    MapView mMapView;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private GoogleMap googleMap;
    private boolean mIsServiceStarted = false;;
    public static final String EXTRA_NOTIFICATION_ID = "notification_id";
    public static final String ACTION_STOP = "STOP_ACTION";
    public static final String ACTION_FROM_NOTIFICATION = "isFromNotification";
    private String action;
    private ImageView tripControl;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);

        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.mapContainer, mMapFragment).commit();
        tripControl = rootView.findViewById(R.id.trip_control);
        tripControl.setOnClickListener(this::controlButtonClicked);
        mMapFragment.getMapAsync(this);
        setHasOptionsMenu(true);
        return rootView;
    }

    public void controlButtonClicked(View view){
        mIsServiceStarted = !mIsServiceStarted;
        setControlButton();
        if(mIsServiceStarted){
            startUpdates();
        }
        else{
            endTrip();
        }
    }
    public void setControlButton(){
        tripControl.setImageResource(mIsServiceStarted ? R.drawable.ic_pause_64dp : R.drawable.ic_record_64dp);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        tripControl.bringToFront();
        setControlButton();
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
        if (!success) {
            Log.e("MapERROR", "Style parsing failed");
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MAP","No location");
            return;
        }
        googleMap.setMyLocationEnabled(true);
        setStartingLocation();



    }

    public void startUpdates() {
        step1();

    }

    public void step1() {
        if (isGooglePlayServicesAvailable()) {
            step2(null);
        } else {
            Log.d("MAP", "No google play services");
        }
    }

    public Boolean step2(DialogInterface dialog){
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(activeNetworkInfo == null || !activeNetworkInfo.isConnected()){
            promptInternetConnect();
            return false;
        }
        if(dialog != null){
            dialog.dismiss();
        }

        if(checkPermissions()){
            step3();
        }
        else{
            requestPermissions();
        }
        return true;
    }

    public void step3(){
            Intent intent = new Intent(getActivity(),LocationMonitoringService.class);
            getActivity().startService(intent);



    }

    /**
     * Handles the Stop Updates button, and requests removal of location updates. Does nothing if
     * updates were not previously requested.
     */
    public void stopUpdates() {
        getActivity().stopService(new Intent(getActivity(), LocationMonitoringService.class));

    }

    public void setStartingLocation() {
        int GPSMode = 0;
        LocationManager locManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        try {
            GPSMode = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        String provide = locManager.getBestProvider(criteria, true);
        if (GPSMode == 0) {

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 2);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }

        Location myLoc = locManager.getLastKnownLocation(provide);
        if (myLoc == null) {
            double lat = 29.5528;
            double lon = -95.0932;
            LatLng JSC = new LatLng(lat, lon);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(JSC));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        } else {
            double lat = myLoc.getLatitude();
            double lon = myLoc.getLongitude();
            LatLng latLng = new LatLng(lat, lon);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(2, resultCode, data);
        setStartingLocation();
    }

    /*
        End trip
        Drive mode off, go to summary view activity
     */
    public void endTrip() {
        stopUpdates();
//        TripDBHelper dbHandler = new TripDBHelper(getActivity());
//        int currTripID = dbHandler.getCurrentTripID();
//        ArrayList<CustomLocation> j = dbHandler.getAllLocationsByTrip(currTripID);
//        for(CustomLocation l : j){
//            Log.i("TRIP","" + l.getLatitude());
//        }

    }


    @Override
    public void onResume() {
        super.onResume();

        if (getActivity().getIntent().getAction() != null) {
            action = getActivity().getIntent().getAction();
            if (action.equalsIgnoreCase(ACTION_FROM_NOTIFICATION)) {
                mIsServiceStarted = true;
            }
        }
    }


    public boolean isGooglePlayServicesAvailable(){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(getActivity());
        if(status != ConnectionResult.SUCCESS){
            if(googleApiAvailability.isUserResolvableError(status)){
                googleApiAvailability.getErrorDialog(getActivity(),status,2404).show();
            }
            return false;
        }
        return true;
    }

    public void promptInternetConnect(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("No internet");
        builder.setMessage("Please connect to use");

        String positiveText =  "Refresh";

        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(step2(dialog)){
                            if(checkPermissions()){
                                step3();
                            }
                            else if(!checkPermissions()){
                                requestPermissions();
                            }
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        //dialog.show();
    }

    private boolean checkPermissions(){
        int permissionState1 = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionState2 = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(getActivity(),new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE
        );
    }


}
