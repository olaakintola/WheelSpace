package com.example.wheelspace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RealtimeResultActivity extends AppCompatActivity {
    
    String origin;
    String goingTo;
    String localTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_result);

        Intent intent = getIntent();

        origin = intent.getStringExtra("originKey");
        goingTo = intent.getStringExtra("goingToKey");
        localTime = intent.getStringExtra("localTimeKey");
    }

}