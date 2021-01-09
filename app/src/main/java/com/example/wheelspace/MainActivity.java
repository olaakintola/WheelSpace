package com.example.wheelspace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button btnMyStatus, btnRealtimeAccess, btnPlanMyJourney, btnForum, btnAbout;
    private TextView txtAppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intiViews();

        btnMyStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyStatusActivity.class);
                startActivity(intent);
            }
        });

        btnRealtimeAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RealtimeAccessActivity.class);
                startActivity(intent);
            }
        });

        btnPlanMyJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlanMyJourneyActivity.class);
                startActivity(intent);
            }
        });

        btnForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForumActivity.class);
                startActivity(intent);
            }
        });
    }

    private void intiViews() {
        btnMyStatus = findViewById(R.id.btnMyStatus);
        btnRealtimeAccess = findViewById(R.id.btnRealtimeAccess);
        btnPlanMyJourney = findViewById(R.id.btnPlanMyJourney);
        btnForum = findViewById(R.id.btnForum);
        btnAbout = findViewById(R.id.btnAbout);
    }
}