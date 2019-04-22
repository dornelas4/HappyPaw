package cs4330.cs.utep.edu.happypaw.Model;


import com.google.gson.annotations.SerializedName;


public class FoodContainer {
    @SerializedName("id")
    private int ID;

    @SerializedName("capacity")
    private int capacity;

    @SerializedName("percentage")
    private int percentage;

    public int getPercentage() {
        return percentage;
    }
    public int getCapacity() {
        return capacity;
    }
}
