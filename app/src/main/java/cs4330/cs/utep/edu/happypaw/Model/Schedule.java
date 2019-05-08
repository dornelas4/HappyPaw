package cs4330.cs.utep.edu.happypaw.Model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;


public class Schedule {
    @SerializedName("meal_per_day")
    private int mealPerDay;

    @SerializedName("first_meal_hour")
    private int firstMealHour;

    @SerializedName("first_meal_minute")
    private int firstMealMinute;

    @SerializedName("interval_hour")
    private int intervalHour;

    @SerializedName("interval_minute")
    private int intervalMinute;

    @SerializedName("next_feed_time")
    private long nextFeedTime;

    public Schedule(int mealPerDay, int firstMealHour, int firstMealMinute,
                    int intervalHour, int intervalMinute) {
        this.mealPerDay = mealPerDay;
        this.firstMealHour = firstMealHour;
        this.firstMealMinute = firstMealMinute;
        this.intervalHour = intervalHour;
        this.intervalMinute = intervalMinute;
    }

    public long getNextFeedTime(){
        return nextFeedTime;
    }

    public int getMealPerDay(){ return mealPerDay; }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
