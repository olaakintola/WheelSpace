package com.example.wheelspace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FeedbackDetailsActivity extends AppCompatActivity {

    private TextView fRoute;
    private TextView fIssue;
    private TextView fDeparture;
    private TextView fDestination;
    private TextView fTime;
    private TextView fDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_details);

        initViews();

        Intent intent = getIntent();

        String fDetailsRoute = intent.getStringExtra("route");
        String fDetailsIssue = intent.getStringExtra("issue");
        String fDetailsDeparture = intent.getStringExtra("departure");
        String fDetailsDestination = intent.getStringExtra("destination");
        String fDetailsTime = intent.getStringExtra("time");
        String fDetailsDescription = intent.getStringExtra("description");

        populateFeedbackDetailsActivity(fDetailsRoute, fDetailsIssue, fDetailsDeparture, fDetailsDestination, fDetailsTime, fDetailsDescription);
    }

    private void populateFeedbackDetailsActivity(String fDetailsRoute, String fDetailsIssue, String fDetailsDeparture, String fDetailsDestination, String fDetailsTime, String fDetailsDescription) {

        fRoute.setText(fDetailsRoute);
        fIssue.setText(fDetailsIssue);
        fDeparture.setText(fDetailsDeparture);
        fDestination.setText(fDetailsDestination);
        fTime.setText(fDetailsTime);
        fDescription.setText(fDetailsDescription);
    }

    private void initViews() {

        fRoute = findViewById(R.id.fRoute);
        fIssue = findViewById(R.id.fIssue);
        fDeparture = findViewById(R.id.fDeparture);
        fDestination = findViewById(R.id.fDestination);
        fTime = findViewById(R.id.fTime);
        fDescription = findViewById(R.id.fDescription);
    }
}