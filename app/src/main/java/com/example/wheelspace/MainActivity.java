package com.example.wheelspace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
    }

    private void intiViews() {
        btnMyStatus = findViewById(R.id.btnMyStatus);
        btnRealtimeAccess = findViewById(R.id.btnRealtimeAccess);
        btnPlanMyJourney = findViewById(R.id.btnPlanMyJourney);
        btnForum = findViewById(R.id.btnForum);
        btnAbout = findViewById(R.id.btnAbout);
    }
}