package com.team2adevs.izho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    HashMap<String, ArrayList<JSONArray>> day_id = new HashMap<>();
    HashMap<String, Boolean> is_checked = new HashMap<>();

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
                                long time = event.getLong(2);
                                String date = getDay(time * 1000);
                                if(day_id.containsKey(date)){
                                    day_id.get(date).add(event);
                                } else {
                                    ArrayList<JSONArray> tmp = new ArrayList<>();
                                    tmp.add(event);
                                    day_id.put(date, tmp);
                                    is_checked.put(date, false);
                                }
                            }
                            System.out.println(day_id.toString());
                            for(final String key: day_id.keySet()){
                                TextView day = new TextView(MyListWindow.this);
                                day.setText(key);
                                day.setTextSize(24);
                                day.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                day.setTextColor(getResources().getColor(R.color.White));

                                final LinearLayout layout1 = new LinearLayout(MyListWindow.this);
                                layout1.setOrientation(LinearLayout.VERTICAL);


                                layout.addView(day, params);
                                layout.addView(layout1, params);

                                for (final JSONArray i: day_id.get(key)){
                                    Button btn = new Button(MyListWindow.this);
                                    String name = i.getString(1);
                                    String time_start = getTime(i.getLong(2) * 1000);
                                    final int id = i.getInt(0);
                                    btn.setText(name + " " + time_start);
                                    btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                    btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(MyListWindow.this, EventWindow.class);
                                            intent.putExtra("id", id);
                                            startActivity(intent);
                                        }
                                    });
                                    layout1.addView(btn);
                                }
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

}
