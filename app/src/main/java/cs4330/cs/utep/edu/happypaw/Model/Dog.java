package cs4330.cs.utep.edu.happypaw.Model;

import com.google.gson.internal.bind.ArrayTypeAdapter;

import java.util.ArrayList;
import java.util.Date;

public class Dog {
    private String name;
    private Date birthday;
    private String gender;
    private String breed;
    private int age;



    public Dog(String name, Date birthday, String gender, String breed) {
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.breed = breed;
    }
}
