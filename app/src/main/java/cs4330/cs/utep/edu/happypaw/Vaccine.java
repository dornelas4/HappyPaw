package cs4330.cs.utep.edu.happypaw;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Vaccine {
    private String vaccineName;
    private Date date;
    public static List<Vaccine> ITEMS;

    public Vaccine(){

    }



    public Vaccine(String vaccineName,Date date){
        if(ITEMS == null){
            ITEMS = new ArrayList<Vaccine>();
        }
        this.vaccineName = vaccineName;
        this.date = date;
    }

    public void setVaccineName(String vaccineName){
        this.vaccineName = vaccineName;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public Date getDate(){
        return this.date;
    }

    public String getVaccineName() {
        return this.vaccineName;
    }
}
