package cs4330.cs.utep.edu.happypaw.Activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import cs4330.cs.utep.edu.happypaw.Model.CustomLocation;
import cs4330.cs.utep.edu.happypaw.Model.Trip;
import cs4330.cs.utep.edu.happypaw.R;
import cs4330.cs.utep.edu.happypaw.Helper.TripDBHelper;

public class TripSummaryActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<CustomLocation> trip;
    private ArrayList<Trip> allTrips;
    private int tripID;
    private CustomLocation startPoint;
    private CustomLocation endPoint;
    TextView tripDate;
    TextView tripDistance;
    ImageButton goHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_summary);
        tripID = getIntent().getIntExtra("tripID", 1);

        tripDate = (TextView) findViewById(R.id.timetv);
        tripDistance = (TextView) findViewById(R.id.distancetv);
        //  speedAvg = (TextView) findViewById(R.id.speedtv);
        goHome = (ImageButton) findViewById(R.id.homeButton);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHome(v);
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapsum);
        mapFragment.getMapAsync(TripSummaryActivity.this);
    }

    public void goToHome(View v) {
        Intent intent = new Intent();
        intent.setClass(this, HomeActivity.class);
        startActivity(intent);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        String date = " ";
        double speed = 0;
        int i = 0;
        int speedCounter = 0;
        double distance = 0.0;
        mMap = googleMap;
        double meterToMile = 2.24;
        TripDBHelper dbHandler = new TripDBHelper(this);

        trip = dbHandler.getAllLocationsByTrip(tripID);
        allTrips = dbHandler.getAllTrips();

        for (Trip t : allTrips) {
            if (t.getID() == tripID) {
                date = t.getTripDate();
            }
        }

        LatLng start = new LatLng(trip.get(i).getLatitude(), trip.get(i).getLongitude());
        Bitmap paw = BitmapFactory.decodeResource(getResources(),R.drawable.ic_paw_52dp);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
        for (CustomLocation loc : trip) {


            if (i == 0) {
                startPoint = loc;
                mMap.addMarker(new MarkerOptions().position(start).title("Start").snippet
                        ("").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            }
            if (i == 1) {
                mMap.addMarker(new MarkerOptions().position(start).title("Start").snippet
                        ("").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
            if (i == trip.size() - 1) {
                endPoint = loc;
                LatLng nextPosition = new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.addMarker(new MarkerOptions().position(nextPosition).title("End").snippet
                        ("").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            } else {
                LatLng nextPosition = new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.addMarker(new MarkerOptions().position(nextPosition).title("Position" + i).snippet
                        ("").icon(BitmapDescriptorFactory.fromBitmap(paw)));
            }
            i++;
        }

        calculateDistance(startPoint, endPoint);

        tripDate.setText(date);
        // speedAvg.setText(speed + "mph");


    }

    public void calculateDistance(CustomLocation start, CustomLocation end) {
        if (start == null || end == null) {
            tripDistance.setText("0.0");
        } else {


            Location startLoc = new Location("");
            Location endLoc = new Location("");
            startLoc.setLatitude(start.getLatitude());
            startLoc.setLongitude(start.getLongitude());
            endLoc.setLatitude(end.getLatitude());
            endLoc.setLongitude(end.getLongitude());
            double distanceMeters = Math.round(startLoc.distanceTo(endLoc) * 100.0) / 100.0;
            double distanceMiles = Math.round((distanceMeters / 1609.34) * 100.0) / 100.0;

//            if (distanceMiles < 1.0) {
//                tripDistance.setText(distanceMeters + "meters");
//            } else
            tripDistance.setText(distanceMiles + "Mi");
        }
    }
}
