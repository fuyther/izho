package com.team2adevs.izho;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    private ArrayList<Integer> ids = new ArrayList<>();

    public void append(int id){
        System.out.println(id);
        ids.add(id);
    }

    public ArrayList<Integer> getIds(){
        return ids;
    }

}
