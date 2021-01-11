package com.example.wheelspace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class PlanMyJourneyActivity extends AppCompatActivity {

    private TextView txtDate, txtLeavingFrom, txtArrivingAt;
    private EditText edtTxtDate, edtTxtFutureDeparture, edtTxtFutureDestination;
    private Button btnFindOffPeak;
    private ConstraintLayout parentPlanMyJourney;
    DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_my_journey);

        initViews();

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        edtTxtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        PlanMyJourneyActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        edtTxtDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void initViews() {
        txtDate = findViewById(R.id.txtDate);
        txtArrivingAt = findViewById(R.id.txtArrivingAt);
        txtLeavingFrom = findViewById(R.id.txtLeavingFrom);
        edtTxtDate = findViewById(R.id.edtTxtDate);
        edtTxtFutureDeparture = findViewById(R.id.edtTxtFutureDeparture);
        edtTxtFutureDestination = findViewById(R.id.edtTxtFutureDestination);
        parentPlanMyJourney = findViewById(R.id.parentPlanMyJourney);
    }
}