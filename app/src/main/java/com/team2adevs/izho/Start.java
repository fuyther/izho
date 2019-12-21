package com.team2adevs.izho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Start extends AppCompatActivity {

    private void switch_view(Class a){
        Intent i = new Intent(Start.this, a);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Button student = findViewById(R.id.student_btn);
        Button team_lead = findViewById(R.id.teamlead_btn);

        if (!((MyApplication)getApplication()).getType().equals("")){
            switch_view(MainWindow.class);
        }
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_view(MainWindow.class);
                ((MyApplication)getApplication()).setType("st");
            }
        });

        team_lead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_view(MainWindow.class);
                ((MyApplication)getApplication()).setType("tl");
            }
        });
    }
}
