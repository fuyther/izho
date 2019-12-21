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

public class MyListWindow extends AppCompatActivity {
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
        JSONObject jsobj = new JSONObject();
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
                                String name = event.getString(1);
                                long time = event.getLong(2);
                                String date = getDate(time * 1000);
                                Button btn_new = new Button(MyListWindow.this);
                                btn_new.setText(name + " " + date);
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
