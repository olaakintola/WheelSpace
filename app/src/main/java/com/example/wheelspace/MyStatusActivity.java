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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    List<BusTrip> busArray = new ArrayList<>();
    List<String> dublinBusList = new ArrayList<>();
    List<String> dublinStops = new ArrayList<>();

    String url = "https://gtfsr.transportforireland.ie";

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
                dublinStops.add(stopString[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MyStatusActivity.this,
                android.R.layout.simple_spinner_dropdown_item, dublinStops);
        loadDepartureStops(dublinStops, spinnerAdapter);
//        spinnerDepature.setAdapter(spinnerAdapter);
//        Log.d("TEST", "13");
//        spinnerDepature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String itemValue = parent.getItemAtPosition(position).toString();
//                Toast.makeText(MyStatusActivity.this, itemValue + " Selected!", Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Log.d("TEST", "14");
//            }
//        });

    }

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