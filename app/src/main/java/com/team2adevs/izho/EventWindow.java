package com.team2adevs.izho;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.yandex.mapkit.search.BusinessFilter;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EventWindow extends AppCompatActivity {
    int id;
    TextView tv;
    Button btn_add;
    boolean is_added;
    String from;
    Toolbar toolbar;
    int PERMISSION_TO_WRITE_CALENDAR = 2;

    long callID = 3;

    @Override
    protected void onStop() {
        MyListWindow.initiated = false;
        super.onStop();
    }

    private void setNotification(long time_start, long time_end, long delay, String title, String description) {
        Calendar c =  Calendar.getInstance();
        c.set(Calendar.DAY_OF_YEAR, Integer.valueOf(getDate((time_start + delay) * 1000, "DDD")));
        c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(getDate((time_start + delay) * 1000, "HH")));
        c.set(Calendar.MINUTE, Integer.valueOf(getDate((time_start + delay) * 1000, "mm")));
        c.set(Calendar.YEAR, Integer.valueOf(getDate((time_start + delay) * 1000, "yyyy")));
        startAlarm(c, id);

        requestPermissions(
                new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR},
                PERMISSION_TO_WRITE_CALENDAR);
        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setType("vnd.android.cursor.item/event");
        calIntent.putExtra(Events.TITLE, title);
        calIntent.putExtra(Events.EVENT_LOCATION, "Ask one of the organization team");
        calIntent.putExtra(Events.DESCRIPTION, description);
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, time_end);
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, time_start);
        startActivity(calIntent);

        System.out.println("event inserted");
        // get the event ID that is the last element in the Uri
    }

    void request(final String url, final TextView tv, final Button btn_add){
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response){
                        System.out.println("event window");
                        try {
                            System.out.println(response);
                            is_added = ((MyApplication) getApplication()).getIds().indexOf(id) != -1;
                            final String name =  response.getString(1);
                            Button toolbar_btn = (findViewById(R.id.toolbar_btn));
                            toolbar_btn.setText(name);
                            toolbar_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                            final String text = response.getString(2);
                            final long time = response.getLong(3);
                            final long time_end = response.getLong(4);
                            if(is_added){
                                btn_add.setText("Delete");
                                btn_add.setBackgroundColor(getResources().getColor(R.color.FizmatRed));
                            }
                            tv.setText(text);
                            Typeface tf = ResourcesCompat.getFont(getApplicationContext(), R.font.archive);
                            tv.setTypeface(tf);
                            btn_add.setTypeface(tf);
                            btn_add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    is_added = ((MyApplication) getApplication()).getIds().indexOf(id) != -1;

                                    int resp = ((MyApplication) getApplication()).getIds().indexOf(id);
                                    System.out.println(resp);
                                    if(!is_added) {
                                        PopupMenu popup = new PopupMenu(EventWindow.this, v);
                                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                                            @Override
                                            public boolean onMenuItemClick(MenuItem item){
                                                btn_add.setText("Delete");
                                                btn_add.setBackgroundColor(getResources().getColor(R.color.FizmatRed));

                                                long delay = 0;
                                                switch (item.getItemId()){
                                                    case R.id.now:
                                                        delay = 0;
                                                    case R.id.five:
                                                        delay = 5;
                                                    case R.id.fifteen:
                                                        delay = 15;
                                                    case R.id.thirty:
                                                        delay = 30;
                                                    case R.id.hour:
                                                        delay = 60;
                                                }
                                                setNotification(time, time_end, delay, name, text);
                                                ((MyApplication) getApplication()).append(id);
                                                return true;
                                            }
                                        });
                                        popup.inflate(R.menu.popup);
                                        popup.show();
                                    } else {
                                        btn_add.setText("Add");

                                        btn_add.setBackgroundColor(getResources().getColor(R.color.FizmatLightBlue));
                                        ((MyApplication) getApplication()).delete(id);
                                        deleteAlarm(id);

                                    }
                                }
                            });
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
                try{
                    if(error.networkResponse.statusCode == 200){
                        request(url, tv, btn_add);
                    }
                } catch (NullPointerException e){
                    request(url, tv, btn_add);
                }

            }
        });
                    MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_window);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
            from = extras.getString("from");
        }

        toolbar = findViewById(R.id.toolbar);
        tv = findViewById(R.id.textEvent);
        btn_add = findViewById(R.id.btn_add);


        String url = "http://plony.hopto.org:70/event/" + id;
        request(url, tv, btn_add);

    }

    private String getDate(long time, String pattern){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date netDate = (new Date(time));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }
    private void startAlarm(Calendar c, int id){
        try {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReciever.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);

            alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
        catch(NullPointerException e){
            System.out.println(e.getMessage());
        }
    }

    private void deleteAlarm(int id){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);

        alarmManager.cancel(pendingIntent);
    }
}
