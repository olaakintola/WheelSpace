package com.example.wheelspace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

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
    private Button btnSubmitFeedback;
    private TextView txtIssue;
    private Spinner spinnerDepartureFeedback;
    private Spinner spinnerDestinationFeedback;
    private EditText edtTxtTimeFeedback;
    private EditText edtTxtFeedbackDescription;
    Calendar fCalendar;
    int fCurrentHour;
    int fCurrentMinute;
    String amPm;
    TimePickerDialog fTimePickerDialog;
    boolean[] issueSelected;
    private ConstraintLayout parentFeedback;

    List<BusTrip> busArray = new ArrayList<>();
    List<String> dublinBusList = new ArrayList<>();
    List<String> dublinStops = new ArrayList<>();
    ArrayList<Integer> listOfIssues = new ArrayList<>();
    String[] issuesArray = {"Ramp", "Wheelchair Buzzer", "Passenger", "Driver", "Inaccessible bus-stops","Luggage not in Racks", "Others"};
    HashMap<String, String> stopMaps = new HashMap<String, String>();
    DatabaseReference feedbackDbRef;


    String url = "https://gtfsr.transportforireland.ie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedbackDbRef = FirebaseDatabase.getInstance().getReference().child("Feedback Message");

        initViews();

        populateFeedbackRouteSpinner();

        showTimeDialog();

        displayIssueFeedbackDialog();

        btnSubmitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSubmit();
            }
        });


    }

    private void initSubmit() {

        if(validateFeedback() ){
            String routeFeedback = spinnerRouteFeedback.getSelectedItem().toString();
            String issueFeedback = txtIssue.getText().toString();
            String timeFeedback = edtTxtTimeFeedback.getText().toString().substring(0,5).trim();
            String departureFeedback = spinnerDepartureFeedback.getSelectedItem().toString();
            String destinationFeedback = spinnerDestinationFeedback.getSelectedItem().toString();
            String descriptionFeedback = edtTxtFeedbackDescription.getText().toString();
            String generatedDate = generateFeedbackSentDate();
            
            Feedback feedback = new Feedback(routeFeedback, issueFeedback, timeFeedback, departureFeedback, destinationFeedback, descriptionFeedback, generatedDate);

            feedbackDbRef.push().setValue(feedback);

            Toast.makeText(FeedbackActivity.this,  " Feedback inserted ", Toast.LENGTH_SHORT).show();

        }else{
            showErrorMessage();
        }
    }

/*
    Generate a Timestamp for when a feedback is sent
*/
    private String generateFeedbackSentDate() {
        String localTime = null;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00") );
        Date currentLocalDate= calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy");
        localTime  = dateFormat.format(currentLocalDate);

        String localDateDayCombo = localTime.substring(0,10);
        String localYear = localTime.substring(29,33);

        localTime = localDateDayCombo + " " + localYear;
        return localTime;
    }

    private boolean validateFeedback() {
        if(edtTxtTimeFeedback.getText().toString().equals("")){
            return false;
        }
        if(txtIssue.getText().toString().equals("")){
            return false;
        }
        if(spinnerDestinationFeedback.getSelectedItem().toString().equals("Choose Stop") ){
            return false;
        }
        if(spinnerDepartureFeedback.getSelectedItem().toString().equals("Choose Stop")){
            return false;
        }
        return true;

    }

    private void showErrorMessage() {
        Snackbar.make(parentFeedback,"Error: Departure and/or Destination has not been Entered", Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }

/*
    Populates the dialog box, with list of possible incidents, for the Issue field in Feedback Activity
*/
    private void displayIssueFeedbackDialog() {
        issueSelected = new boolean[issuesArray.length];
        txtIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FeedbackActivity.this);
                alertDialogBuilder.setTitle("Tick Issues That Apply");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setMultiChoiceItems(issuesArray, issueSelected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // Action to be taken when the user selects and unselects an Option
                        if(isChecked){
                            listOfIssues.add(which);
                            Collections.sort(listOfIssues);
                        }else{
                            listOfIssues.remove(which);
                        }
                    }
                });
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<String> chosenList = new ArrayList<>();
                        for(int j = 0; j < listOfIssues.size(); j++){
                            chosenList.add(issuesArray[listOfIssues.get(j) ] );
                        }
                        String issuesChosen = String.join(", ", chosenList);
                        txtIssue.setText( issuesChosen );
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialogBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int j = 0; j < issueSelected.length; j++){
                            issueSelected[j] = false;
                            listOfIssues.clear();
                            txtIssue.setText("");
                        }
                    }
                });
                alertDialogBuilder.show();
            }
        });
    }

/*
    Displays the Dialog box for time
*/
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

/*
    Populates the route field in Feedback Activity with bus routes numbers
*/
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

/*
    Calls the functions that populates the departure and destination stops in Feedback Activity
*/
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
                String stopId = stopString[0].substring(1, (stopString[0].length()-1 )).trim() ;

                unsortedDublinStops.add(stopName);
                stopMaps.put(stopName, stopId);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(unsortedDublinStops);
        dublinStops.addAll(unsortedDublinStops);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(FeedbackActivity.this,
                android.R.layout.simple_spinner_dropdown_item, dublinStops);
        loadFeedbackDepartureStops(dublinStops, spinnerAdapter);
        loadFeedbackDestinationStops(dublinStops, spinnerAdapter);
    }

/*
    Loads the destination field in Feedback Activity
*/
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
    }

    /*
    Loads the departure field in Feedback Activity
*/
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
        spinnerDepartureFeedback = findViewById(R.id.spinnerFdbackDeparture);
        spinnerDestinationFeedback = findViewById(R.id.spinnerFdbackDestination);
        edtTxtTimeFeedback =findViewById(R.id.edtTxtTimeFeedback);
        edtTxtFeedbackDescription = findViewById(R.id.edtTxtFeedbackDescription);
        txtIssue = findViewById(R.id.txtIssue);
        btnSubmitFeedback = findViewById(R.id.btnSubmitFeedback);
        parentFeedback = findViewById(R.id.parentFeedback);
    }

}