package com.team2adevs.izho;

import android.app.Application;
import android.text.format.DateFormat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyApplication extends Application {
    private ArrayList<Integer> ids = new ArrayList<>();
    private String day = "";
    private String type = "st";


    public void append(int id){
        System.out.println(id);
        ids.add(id);
    }

    public void setDay(String day){
        this.day = day;
    }

    public String getDay(){
        if (!day.equals("")){
            return day;
        } else {
            return getToday();
        }
    }

    public ArrayList<Integer> getIds(){
        return ids;
    }

    public void setType(String type) { this.type = type; }

    public String getType() { return type; }

    public static String getToday(){
        Date d = new Date();
        if (new Date(2020, 1, 8).getTime() > d.getTime()){
            return "2020-01-08";
        } else if(d.getTime() > new Date(2020, 1, 15).getTime()){
            return "2020-01-15";
        } else {
            return (String) DateFormat.format("yyyy-MM-dd", d.getTime());
        }
    }
}
