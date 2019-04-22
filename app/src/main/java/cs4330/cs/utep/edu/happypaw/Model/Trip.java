package cs4330.cs.utep.edu.happypaw.Model;

public class Trip {
    private int ID;
    private String tripName;
    private String tripDate;

    public Trip(String tripName,int ID){
        this.tripName = tripName;
        this.ID = ID;
    }

    public Trip(){}

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }
}
