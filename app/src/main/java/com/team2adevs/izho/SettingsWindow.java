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
    private MapView mapview;
    Toolbar toolbar;
    private void change(Button bt, String color){
        bt.setBackgroundColor(Color.parseColor(color));
    }
    private void activate(Button bt){
        change(bt, "#40E648");
    }
    private void deactive(Button bt){ change(bt, "#053f76");}
    final int PERMISSION_ID = 12;

    private void routeToSchool(double longitude, double latitude){
        Transport transport = TransportFactory.getInstance();
        PedestrianRouter router = transport.createPedestrianRouter();
        ArrayList<RequestPoint> requests = new ArrayList<>();
        requests.add(new RequestPoint(new Point(latitude, longitude), RequestPointType.WAYPOINT, ""));
        requests.add(new RequestPoint(new Point(43.231417, 76.917620), RequestPointType.WAYPOINT, ""));
        router.requestRoutes(requests, new TimeOptions(), new Session.RouteListener() {
            @Override
            public void onMasstransitRoutes(@NonNull List<Route> list) {
                System.out.println(list.toString());
                if(!list.isEmpty()){
                    mapview.getMap().getMapObjects().clear();
                }
                for(Route route: list){
                    mapview.getMap().getMapObjects().addPolyline(list.get(0).getGeometry());
                }
            }

            @Override
            public void onMasstransitRoutesError(@NonNull Error error) {
                System.out.println(error);
            }
        });
    }


    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            try{
                routeToSchool(location.getLongitude(), location.getLatitude());
            }catch (NullPointerException e){
                System.out.println("GPS is off");
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey("cde76994-d9dd-4255-96ad-830cf03f240f");
        MapKitFactory.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_window);
        toolbar = findViewById(R.id.toolbar_set);
        toolbar.setTitle("Settings");
        TransportFactory.initialize(getApplicationContext());
        mapview = findViewById(R.id.mapview);
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

        mapview.getMap().move(
                new CameraPosition(new Point(43.231417, 76.917620), 13f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

        if (ContextCompat.checkSelfPermission(SettingsWindow.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
        }
        mapview.getMap().getMapObjects().addPlacemark(new Point(43.231417, 76.917620));
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try{
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            routeToSchool(location.getLongitude(), location.getLatitude());
        }catch (NullPointerException e){
            System.out.println("GPS is off");
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

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
