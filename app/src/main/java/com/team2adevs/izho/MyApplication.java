package com.team2adevs.izho;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.format.DateFormat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyApplication extends Application {
    private ArrayList<Integer> ids = reloadIds();
    private String day = reloadDay();
    private String type = reloadType();
    private SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    private SharedPreferences.Editor editor = sharedPreferences.edit();

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String ID = "id";
    public static final String DAY = "day";
    public static final String TYPE = "type";

    private void updateIds(ArrayList ids){
        editor.putString(TYPE, type);
        for(int i = 0; i < ids.size(); i++){
            editor.putInt(ID + (i + 1), (int)ids.get(i));
        }
        editor.apply();
    }

    private void updateDay(String day){
        editor.putString(DAY, day);
        editor.apply();
    }

    private void updateType(String type){
        editor.putString(TYPE, type);
        editor.apply();
    }

    private String reloadDay(){
        try{
            return sharedPreferences.getString(DAY, "");
        } catch (Exception e){
            return "";
        }
    }

    private String reloadType(){
        try{
            return sharedPreferences.getString(TYPE, "st");
        } catch (Exception e){
            return "st";
        }
    }

    private ArrayList<Integer> reloadIds(){
        ArrayList<Integer> ids = new ArrayList<>();
        int counter = 1;
        try{
            while(true){
                int id = sharedPreferences.getInt(ID + counter, -1);
                if (id == -1){
                    break;
                }
                counter++;
                ids.add(id);
            }
        } catch (Exception e){}
        return ids;
    }

    public void append(int id){
        System.out.println(id);
        ids.add(id);
        updateIds(ids);
    }

    public void setDay(String day){
        this.day = day;
        updateDay(day);
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

    public void setType(String type) {
        this.type = type;
        updateType(type);
    }

    public String getType() { return type; }

    public static String getToday(){
        Date d = new Date();
        if (new Date(2020, 1, 8).getTime() > d.getTime()){
            return "2020-01-08";
        } else if(d.getTime() > new Date(2020, 1, 14).getTime()){
            return "2020-01-14";
        } else {
            return (String) DateFormat.format("yyyy-MM-dd", d.getTime());
        }
    }
}
