package com.team2adevs.izho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

public class SettingsWindow extends AppCompatActivity {
    private MapView mapview;
    private void change(Button bt, String color){
        bt.setBackgroundColor(Color.parseColor(color));
    }
    private void activate(Button bt){
        change(bt, "#40E648");
    }
    private void deactive(Button bt){
        change(bt, "#d3d3d3");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey("cde76994-d9dd-4255-96ad-830cf03f240f");
        MapKitFactory.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_window);
        setTitle("Settings");

        mapview = findViewById(R.id.mapview);
        BottomNavigationView bottomnavbar = findViewById(R.id.btmnavbar_st);
        final Button st = findViewById(R.id.student_btn_set);
        final Button tl = findViewById(R.id.teamlead_btn_set);
        String type = ((MyApplication)getApplication()).getType();
        if(type.equals("st")){
            activate(st);
        }
        else if(type.equals("tl")){
            activate(tl);
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

        mapview.getMap().move(
                new CameraPosition(new Point(43.231417, 76.917620), 13f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

        bottomnavbar.setSelectedItemId(R.id.settings);
        bottomnavbar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapview.onStop();
        MapKitFactory.getInstance().onStop();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mapview.onStart();
        MapKitFactory.getInstance().onStart();
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
