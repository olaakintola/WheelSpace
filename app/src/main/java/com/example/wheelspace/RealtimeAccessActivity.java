package com.example.wheelspace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class RealtimeAccessActivity extends AppCompatActivity {

    private TextView txtOrigin, txtGoingTo;
    private EditText edtTxtOrigin, edtTxtGoingTo;
    private Button btnSearch;
    private ConstraintLayout parentRealTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_access);

        initViews();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSearch();
            }
        });
    }

    private void initSearch() {
        if(validateData() ){
            Toast.makeText(this, "Processing", Toast.LENGTH_SHORT).show();
        }else{
            showSnackBar();
        }
    }

    private void showSnackBar() {
        Snackbar.make(parentRealTime,"Error: Departure and/or Destination has not been Entered", Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtTxtOrigin.setText("");
                        edtTxtGoingTo.setText("");
                    }
                }).show();
    }

    private boolean validateData() {
        if(edtTxtOrigin.getText().toString().equals("")){
            return false;
        }
        if(edtTxtGoingTo.getText().toString().equals("")){
            return false;
        }
        return true;
    }

    private void initViews() {
        txtOrigin = findViewById(R.id.txtOrigin);
        txtGoingTo = findViewById(R.id.txtGoingTo);
        edtTxtOrigin = findViewById(R.id.edtTxtOrigin);
        edtTxtGoingTo = findViewById(R.id.edtTxtGoingTo);
        btnSearch = findViewById(R.id.btnSearch);
        parentRealTime = findViewById(R.id.parentRealTime);
    }
}