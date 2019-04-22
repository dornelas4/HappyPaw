package cs4330.cs.utep.edu.happypaw.Model;

import com.google.gson.Gson;

import java.io.FileWriter;

public class Scheduler {
    private int mealPerDay;
    private int firstMealHour;
    private int firstMealMinute;
    private int intervalHour;
    private int intervalMinute;

    public Scheduler(int mealPerDay, int firstMealHour, int firstMealMinute, int intervalHour, int intervalMinute) {
        this.mealPerDay = mealPerDay;
        this.firstMealHour = firstMealHour;
        this.firstMealMinute = firstMealMinute;
        this.intervalHour = intervalHour;
        this.intervalMinute = intervalMinute;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
