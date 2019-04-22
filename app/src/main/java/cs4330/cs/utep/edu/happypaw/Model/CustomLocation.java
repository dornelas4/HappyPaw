package cs4330.cs.utep.edu.happypaw.Model;

public class CustomLocation {
    private int ID;
    private double latitude;
    private double longitude;

    public CustomLocation(){}

    public CustomLocation(double lat,double lon){
        this.latitude = lat;
        this.longitude = lon;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}
