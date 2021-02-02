package com.example.wheelspace;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedbackActivity extends AppCompatActivity {

    private TextView txtRouteFeedback;
    private TextView txtIssueFeedback;
    private TextView txtTimeFeedback;
    private TextView txtDestinationFeedback;
    private TextView txtDepartureFeedback;
    private TextView txtFeedbackDescription;
    private Spinner spinnerRouteFeedback;
    private Spinner spinnerIssueFeedback;
    private Spinner spinnerDepartureFeedback;
    private Spinner spinnerDestinationFeedback;
    private EditText edtTxtTimeFeedback;
    private EditText edtTxtFeedbackDescription;
    Calendar fCalendar;
    int fCurrentHour;
    int fCurrentMinute;
    String amPm;
    TimePickerDialog fTimePickerDialog;

    List<BusTrip> busArray = new ArrayList<>();
    List<String> dublinBusList = new ArrayList<>();
    List<String> dublinStops = new ArrayList<>();
    HashMap<String, String> stopMaps = new HashMap<String, String>();


    String url = "https://gtfsr.transportforireland.ie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initViews();

        populateFeedbackRouteSpinner();

        showTimeDialog();
    }

    private void showTimeDialog() {
        edtTxtTimeFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fCalendar = Calendar.getInstance();
                fCurrentHour = fCalendar.get(Calendar.HOUR_OF_DAY);
                fCurrentMinute = fCalendar.get(Calendar.MINUTE);

                fTimePickerDialog = new TimePickerDialog(FeedbackActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        if(hourOfDay >= 12){
                            amPm = "PM";
                        }else{
                            amPm = "AM";
                        }
                        edtTxtTimeFeedback.setText(String.format("%02d:%02d", hourOfDay, minute) + amPm );
                    }
                }, fCurrentHour, fCurrentMinute, false);
                fTimePickerDialog.show();
            }
        });
    }

    private void populateFeedbackRouteSpinner() {
        Log.d("Test" ,"1");

        // Retrofit Builder
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create() )
                .build();

        // instance for interface
        BusAPICaller busAPICaller = retrofit.create(BusAPICaller.class);
        Call<BusModel> call = busAPICaller.getData();
        Log.d("TEST","2");

        loadBusStops();

        call.enqueue(new Callback<BusModel>() {

            @Override
            public void onResponse(Call<BusModel> call, Response<BusModel> response) {
                Log.d("TEST", "3");

                // Checking for response
                if(response.code() != 200){
                    Toast.makeText(FeedbackActivity.this, "Check Connection", Toast.LENGTH_SHORT).show();
                }

                // GEt the data into textView
                String busResponse = "";

                busArray = response.body().getBusEntities();
                for(int i = 0; i < busArray.size(); i++){   // change to busArray.size  from 10
                    String routeId = busArray.get(i).getTripUpdateDetails().getTripDetails().getRoute_id();
                    String busId = routeId.substring(0, 2);
                    if(busId.equals("60")){
                        busResponse = "Route Id" + routeId.substring(3, 5);
//                        textJson.append(busResponse);
                        String checkNumberInArray = routeId.substring(3, 6);
                        String checkDash = Character.toString(checkNumberInArray.charAt(1));
                        if(checkDash.equals("-")){
                            checkNumberInArray = Character.toString(checkNumberInArray.charAt(0));
                            checkNumberInArray.trim();
                        }

                        if(checkNumberInArray.length() == 3){
                            checkDash = Character.toString(checkNumberInArray.charAt(2));
                            if(checkDash.equals("-")){
                                checkNumberInArray = checkNumberInArray.substring(0, 2);
                            }
                        }

                        if(!(dublinBusList.contains(checkNumberInArray) ) ){
                            dublinBusList.add(checkNumberInArray);
                            Collections.sort(dublinBusList);
                        }
                    }
                }

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(FeedbackActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, dublinBusList);
                spinnerRouteFeedback.setAdapter(spinnerAdapter);
                spinnerRouteFeedback.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String itemValue = parent.getItemAtPosition(position).toString();
                        Toast.makeText(FeedbackActivity.this, itemValue + " Selected!", Toast.LENGTH_SHORT).show();
//                        routeSelected = itemValue;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<BusModel> call, Throwable t) {
                Log.d("Failure", "LAST TEST");
                Log.i("onfailure", "Throwable", t);
            }
        });
    }

    private void loadBusStops() {
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

                dublinStops.add(stopName);
                stopMaps.put(stopName, stopId);
//                dublinStops.add(stopId);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(FeedbackActivity.this,
                android.R.layout.simple_spinner_dropdown_item, dublinStops);
        loadFeedbackDepartureStops(dublinStops, spinnerAdapter);
        loadFeedbackDestinationStops(dublinStops, spinnerAdapter);
    }

    private void loadFeedbackDestinationStops(List<String> dublinStops, ArrayAdapter<String> spinnerAdapter) {
        spinnerDestinationFeedback.setAdapter(spinnerAdapter);
        Log.d("TEST", "13");
        spinnerDestinationFeedback.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = parent.getItemAtPosition(position).toString();
                Toast.makeText(FeedbackActivity.this, itemValue + " Selected!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("TEST", "14");
            }
        });
//        destination = spinnerDestination.getSelectedItem().toString();
    }

    private void loadFeedbackDepartureStops(List<String> dublinStops, ArrayAdapter<String> spinnerAdapter) {
        spinnerDepartureFeedback.setAdapter(spinnerAdapter);
        Log.d("TEST", "13");
        spinnerDepartureFeedback.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = parent.getItemAtPosition(position).toString();
                Toast.makeText(FeedbackActivity.this, itemValue + " Selected!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("TEST", "14");
            }
        });
    }

    private void initViews() {
        txtRouteFeedback = findViewById(R.id.txtRouteFeedback);
        txtIssueFeedback = findViewById(R.id.txtIssueFeedback);
        txtTimeFeedback =findViewById(R.id.txtTimeFeedback);
        txtDestinationFeedback =findViewById(R.id.txtDestinationFeedback);
        txtDepartureFeedback = findViewById(R.id.txtDepartureFeedback);
        txtFeedbackDescription = findViewById(R.id.txtFeedbackDescription);
        spinnerRouteFeedback =findViewById(R.id.spinnerRouteFeedback);
        spinnerIssueFeedback = findViewById(R.id.spinnerIssueFeedback);
        spinnerDepartureFeedback = findViewById(R.id.spinnerDepartureFeedback);
        spinnerDestinationFeedback = findViewById(R.id.spinnerDestinationFeedback);
        edtTxtTimeFeedback =findViewById(R.id.edtTxtTimeFeedback);
        edtTxtFeedbackDescription = findViewById(R.id.edtTxtFeedbackDescription);
    }

}