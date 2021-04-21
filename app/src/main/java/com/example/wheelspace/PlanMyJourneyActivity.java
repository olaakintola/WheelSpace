package com.example.wheelspace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class PlanMyJourneyActivity extends AppCompatActivity {

    private TextView txtDate, txtLeavingFrom, txtArrivingAt;
    private EditText edtTxtDate, edtTxtFutureDeparture, edtTxtFutureDestination;
    private Button btnFindOffPeak;
    private ConstraintLayout parentPlanMyJourney;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    Spinner spinnerFtrDeparture;
    Spinner spinnerFtrDestination;
    ArrayList<String> dublinStops = new ArrayList<>();
    HashMap<String, String> stopMaps = new HashMap<String, String>();
    BusStopUtility busStopUtility = new BusStopUtility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_my_journey);

        initViews();

        busStopUtility.loadBusStops(stopMaps, this, dublinStops, spinnerFtrDestination, spinnerFtrDeparture);

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

        btnFindOffPeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFindOffPeak();
            }
        });

    }

    private void initFindOffPeak() {
        if(validateData() ){
            Toast.makeText(this, "Processing", Toast.LENGTH_SHORT).show();

            String ftrDeparture = spinnerFtrDeparture.getSelectedItem().toString();
            String ftrDestination = spinnerFtrDestination.getSelectedItem().toString();
            String dateInput = edtTxtDate.getText().toString() ;
            String ftrDate = getWeekDay(dateInput);

            Intent intent = new Intent(PlanMyJourneyActivity.this, OffPeakResultActivity.class);

            intent.putExtra("ftrDepartureKey", ftrDeparture);
            intent.putExtra("ftrDestinationKey", ftrDestination);
            intent.putExtra("ftrDateKey", ftrDate);
            startActivity(intent);

        }else{
            showSnackBar();
        }
    }

    private String getWeekDay(String dateInput) {
        SimpleDateFormat simpleDateFmt = new SimpleDateFormat("dd/MM/yyyy" );
        Date newDate = null;
        try {
            newDate = simpleDateFmt.parse(dateInput);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat dateFormat = new SimpleDateFormat("EEEE");
        String dayOfWeek = dateFormat.format(newDate);
        return dayOfWeek;
    }

    private void showSnackBar() {
        Snackbar.make(parentPlanMyJourney,"Error: Complete All Fields", Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtTxtFutureDestination.setText("");
                        edtTxtFutureDeparture.setText("");
                        edtTxtDate.setText("");
                    }
                }).show();
    }

    private boolean validateData() {
        if(spinnerFtrDeparture.getSelectedItem().toString().equals("")){
            return false;
        }
        if(spinnerFtrDestination.getSelectedItem().toString().equals("")){
            return false;
        }
        if(edtTxtDate.getText().toString().equals("")){
            return false;
        }
        return true;
    }

    private void initViews() {
        txtDate = findViewById(R.id.txtDate);
        txtArrivingAt = findViewById(R.id.txtArrivingAt);
        txtLeavingFrom = findViewById(R.id.txtLeavingFrom);
        edtTxtDate = findViewById(R.id.edtTxtDate);
//        edtTxtFutureDeparture = findViewById(R.id.edtTxtFutureDeparture);
//        edtTxtFutureDestination = findViewById(R.id.edtTxtFutureDestination);
        parentPlanMyJourney = findViewById(R.id.parentPlanMyJourney);
        btnFindOffPeak = findViewById(R.id.btnFindOffPeak);
        spinnerFtrDeparture = findViewById(R.id.spinnerFtrDeparture);
        spinnerFtrDestination = findViewById(R.id.spinnerFtrDestination);
    }
}