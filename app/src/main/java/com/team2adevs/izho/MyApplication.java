package com.team2adevs.izho;

import android.app.Application;
import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyApplication extends Application {
    private ArrayList<Integer> ids = new ArrayList<>();
    private String type = "st";


    public void append(int id){
        System.out.println(id);
        ids.add(id);
    }

    public ArrayList<Integer> getIds(){
        return ids;
    }

    public void setType(String type) { this.type = type; }

    public String getType() { return type; }

    public static String getToday(){
        Date d = new Date();
        return (String) DateFormat.format("yyyy-MM-dd", d.getTime());
    }
}
