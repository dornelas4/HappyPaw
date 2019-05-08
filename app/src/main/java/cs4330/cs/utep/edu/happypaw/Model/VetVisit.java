package cs4330.cs.utep.edu.happypaw.Model;

import java.util.ArrayList;
import java.util.Date;

public class VetVisit {
    private String doctor;
    private Date date;
    private String reason;

    public VetVisit(String doctor, Date date, String reason){
        this.reason = reason;
        this.date = date;
        this.doctor = doctor;
    }
}
