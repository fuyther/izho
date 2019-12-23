package com.team2adevs.izho;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.format.DateFormat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyApplication extends Application {
    private ArrayList<Integer> ids = new ArrayList<>();
    private String day = "";
    private String type = "";


    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String ID = "id";
    public static final String DAY = "day";
    public static final String TYPE = "type";

    public void setIds(ArrayList<Integer> ids) {
        this.ids = ids;
    }

    private void updateIds(ArrayList ids){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TYPE, type);
        for(int i = 0; i < ids.size(); i++){
            editor.putInt(ID + (i + 1), (int)ids.get(i));
        }
        editor.apply();
    }

    private void updateDay(String day){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DAY, day);
        editor.apply();
    }

    private void updateType(String type){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TYPE, type);
        editor.apply();
    }

    private String reloadDay(){
        try{
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            return sharedPreferences.getString(DAY, getToday());
        } catch (Exception e){
            return getToday();
        }
    }

    private String reloadType(){
        try{
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            return sharedPreferences.getString(TYPE, "");
        } catch (Exception e){
            return "";
        }
    }

    private ArrayList<Integer> reloadIds(){
        ArrayList<Integer> ids = new ArrayList<>();
        int counter = 1;
        try{
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            while(true){
                int id = sharedPreferences.getInt(ID + counter, -1);
                if (id == -1){
                    break;
                }
                counter++;
                if(!ids.contains(id)){
                    ids.add(id);
                }
            }
        } catch (Exception e){}
        return ids;
    }

    public void append(int id){
        System.out.println(id);
        ids.add(id);
        updateIds(ids);
    }

    public void delete(int id){
        try{
            ids.remove(ids.indexOf(id));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void setDay(String day){
        this.day = day;
        updateDay(day);
    }

    public String getDay(){
        if (day.equals("")){
            try{
                day = reloadDay();
            } catch (Exception e){
                System.out.println(e.getMessage());
                return getToday();
            }
        }
        return day;
    }

    public ArrayList<Integer> getIds(){
        if (ids.equals(new ArrayList<Integer>())){
            try{
                ids = reloadIds();
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return ids;
    }

    public void setType(String type) {
        this.type = type;
        updateType(type);
    }

    public String getType() {
        if (type.equals("")){
            try{
                type = reloadType();
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return type;
    }

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
