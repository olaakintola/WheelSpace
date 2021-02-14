package com.example.wheelspace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class RealtimeAccessActivity extends AppCompatActivity {

    private TextView txtOrigin, txtGoingTo;
    private Spinner spinnerOriginRealTime, spinnerGoingToRealTime;
    private Button btnSearch;
    private ConstraintLayout parentRealTime;
    List<String> dublinStops = new ArrayList<>();
    HashMap<String, String> stopMaps = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_access);

        initViews();

        loadBusStops();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSearch();
            }
        });
    }

    private void loadBusStops() {
        ArrayList<String> unsortedDublinStops = new ArrayList<>();
        Log.d("TEST", "10");
        BufferedReader bufferedReader = null;
        String lineFromFile;
        dublinStops.clear();
        dublinStops.add("Choose Stop");
        Log.d("TEST", "11");
        try {
            bufferedReader = new BufferedReader( new InputStreamReader( getAssets().open("stops.txt"), "UTF-8"));
            Log.d("TEST", "12");
            while( (lineFromFile = bufferedReader.readLine() ) != null){
                String[] stopString = lineFromFile.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                String stopName = stopString[1].substring(1, (stopString[1].length()-1 )).trim() ;
//                String stopId = stopString[0].substring(2, (stopString[0].length()-1 )).trim() ;
                String stopId = stopString[0].substring(1, (stopString[0].length()-1 )).trim() ;

                unsortedDublinStops.add(stopName);
                stopMaps.put(stopName, stopId);
//                dublinStops.add(stopId);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(unsortedDublinStops);
//        dublinStops.add(stopName);
        dublinStops.addAll(unsortedDublinStops);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(RealtimeAccessActivity.this,
                android.R.layout.simple_spinner_dropdown_item, dublinStops);

        loadDepartureStops( dublinStops,spinnerAdapter);
        loadDestinationStops( dublinStops, spinnerAdapter);
    }

    private void loadDestinationStops(List<String> dublinStops, ArrayAdapter<String> spinnerAdapter) {

        spinnerGoingToRealTime.setAdapter(spinnerAdapter);
        Log.d("TEST", "13");
        spinnerGoingToRealTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = parent.getItemAtPosition(position).toString();
                Toast.makeText(RealtimeAccessActivity.this, itemValue + " Selected!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("TEST", "14");
            }
        });
    }

    private void loadDepartureStops(List<String> dublinStops, ArrayAdapter<String> spinnerAdapter) {
        spinnerOriginRealTime.setAdapter(spinnerAdapter);
        Log.d("TEST", "13");
        spinnerOriginRealTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = parent.getItemAtPosition(position).toString();
                Toast.makeText(RealtimeAccessActivity.this, itemValue + " Selected!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("TEST", "14");
            }
        });
    }


    private void initSearch() {
        if(validateData() ){
            Toast.makeText(this, "Processing", Toast.LENGTH_SHORT).show();
            String origin = spinnerOriginRealTime.getSelectedItem().toString();
            String goingTo = spinnerGoingToRealTime.getSelectedItem().toString();
            String localTime = getLocalTime();

            Intent intent = new Intent(RealtimeAccessActivity.this, RealtimeResultActivity.class);

            intent.putExtra("originKey", origin);
            intent.putExtra("goingToKey", goingTo);
            intent.putExtra("localTimeKey", localTime);
            startActivity(intent);
       }else{
            showSnackBar();
        }
    }

    private String getLocalTime() {
        String localTime = null;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00") );
        Date currentLocalTime = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm a");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
        localTime  = dateFormat.format(currentLocalTime);

        return localTime;
    }

    private void showSnackBar() {
        Snackbar.make(parentRealTime,"Error: Departure and/or Destination has not been Entered", Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }

    private boolean validateData() {
        if(spinnerOriginRealTime.getSelectedItem().toString().equals("Choose Stop")){
            return false;
        }
        if(spinnerGoingToRealTime.getSelectedItem().toString().equals("Choose Stop")){
            return false;
        }
        return true;
    }

    private void initViews() {
        txtOrigin = findViewById(R.id.txtOrigin);
        txtGoingTo = findViewById(R.id.txtGoingTo);
        spinnerOriginRealTime = findViewById(R.id.spinnerOriginRealTime);
        spinnerGoingToRealTime = findViewById(R.id.spinnerGoingToRealTime);
        btnSearch = findViewById(R.id.btnSearch);
        parentRealTime = findViewById(R.id.parentRealTime);
    }
}