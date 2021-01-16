package com.example.wheelspace;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
//                Intent intent = new Intent(MainActivity.this, ForumActivity.class);
                Intent intent = new Intent(MainActivity.this, ForumActivity.class);
                startActivity(intent);
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getString(R.string.app_name));
                builder.setMessage("This is a smartphone application that allows wheelchair " +
                        "users to monitor, using crowdsourcing, the real-time occupancy/availability" +
                        " of wheelchair bays on specific public transport bus routes.");
                builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
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