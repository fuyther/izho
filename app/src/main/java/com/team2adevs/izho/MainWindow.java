package com.team2adevs.izho;

import android.app.ActionBar;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainWindow extends AppCompatActivity {
    String type;
    String day;


    final String[] days = {"2020-01-08", "2020-01-09", "2020-01-10", "2020-01-11", "2020-01-12", "2020-01-13", "2020-01-14", "2020-01-15"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);
        type = ((MyApplication)getApplication()).getType();
        day = ((MyApplication) getApplication()).getDay();
        final LinearLayout layout = findViewById(R.id.linear_l);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        BottomNavigationView bottomnavbar = findViewById(R.id.btmnavbar);
        bottomnavbar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setTitle("Home");
        String url = "http://plony.hopto.org:70/list_main";
        JSONArray js = new JSONArray();
        JSONObject jsobj = new JSONObject();
        try{
            jsobj.put("type", type);
            jsobj.put("day", day);
            js.put(jsobj);
        }
        catch (JSONException e) {
            System.out.println(e.getMessage());
        }
        request(url, js, layout, params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.days, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.day_1:
                System.out.println("Day 1");
                ((MyApplication)getApplication()).setDay(days[0]);
                Intent intent = new Intent(MainWindow.this, MainWindow.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            case R.id.day_2:
                System.out.println("Day 2");
                ((MyApplication)getApplication()).setDay(days[1]);
                Intent intent1 = new Intent(MainWindow.this, MainWindow.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            case R.id.day_3:
                System.out.println("Day 3");
                ((MyApplication)getApplication()).setDay(days[2]);
                Intent intent2 = new Intent(MainWindow.this, MainWindow.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            case R.id.day_4:
                System.out.println("Day 4");
                ((MyApplication)getApplication()).setDay(days[3]);
                Intent intent3 = new Intent(MainWindow.this, MainWindow.class);
                startActivity(intent3);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            case R.id.day_5:
                System.out.println("Day 5");
                ((MyApplication)getApplication()).setDay(days[4]);
                Intent intent4 = new Intent(MainWindow.this, MainWindow.class);
                startActivity(intent4);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            case R.id.day_6:
                System.out.println("Day 6");
                ((MyApplication)getApplication()).setDay(days[5]);
                Intent intent5 = new Intent(MainWindow.this, MainWindow.class);
                startActivity(intent5);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            case R.id.day_7:
                System.out.println("Day 7");
                ((MyApplication)getApplication()).setDay(days[6]);
                Intent intent6 = new Intent(MainWindow.this, MainWindow.class);
                startActivity(intent6);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.mylist:
                    Intent intent = new Intent(MainWindow.this, MyListWindow.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.settings:
                    Intent intent1 = new Intent(MainWindow.this, SettingsWindow.class);
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
                        System.out.println("main window");
                        try {
                            System.out.println(response);
                            int length = response.length();
                            for(int i=length-1; i > -1; i--){
                                JSONArray event = response.getJSONArray(i);
                                final int id =  event.getInt(0);
                                String name = event.getString(1);
                                long time_start = event.getLong(2);
                                long time_end = event.getLong(3);
                                String date_start = getDate(time_start * 1000);
                                String date_end = getDate(time_end * 1000);
                                Button btn_new = new Button(MainWindow.this);
                                btn_new.setText(name + " " + date_start + " - " + date_end);
                                btn_new.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                btn_new.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainWindow.this, EventWindow.class);
                                        intent.putExtra("id", id);
                                        startActivity(intent);
                                    }
                                });
                                layout.addView(btn_new, params);
                            }
                            Button btn_type = new Button(MainWindow.this);
                            btn_type.setText(type);
                            btn_type.setTextColor(getResources().getColor(R.color.FizmatRed));
                            btn_type.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            layout.addView(btn_type, params);
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
    private String getDate(long time){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date netDate = (new Date(time));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }
    }
