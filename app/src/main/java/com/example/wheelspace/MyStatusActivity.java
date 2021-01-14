package com.example.wheelspace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class MyStatusActivity extends AppCompatActivity {

    private EditText edtTxtTimePicker;
    private TextView txtRoute, txtDepature, txtDestination, txtStatus;
    private Spinner spinnerRoute, spinnerDepature, spinnerDestination;
    private Button btnSend;
    private RadioGroup rgStatus;
    private RadioButton rbOnBoard, rbGotOff;
    private ConstraintLayout parent;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_status);

        initViews();

        populateSpinner();

        edtTxtTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(MyStatusActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        if(hourOfDay >= 12){
                            amPm = "PM";
                        }else{
                            amPm = "AM";
                        }
                        edtTxtTimePicker.setText(String.format("%02d:%02d", hourOfDay, minute) + amPm );
                    }
                }, currentHour, currentMinute, false);
                timePickerDialog.show();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSend();
            }
        });


    }


    private void initSend() {
        if(validateData() ){
            Toast.makeText(this, "Status Sent", Toast.LENGTH_SHORT).show();
        }else{
            showSnackBar();
        }
    }

    private void showSnackBar() {
        Snackbar.make(parent, "Status Not Sent: Complete All Fields", Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtTxtTimePicker.setText("");
                    }
                }).show();
    }

    private boolean validateData() {
        if(edtTxtTimePicker.getText().toString().equals("")){
            return false;
        }
        return true;
    }

    private void initViews() {
        edtTxtTimePicker = findViewById(R.id.edtTxtTimePicker);
        txtDepature = findViewById(R.id.txtDepature);
        txtDestination = findViewById(R.id.txtDestination);
        txtRoute = findViewById(R.id.txtRoute);
        txtStatus = findViewById(R.id.txtStatus);
        spinnerDepature = findViewById(R.id.spinnerDepature);
        spinnerDestination = findViewById(R.id.spinnerDestination);
        spinnerRoute = findViewById(R.id.spinnerRoute);
        parent = findViewById(R.id.parent);
        rgStatus = findViewById(R.id.rgStatus);
        rbOnBoard = findViewById(R.id.rbOnBoard);
        rbGotOff = findViewById(R.id.rbGotOff);
        btnSend = findViewById(R.id.btnSend);
    }

    private void populateSpinner() {
    }
}