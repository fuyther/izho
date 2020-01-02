package com.team2adevs.izho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
import java.util.List;

public class ContactsWindow extends AppCompatActivity {

    Toolbar toolbar;
    private MapView mapview;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_window);
        Button toolbar_btn = (findViewById(R.id.toolbar_btn));
        toolbar_btn.setText("Contacts");
        toolbar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TransportFactory.initialize(getApplicationContext());
        mapview = findViewById(R.id.mapview);
        mapview.getMap().move(
                new CameraPosition(new Point(43.231417, 76.917620), 13f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

        if (ContextCompat.checkSelfPermission(ContactsWindow.this, Manifest.permission.ACCESS_FINE_LOCATION)
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
}
