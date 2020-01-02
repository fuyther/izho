package com.team2adevs.izho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.transport.Transport;
import com.yandex.mapkit.transport.TransportFactory;
import com.yandex.mapkit.transport.masstransit.PedestrianRouter;
import com.yandex.mapkit.transport.masstransit.Route;
import com.yandex.mapkit.transport.masstransit.Session;
import com.yandex.mapkit.transport.masstransit.TimeOptions;
import com.yandex.runtime.Error;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsWindow extends AppCompatActivity {
    Toolbar toolbar;
    private void change(Button bt, String color){
        bt.setBackgroundColor(Color.parseColor(color));
    }
    private void activate(Button bt){
        change(bt, "#40E648");
    }
    private void deactive(Button bt){ change(bt, "#053f76");}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey("cde76994-d9dd-4255-96ad-830cf03f240f");
        MapKitFactory.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_window);
        toolbar = findViewById(R.id.toolbar_set);
        toolbar.setTitle("Settings");
        Button contacts = findViewById(R.id.contacts_btn);
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsWindow.this, ContactsWindow.class);
                startActivity(intent);
            }
        });
        Button about = findViewById(R.id.about_btn);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsWindow.this, AboutWindow.class);
                startActivity(intent);

            }
        });

        BottomNavigationView bottomnavbar = findViewById(R.id.btmnavbar_st);
        final Button st = findViewById(R.id.student_btn_set);
        Typeface tf = ResourcesCompat.getFont(getApplicationContext(), R.font.archive);
        st.setTypeface(tf);
        final Button tl = findViewById(R.id.teamlead_btn_set);
        tl.setTypeface(tf);
        String type = ((MyApplication)getApplication()).getType();
        if(type.equals("st")){
            activate(st);
            deactive(tl);
        }
        else if(type.equals("tl")){
            activate(tl);
            deactive(st);
        }
        st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyApplication)getApplication()).setType("st");
                activate(st);
                deactive(tl);
            }
        });
        tl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyApplication)getApplication()).setType("tl");
                activate(tl);
                deactive(st);
            }
        });


        bottomnavbar.setSelectedItemId(R.id.settings);
        bottomnavbar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    Intent intent = new Intent(SettingsWindow.this, MainWindow.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.mylist:
                    Intent intent1 = new Intent(SettingsWindow.this, MyListWindow.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        }
    };
}
