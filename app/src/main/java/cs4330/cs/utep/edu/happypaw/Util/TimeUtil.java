package cs4330.cs.utep.edu.happypaw.Util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {
    private final static String strDateFormat = "yyyy-MM-dd hh:mm:ss";
    private final static DateFormat dateFormat = new SimpleDateFormat(strDateFormat);

    public static String formatElapsedTime(long elapsedTime) {
        int seconds = (int) (elapsedTime / 1000) % 60;
        int minutes = (int) ((elapsedTime / (1000*60)) % 60);
        int hours   = (int) ((elapsedTime / (1000*60*60)) % 24);
        return String.format("%dh:%02dm:%02ds", hours, minutes, seconds);
    }

    public static String currDate2Str(){
        Date date = currDate();
        return dateFormat.format(date);
    }

    public static String date2Str(Date date){
        return dateFormat.format(date);
    }

    public static Date currDate(){
        return Calendar.getInstance().getTime();
    }


    public static Date str2Date(String strDate) {
        Date date = null;
        try{
//            dateFormat.setTimeZone(TimeZone.getTimeZone("MDT"));
            date = dateFormat.parse(strDate);
        }catch (ParseException ex){
            Log.e("Date Parser", ex.getMessage());
        }
        return date;
    }
}
