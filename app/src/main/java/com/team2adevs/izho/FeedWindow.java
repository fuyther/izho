package com.team2adevs.izho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FeedWindow extends AppCompatActivity {

    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_window);
        BottomNavigationView bottomnavbar = findViewById(R.id.btmnavbar);
        bottomnavbar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ProgressBar pb = new ProgressBar(FeedWindow.this);
        pb.setVisibility(View.VISIBLE);
        layout = findViewById(R.id.linear_feed);
        layout.addView(pb);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    Intent intentf = new Intent(FeedWindow.this, MainWindow.class);
                    startActivity(intentf);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.mylist:
                    Intent intent = new Intent(FeedWindow.this, MyListWindow.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.settings:
                    Intent intent1 = new Intent(FeedWindow.this, SettingsWindow.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        }
    };
}
