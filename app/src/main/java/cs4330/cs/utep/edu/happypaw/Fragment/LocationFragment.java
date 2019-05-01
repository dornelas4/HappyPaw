package cs4330.cs.utep.edu.happypaw.Fragment;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import cs4330.cs.utep.edu.happypaw.R;
import cs4330.cs.utep.edu.happypaw.ServiceNotification;
import cs4330.cs.utep.edu.happypaw.Services.BackgroundLocationService;
import cs4330.cs.utep.edu.happypaw.Services.LocationTrackerService;
import cs4330.cs.utep.edu.happypaw.TripDBHelper;

import static android.content.Context.LOCATION_SERVICE;


public class LocationFragment extends Fragment implements OnMapReadyCallback {

    MapView mMapView;
    private GoogleMap googleMap;
    private boolean mIsServiceStarted = false;
    private int notifID;
    public static final String EXTRA_NOTIFICATION_ID = "notification_id";
    public static final String ACTION_STOP = "STOP_ACTION";
    public static final String ACTION_FROM_NOTIFICATION = "isFromNotification";
    private String action;
    private Location utep;
    private ImageView tripControl;
    private BackgroundLocationService gpsService;

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

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        googleMap.addMarker(new MarkerOptions()
//                .position(test)
//                .title("title"));

        boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
        if (!success) {
            Log.e("MapERROR", "Style parsing failed");
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        googleMap.setMyLocationEnabled(true);
        setStartingLocation();


    }

    public void startUpdates() {
        if (!mIsServiceStarted) {
            mIsServiceStarted = true;

//            OnGoingLocationNotification(getActivity());
            Intent intent = new Intent(this.getActivity().getApplication(),BackgroundLocationService.class);
            this.getActivity().getApplication().startService(intent);
            this.getActivity().getApplication().bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE);
            gpsService.startTracking();

        }
    }

    /**
     * Handles the Stop Updates button, and requests removal of location updates. Does nothing if
     * updates were not previously requested.
     */
    public void stopUpdates() {
        if (mIsServiceStarted) {
            mIsServiceStarted = false;

            //cancelNotification(getActivity(), notifID);
            gpsService.stopTracking();
            //getActivity().stopService(new Intent(getActivity(), LocationTrackerService.class));
        }
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
        TripDBHelper dbHandler = new TripDBHelper(getActivity());
        int currTripID = dbHandler.getCurrentTripID();


//
//        Intent intent = new Intent(getBaseContext(), MapSummary.class);
//        intent.putExtra("tripID", currTripID);
//        startActivity(intent);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            String c = name.getClassName();
            if(c.endsWith("BackgroundLocationService")){
                gpsService=((BackgroundLocationService.LocationServiceBinder) service).getService();

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if(name.getClassName().equals("BackgroundLocationService")){
                gpsService = null;
            }
        }
    };
    /**
     * Method to generate OnGoingLocationNotification
     *
     * @param mcontext
     */
    public static void OnGoingLocationNotification(Context mcontext) {
        int mNotificationId;

        mNotificationId = (int) System.currentTimeMillis();

        //Broadcast receiver to handle the stop action
        Intent mstopReceive = new Intent(mcontext, ServiceNotification.class);
        mstopReceive.putExtra(EXTRA_NOTIFICATION_ID, mNotificationId);
        mstopReceive.setAction(ACTION_STOP);
        PendingIntent pendingIntentStopService = PendingIntent.getBroadcast(mcontext, (int) System.currentTimeMillis(), mstopReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mcontext)
                        .setSound(alarmSound)
                        .setSmallIcon(R.drawable.ic_cast_off_light)
                        .setContentTitle("Location Service")
                        .addAction(R.drawable.ic_cancel, "Stop Service", pendingIntentStopService)
                        .setOngoing(true).setContentText("Running...");
        mBuilder.setAutoCancel(false);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(mcontext, LocationFragment.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.setAction(ACTION_FROM_NOTIFICATION);
        resultIntent.putExtra(EXTRA_NOTIFICATION_ID, mNotificationId);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mcontext, (int) System.currentTimeMillis(), resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.cancel(mNotificationId);

        Notification mNotification = mBuilder.build();
        mNotification.defaults |= Notification.DEFAULT_VIBRATE;
        mNotification.flags |= Notification.FLAG_ONGOING_EVENT;

        mNotificationManager.notify(mNotificationId, mNotification);

    }

    private void cancelNotification(Context mContext, int mnotinotifId) {
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(mnotinotifId);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (getActivity().getIntent().getAction() != null) {
            action = getActivity().getIntent().getAction();
            notifID = getActivity().getIntent().getIntExtra(EXTRA_NOTIFICATION_ID, 0);
            if (action.equalsIgnoreCase(ACTION_FROM_NOTIFICATION)) {
                mIsServiceStarted = true;


            }
        }
    }



}
