package com.team2adevs.izho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class MyListWindow extends AppCompatActivity {

    HashMap<String, ArrayList<Integer>> day_id = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list_window);

        setTitle("My List");

        final LinearLayout layout = findViewById(R.id.linear_mylist);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        BottomNavigationView bottomnavbar = findViewById(R.id.btmnavbar_ml);
        bottomnavbar.setSelectedItemId(R.id.mylist);
        bottomnavbar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        JSONArray ids = new JSONArray(((MyApplication) getApplication()).getIds());
        String url = "http://plony.hopto.org:70/events";
        final JSONObject jsobj = new JSONObject();
        JSONArray r = new JSONArray();
        try{
            jsobj.put("ids", ids);
            jsobj.put("type", ((MyApplication)getApplication()).getType());
            r.put(jsobj);
        } catch (JSONException e){
            System.out.println(e.getMessage());
        }

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.POST, url, r,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response){
                        System.out.println("main window");
                        try {
                            System.out.println(response);
                            int length = response.length();

                            for(int i=length-1; i > -1; i--){
                                JSONArray event = response.getJSONArray(i);
                                final int id =  event.getInt(0);
                                long time = event.getLong(2);
                                String date = getDay(time * 1000);
                                if(day_id.containsKey(date)){
                                    day_id.get(date).add(id);
                                } else {
                                    ArrayList<Integer> tmp = new ArrayList<>();
                                    tmp.add(id);
                                    day_id.put(date, tmp);
                                }
                            }
                            System.out.println(day_id.toString());
                            for(final String key: day_id.keySet()){
                                Button btn_new = new Button(MyListWindow.this);
                                btn_new.setText(key);
                                btn_new.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                                final LinearLayout layout1 = new LinearLayout(MyListWindow.this);

                                btn_new.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        JSONArray js1 = new JSONArray();
                                        JSONObject jsonObject = new JSONObject();
                                        try{
                                            jsonObject.put("type", ((MyApplication)getApplication()).getType());
                                            jsonObject.put("ids", new JSONArray(day_id.get(key).toArray()));
                                            js1.put(jsonObject);
                                            request("http://plony.hopto.org:70/events", js1, layout1, params);
                                        } catch (JSONException e){
                                            System.out.println(e.getMessage());
                                        }
                                    }
                                });
                                layout.addView(btn_new, params);
                                layout.addView(layout1, params);
                            }
                        }
                        catch(JSONException e){
                            System.out.println(response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error");
                System.out.println(error.getMessage());
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private String getTime(long time){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date netDate = (new Date(time));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

    final String[] days = {"2020-01-08", "2020-01-09", "2020-01-10", "2020-01-11", "2020-01-12", "2020-01-13", "2020-01-14", "2020-01-15"};

    private String getDay(long time){
        try{
            SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
            Date netDate = (new Date(time));
            String day = sdt.format(netDate);
            for (int i = 0; i < days.length; i++){
                if (day.equals(days[i])){
                    return "Day " + (i + 1);
                }
            }
            return "";
        }
        catch(Exception ex){
            return "";
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    Intent intent = new Intent(MyListWindow.this, MainWindow.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.settings:
                    Intent intent1 = new Intent(MyListWindow.this, SettingsWindow.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        }
    };
    void request(final String url, final JSONArray js, final LinearLayout layout, final LinearLayout.LayoutParams params){
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.POST, url,js,
                new Response.Listener<JSONArray>() {
                    @Override

                    public void onResponse(JSONArray response){
                        System.out.println("my list window");
                        try {
                            System.out.println(response);
                            int length = response.length();
                            for(int i=length-1; i > -1; i--){
                                JSONArray event = response.getJSONArray(i);
                                final int id =  event.getInt(0);
                                String name = event.getString(1);
                                long time_start = event.getLong(2);
                                long time_end = event.getLong(3);
                                String date_start = getTime(time_start * 1000);
                                String date_end = getTime(time_end * 1000);
                                Button btn_new = new Button(MyListWindow.this);
                                btn_new.setText(name + " " + date_start + " - " + date_end);
                                btn_new.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                btn_new.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MyListWindow.this, EventWindow.class);
                                        intent.putExtra("id", id);
                                        startActivity(intent);
                                    }
                                });
                                layout.addView(btn_new, params);
                            }
                        }
                        catch(JSONException e){
                            System.out.println(response.toString());
                            System.out.println(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error");
                System.out.println(error.getMessage());
                try{
                    if (error.networkResponse.statusCode == 200){
                        request(url, js, layout, params);
                    }
                } catch (Exception e){
                    request(url, js, layout, params);
                }
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
