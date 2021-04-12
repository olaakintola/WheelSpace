package com.example.wheelspace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class OffPeakResultActivity extends AppCompatActivity {

    String futureDeparture;
    String futureDestination;
    String futureDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_off_peak_result);

        Intent intent = getIntent();

        futureDeparture = intent.getStringExtra("ftrDepartureKey");
        futureDestination = intent.getStringExtra("ftrDestinationKey");
        futureDate = intent.getStringExtra("ftrDateKey");

    }
}