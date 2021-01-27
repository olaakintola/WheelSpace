package com.example.wheelspace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

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

public class MyStatusActivity extends AppCompatActivity {

    // solve the crashing as a result of generateid function
    // explore app with room
    // explore reading text file into room
    // creating restapi and/or google firebase - read report
    // get the various data that user has selected and display it in snackbar
    // machine learning variables
    private EditText edtTxtTimePicker;
    private TextView txtRoute, txtDepature, txtDestination, txtStatus;
    private Spinner spinnerRoute, spinnerDepature, spinnerDestination;
    private Button btnSend;
    private RadioGroup rgStatus;
//    private RadioButton rbOnBoard, rbGotOff;
    private RadioButton statusRadioBtn;
    private ConstraintLayout parent;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;
    String routeString;
    String routeSelected;
    List<BusTrip> busArray = new ArrayList<>();
    List<String> dublinBusList = new ArrayList<>();
    List<String> dublinStops = new ArrayList<>();
    HashMap<String, String> stopMaps = new HashMap<String, String>();

    String url = "https://gtfsr.transportforireland.ie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_status);

        initViews();

        populateSpinner();

/*
        Selecting and Displaying Time in correct format
*/
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

/*
        Action takes place when Send Button is clicked
*/
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSend();
//                String departingStop = spinnerDepature.getSelectedItem().toString();
//                routeSelected = spinnerRoute.getSelectedItem().toString();
//                String tripId = generateTripId(departingStop, routeSelected);
//                Toast.makeText(MyStatusActivity.this, tripId + ":Trip ID", Toast.LENGTH_SHORT).show();
            }
        });


    }

/*
    method for radio button
*/
    public void checkButton(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.rbOnBoard:
                if(checked) {
                    statusRadioBtn = findViewById(R.id.rbOnBoard);
                    Toast.makeText(MyStatusActivity.this, "Status: " + statusRadioBtn.getText() , Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rbGotOff:
                if(checked){
                    statusRadioBtn = findViewById(R.id.rbGotOff);
                    Toast.makeText(MyStatusActivity.this, "Status: " + statusRadioBtn.getText() , Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
    }

/*
    Needed checks before the entered data is sent as status
*/
    private void initSend() {
        if(validateData() ){
            if(statusRadioBtn.getText().equals("On Board") ){
                // TRY TO GET TOAST TO DISAPLAY
                Toast.makeText(MyStatusActivity.this, "Status Sent: On Board - Generating Trip ID", Toast.LENGTH_SHORT).show();
                String departingStop = spinnerDepature.getSelectedItem().toString();
                routeSelected = spinnerRoute.getSelectedItem().toString();
                String tripId = generateTripId(departingStop, routeSelected);
//                Toast.makeText(MyStatusActivity.this, "Status Sent: On Board - Generating Trip ID: " + tripId , Toast.LENGTH_SHORT).show();
                Toast.makeText(MyStatusActivity.this, tripId + " :Trip ID", Toast.LENGTH_SHORT).show();
                // TODO: Go and Save the Trip Id and associated Data - Deaprture stop, Destination stop and Time, Status in Firebase
            }else{
                // TODO: Should I generate tripId again so that I can now use it to go and delete the particular trip from Firebase
                Toast.makeText(MyStatusActivity.this, "Confirmation of Getting Off Bus - Deleting DATA", Toast.LENGTH_SHORT).show();
            }

        }else{
            showSnackBar();
        }
    }

/*
    It is activated when a field is yet to be completed and the send button is clicked
*/
    private void showSnackBar() {
        Snackbar.make(parent, "Status Not Sent: Complete All Fields", Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtTxtTimePicker.setText("");
                    }
                }).show();
    }

/*
    Validates that the text field (eg. time) is not left empty
*/
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
//        rbOnBoard = findViewById(R.id.rbOnBoard);
//        rbGotOff = findViewById(R.id.rbGotOff);
        btnSend = findViewById(R.id.btnSend);
    }

/*
    populates with all the active bus routes
*/
    private void populateSpinner() {
        Log.d("Test" ,"1");
//        textJson = findViewById(R.id.textJson);
//        spinnerBus = findViewById(R.id.spinnerBus);

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
                    Toast.makeText(MyStatusActivity.this, "Check Connection", Toast.LENGTH_SHORT).show();
                }

                // GEt the data into textView
                String busResponse = "";

//                busResponse = "Version :" + response.body().getHeaderDetails().getGtfs_realtime_version() +
//                        "\n Timestamp :" + response.body().getHeaderDetails().getTimestamp();
//
//                textJson.append(busResponse);
                //use getarray to get the array part
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

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MyStatusActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, dublinBusList);
                spinnerRoute.setAdapter(spinnerAdapter);
                spinnerRoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String itemValue = parent.getItemAtPosition(position).toString();
                        Toast.makeText(MyStatusActivity.this, itemValue + " Selected!", Toast.LENGTH_SHORT).show();
//                        routeSelected = itemValue;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
//                String busroute = busArray.get(0).getTripUpdateDetails().getTripDetails().getRoute_id();
//                String busroute1 = busroute.substring(0, 2);
//                busResponse = "Route Id" + busroute1;
//                textJson.append(busResponse);

                // use the first two characters of route_id to see if it is dublin
                // if use the fourth and fifth character to build an array list of character that can be sent to loadspinner
                // if I click on a stop and bus route number, I should go through api again to see which of the buses
                // with that route number is at that stop using the time to filter. using the time to filter means that
                // we can know which trip id we need to store in firebase. we then store the destination in the firesbase also.
            }

            @Override
            public void onFailure(Call<BusModel> call, Throwable t) {
                Log.d("Failure", "LAST TEST");
                Log.i("onfailure", "Throwable", t);
            }
        });
    }

/*
    populates the destination and departure stops dropdown list
*/
    private void loadBusStops() {
        Log.d("TEST", "10");
        BufferedReader bufferedReader = null;
        String lineFromFile;
        dublinStops.clear();
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
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MyStatusActivity.this,
                android.R.layout.simple_spinner_dropdown_item, dublinStops);
        loadDepartureStops(dublinStops, spinnerAdapter);
        loadDestinationStops(dublinStops, spinnerAdapter);
    }

/*
    populates only the destination dropdown list
*/
    private void loadDestinationStops(List<String> dublinStops, ArrayAdapter<String> spinnerAdapter) {
        spinnerDestination.setAdapter(spinnerAdapter);
        Log.d("TEST", "13");
        spinnerDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = parent.getItemAtPosition(position).toString();
                Toast.makeText(MyStatusActivity.this, itemValue + " Selected!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("TEST", "14");
            }
        });
        routeString = spinnerDestination.getSelectedItem().toString();
    }

/*
    generates unique identifier for each bus trip
*/
    private String generateTripId(String stopName, String routeSelected) {
        String idForTrip = null;
        int sixtyPosition, startIndexOfSelectedRoute, endIndexOfSelectedRoute;
        String idStop = stopMaps.get(stopName);
        String idStop2 = idStop.trim();
        BufferedReader lineReader = null;
        String fileLine;
        Log.d("TEST", "21");
        try {
            lineReader = new BufferedReader( new InputStreamReader( getAssets().open("stop_times.txt"), "UTF-8"));
            Log.d("TEST", "22");
            int i = 0;
            int minimum = 1000;
            while( (fileLine = lineReader.readLine() ) != null){
//                Log.d("TEST", "23 - WHILE LOOP");
                String[] stopTimesArray = fileLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
//                Log.d("TEST", "24 - AFTER REGEX");
                String stopTrim = stopTimesArray[3].substring(1, (stopTimesArray[3].length()-1 )).trim() ;
                if(idStop2.equals(stopTrim ) ){
                    Log.d("TEST", "60");
                    sixtyPosition = stopTimesArray[0].indexOf("60");
                    startIndexOfSelectedRoute = sixtyPosition + 3;
                    endIndexOfSelectedRoute = startIndexOfSelectedRoute + routeSelected.length();
                    if(stopTimesArray[0].substring( startIndexOfSelectedRoute, endIndexOfSelectedRoute ).equals(routeSelected) ){
                        String timePicked = edtTxtTimePicker.getText().toString();
                        Log.d("TEST", "61");
                        String timeFromArray = stopTimesArray[2].substring(1,5).trim() ;
                        String timePickedSubString = timePicked.substring(0,4).trim();
                        if(timePickedSubString.equals(timeFromArray) ){ // changing to 1, 5 instead of 0, 4
//                            if(timeFromArray.equals(timePicked.substring(0,4)) ){ // changing to 1, 5 instead of 0, 4
//                            if(stopTimesArray[2].contains(timePicked.substring(0,4)) ){ // changing to 1, 5 instead of 0, 4
                            String arrayTime = stopTimesArray[2].substring(1,6).trim();
                            String comparePickedTime = timePicked.substring(0,5);
                            String arrayTimeSplit[] = arrayTime.split(":");
                            String comparePickedTimeSplit[] = comparePickedTime.split(":");
                            int arrayTimeMinute = Integer.parseInt(arrayTimeSplit[1]);
                            int compareTimePickedMinute = Integer.parseInt(comparePickedTimeSplit[1]);
                            int differenceInMinutes = arrayTimeMinute - compareTimePickedMinute ;
                            if(differenceInMinutes >= 0 && minimum >= differenceInMinutes){
                                idForTrip = stopTimesArray[0].substring(1, (stopTimesArray[0].length()-1 )).trim() ;
                                minimum = differenceInMinutes;
                                i++;
                            }
//                            idForTrip = stopTimesArray[0].substring(1, (stopTimesArray[0].length()-1 )).trim() ;
                            Log.d("TEST", "62 - match");
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            Log.e("MYAPP", "exception", e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("NEWAPP","IOEException", e);
            e.printStackTrace();
        }
        Log.d("TEST", "63 - return");
        return idForTrip;
    }

/*
    populates only the departure dropdown list
*/
    private void loadDepartureStops(List<String> dublinStops, ArrayAdapter<String> spinnerAdapter) {
        spinnerDepature.setAdapter(spinnerAdapter);
        Log.d("TEST", "13");
        spinnerDepature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = parent.getItemAtPosition(position).toString();
                Toast.makeText(MyStatusActivity.this, itemValue + " Selected!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("TEST", "14");
            }
        });
    }
}