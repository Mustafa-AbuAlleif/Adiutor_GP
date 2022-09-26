package com.example.adiutor_app.Entities;

import java.sql.Time;
import java.util.Date;

public class Subject_Data {
   private int ID;
   private String  Name,Day,Place;
   private String  Type;
   private String  time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Subject_Data() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String  getType() {
        return Type;
    }

    public void setType(String  type) {
        Type = type;
    }


}
