package cs4330.cs.utep.edu.happypaw.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cs4330.cs.utep.edu.happypaw.Model.CustomLocation;
import cs4330.cs.utep.edu.happypaw.Model.Trip;

/**
 * Created by Daniel Ornelas on 6/13/2017.
 */

public class TripDBHelper extends SQLiteOpenHelper {
    //Logcat tag
    private static final String LOG = "DBHelper";
    //Database Version
    private static final int DATABASE_VERSION = 3;
    //Database Name
    private static final String DATABASE_NAME = "tripManager";
    //Table Names
    private static final String TABLE_LOCATIONS = "locations";
    private static final String TABLE_TRIPS = "trips";
    private static final String TABLE_TRIP_LOCATIONS = "trip_locations";

    //Common column names
    public static final String KEY_ID = "_id";

    //Locations table - columns
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_SPEED = "speed";

    //Trip table column names
    public static final String COLUMN_START_DATE = "startDate";
    public static final String COLUMN_END_DATE = "endDate";
    private static final String COLUMN_TRIP_NAME = "tripName";
    //trip locations table - columns
    private static final String KEY_LOCATIONS_ID = "locations_id";
    private static final String KEY_TRIPS_ID = "trips_id";


    //Table create statements
    //Trip table create statements
    private static final String CREATE_TABLE_TRIPS = "CREATE TABLE "
            + TABLE_TRIPS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_TRIP_NAME + " STRING, "
            + COLUMN_START_DATE + " DATETIME"
            + ")";
    //Locations table create statement
    private static final String CREATE_TABLE_LOCATIONS = "CREATE TABLE "
            + TABLE_LOCATIONS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + COLUMN_LATITUDE
            + " DOUBLE," + COLUMN_LONGITUDE + " DOUBLE"  + ")";

    //TRIP_LOCATION table create statement
    private static final String CREATE_TABLE_TRIP_LOCATIONS = "CREATE TABLE "
            + TABLE_TRIP_LOCATIONS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TRIPS_ID + " INTEGER," + KEY_LOCATIONS_ID + " INTEGER"
            + ")";

    //Constructor
    public TripDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
        Create databases
        Only runs once
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating required tables
        db.execSQL(CREATE_TABLE_TRIPS);
        db.execSQL(CREATE_TABLE_LOCATIONS);
        db.execSQL(CREATE_TABLE_TRIP_LOCATIONS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP_LOCATIONS);


        // create new tables
        onCreate(db);
    }

    public void resetDataBase(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP_LOCATIONS);
        this.onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //on downgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP_LOCATIONS);


        // create new tables
        onCreate(db);
    }

    //CRUD OPERATIONS
    /*
        Creating a Location
     */
    public long createLocation(CustomLocation location, long trip_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LATITUDE, location.getLatitude());
        values.put(COLUMN_LONGITUDE, location.getLongitude());


        //insert row
        long location_id = db.insert(TABLE_LOCATIONS, null, values);
        //assigning trip to location
        createLocationTrip(location_id, trip_id);

        return location_id;

    }

    /*
        get a single location
     */
    public CustomLocation getLocation(long location_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_LOCATIONS + " WHERE "
                + KEY_ID + " = " + location_id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
        }
        CustomLocation loc = new CustomLocation();
        loc.setID(c.getInt(c.getColumnIndex(KEY_ID)));
        loc.setLatitude(c.getDouble(c.getColumnIndex(COLUMN_LATITUDE)));
        loc.setLongitude(c.getDouble(c.getColumnIndex(COLUMN_LONGITUDE)));



        return loc;
    }

    /*\
        Getting all locations
     */
    public ArrayList<CustomLocation> getAllLocations() {
        ArrayList<CustomLocation> locations = new ArrayList<CustomLocation>();
        String selectQuery = "SELECT * FROM " + TABLE_LOCATIONS;
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {

            do {
                CustomLocation loc = new CustomLocation();
                loc.setID(c.getInt(c.getColumnIndex(KEY_ID)));
                loc.setLatitude(c.getDouble(c.getColumnIndex(COLUMN_LATITUDE)));
                loc.setLongitude(c.getDouble(c.getColumnIndex(COLUMN_LONGITUDE)));

                //add to list
                locations.add(loc);
            } while (c.moveToNext());
        }

        return locations;
    }

    /*
        Getting all locations under a trip
     */
    public ArrayList<CustomLocation> getAllLocationsByTrip(int trip_id) {
        ArrayList<CustomLocation> locations = new ArrayList<CustomLocation>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOCATIONS + " location, "
                + TABLE_TRIPS + " trip, " + TABLE_TRIP_LOCATIONS + " trip_loc WHERE trip."
                + KEY_ID + " = '" + trip_id + "'" + " AND trip." + KEY_ID
                + " = " + "trip_loc." + KEY_TRIPS_ID + " AND location." + KEY_ID + " = "
                + "trip_loc." + KEY_LOCATIONS_ID;

        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        //looping through all rows adding to list
        if (c.moveToFirst()) {
            do {
                CustomLocation loc = new CustomLocation();
                loc.setID(c.getInt(c.getColumnIndex(KEY_ID)));
                loc.setLatitude(c.getDouble(c.getColumnIndex(COLUMN_LATITUDE)));
                loc.setLongitude(c.getDouble(c.getColumnIndex(COLUMN_LONGITUDE)));

                //add to list
                locations.add(loc);
            } while (c.moveToNext());
        }
        return locations;
    }

    //Update a location
    public int updateLocation(CustomLocation location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LATITUDE, location.getLatitude());
        values.put(COLUMN_LONGITUDE, location.getLongitude());


        //update row
        return db.update(TABLE_LOCATIONS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(location.getID())});
    }

    //Delete a location
    public void deleteLocation(long loc_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCATIONS, KEY_ID + " = ?",
                new String[]{String.valueOf(loc_id)});
    }

    /*
        Creating trip
     */
    public long createTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TRIP_NAME, trip.getTripName());
        values.put(COLUMN_START_DATE, getDateTime());
        //insert
        long trip_id = db.insert(TABLE_TRIPS, null, values);
        return trip_id;
    }

    // Getting all trips
    public ArrayList<Trip> getAllTrips() {
        ArrayList<Trip> trips = new ArrayList<Trip>();
        Cursor c =getAllTeripsCursor();

        if (c.moveToFirst()) {
            do {
                Trip t = new Trip();
                t.setID(c.getInt(c.getColumnIndex(KEY_ID)));
                t.setTripName(c.getString(c.getColumnIndex(COLUMN_TRIP_NAME)));
                t.setTripDate(c.getString(c.getColumnIndex(COLUMN_START_DATE)));

                trips.add(t);
            } while (c.moveToNext());
        }
        return trips;
    }

    public Cursor getAllTeripsCursor(){
        String selectQuery = "SELECT * FROM " + TABLE_TRIPS;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    //Updating trips
    public int updateTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TRIP_NAME, trip.getTripName());


        // updating row
        return db.update(TABLE_TRIPS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(trip.getID())});
    }

    /*
        Deleting a trip
        THis will also delete all locations under the trip
     */
    public void deleteTrip(int id, boolean should_delete_all_trip_locations) {
        SQLiteDatabase db = this.getWritableDatabase();

        // before deleting trip
        // check if locations under this trip should also be deleted
        if (should_delete_all_trip_locations) {
            // get all locations under this trip
            ArrayList<CustomLocation> allLocations = getAllLocationsByTrip(id);

            // delete all todos
            for (CustomLocation loc : allLocations) {
                // delete todo
                deleteLocation(loc.getID());
            }
        }

        // now delete the tag
        db.delete(TABLE_TRIPS, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    //Assign a trip to location
    public long createLocationTrip(long locationID, long tripID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LOCATIONS_ID, locationID);
        values.put(KEY_TRIPS_ID, tripID);


        long id = db.insert(TABLE_TRIP_LOCATIONS, null, values);
        return id;
    }

    //updating a location trip
    public int updateLocationTrip(long id, long tag_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LOCATIONS_ID, tag_id);
        //update row
        return db.update(TABLE_LOCATIONS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    //CLosing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /*
        Get Datetime
     */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public int getUniqueID() {
        ArrayList<Trip> currentTrips = this.getAllTrips();
        int nextTripID = currentTrips.size() + 1;
        return nextTripID;
    }

    public int getCurrentTripID() {
        ArrayList<Trip> currentTrips = this.getAllTrips();
        int nextTripID = currentTrips.size();
        return nextTripID;
    }

}
