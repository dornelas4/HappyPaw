package cs4330.cs.utep.edu.happypaw.Helper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import cs4330.cs.utep.edu.happypaw.Model.VetVisit;
import cs4330.cs.utep.edu.happypaw.Util.TimeUtil;

public class VetVisitDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "vetDB";
    private static final String VET_VISIT_TABLE = "vetVisitTable";

    private static final String KEY_ID = "_id";
    public static final String KEY_DOCTOR = "doctor";
    public static final String KEY_REASON = "reason";
    public static final String KEY_DATE = "date";

    public VetVisitDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + VET_VISIT_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_DOCTOR + " TEXT, "
                + KEY_REASON + " TEXT,"
                + KEY_DATE + " DATE"
                + ")";
        db.execSQL(sql);
    }

    public void onUpgrade(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + VET_VISIT_TABLE);
        onCreate(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + VET_VISIT_TABLE);
        onCreate(db);
    }

    public void addItem(String doctor, String reason, Date date){
        SQLiteDatabase db = this.getWritableDatabase();
        long id;

        ContentValues values = new ContentValues();
        values.put(KEY_DOCTOR, doctor);
        values.put(KEY_REASON, reason);
        values.put(KEY_DATE, TimeUtil.date2Str(date));

        id = db.insert(VET_VISIT_TABLE, null, values);
        db.close();

    }

    public Boolean updateItem(long id, String doctor, String reason, Date date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Boolean success = false;

        ContentValues values = new ContentValues();
        values.put(KEY_DOCTOR, doctor);
        values.put(KEY_REASON, reason);
        values.put(KEY_DATE, TimeUtil.date2Str(date));

        success = db.update(VET_VISIT_TABLE,
                values,
                KEY_ID + " = ?",
                new String[] { Long.toString(id) } ) >= 1;
        db.close();

        return success;
    }

    public Boolean deleteItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Boolean success = db.delete(VET_VISIT_TABLE,
                KEY_ID + " = ?",
                new String[] { Long.toString(id) } ) >= 1;
        db.close();

        return success;
    }

    public Cursor allItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM "+ VET_VISIT_TABLE,null);
    }

}
