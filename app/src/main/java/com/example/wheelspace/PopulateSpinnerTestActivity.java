package com.example.wheelspace;
// This File was used for testing
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PopulateSpinnerTestActivity extends AppCompatActivity {

    TextView textJson;
    private Spinner spinnerBus;
    List<BusTrip> busArray = new ArrayList<>();
    List<String> dublinBusList = new ArrayList<>();
    List<String> dublinStops = new ArrayList<>();
//    String url = "https://gtfsr.transportforireland.ie/v1/";
    String url = "https://gtfsr.transportforireland.ie";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_populate_spinner_test);

        Log.d("Test" ,"1");
        textJson = findViewById(R.id.textJson);
        spinnerBus = findViewById(R.id.spinnerBus);

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
                    textJson.setText("Something went wrong!");
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
                        String checkNumberInArray = routeId.substring(3, 5);
                        String checkDash = Character.toString(checkNumberInArray.charAt(1));
                        if(checkDash.equals("-")){
                            checkNumberInArray = Character.toString(checkNumberInArray.charAt(0));
                            checkNumberInArray.trim();
                        }
//                        if(!(dublinBusList.contains(checkNumberInArray) ) ){
//                           dublinBusList.add(checkNumberInArray);
//                            Collections.sort(dublinBusList);
//                        }
                    }
                }

//                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(PopulateSpinnerTestActivity.this,
//                        android.R.layout.simple_spinner_dropdown_item, dublinBusList);
//                spinnerBus.setAdapter(spinnerAdapter);
//                spinnerBus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        String itemValue = parent.getItemAtPosition(position).toString();
//                        Toast.makeText(PopulateSpinnerTestActivity.this, itemValue + " Selected!", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });

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
//            lineFromFile = bufferedReader.readLine();
            Log.d("TEST", "12");
//            String[] stopString = lineFromFile.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            while( (lineFromFile = bufferedReader.readLine() ) != null){
                String[] stopString = lineFromFile.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                dublinStops.add(stopString[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(PopulateSpinnerTestActivity.this,
                android.R.layout.simple_spinner_dropdown_item, dublinStops);
        spinnerBus.setAdapter(spinnerAdapter);
        Log.d("TEST", "13");
        spinnerBus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = parent.getItemAtPosition(position).toString();
                Toast.makeText(PopulateSpinnerTestActivity.this, itemValue + " Selected!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("TEST", "14");
            }
        });
    }

//    private String generateTripId(String itemValue) {
//        String idForTrip = null;
//        String idStop = stopMaps.get(itemValue);
//
//        BufferedReader lineReader = null;
//        String fileLine;
//
//        Log.d("TEST", "21");
//        try {
//            lineReader = new BufferedReader( new InputStreamReader( getAssets().open("stop_times.txt"), "UTF-8"));
//            Log.d("TEST", "22");
//            while( (fileLine = lineReader.readLine() ) != null){
//                String[] stopTimesArray = fileLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
//                if(idStop.equals(stopTimesArray[3] ) ){
//                    int sixtyPosition = stopTimesArray[0].indexOf("60");
//                    int startIndexOfSelectedRoute = sixtyPosition + 3;
//                    int endIndexOfSelectedRoute = startIndexOfSelectedRoute + routeSelected.length();
//                    if(stopTimesArray[0].substring( startIndexOfSelectedRoute, endIndexOfSelectedRoute ).equals(routeSelected) ){
//                        String timePicked = edtTxtTimePicker.getText().toString();
//                        if(stopTimesArray[2].contains(timePicked.substring(0,4)) ){
//                            idForTrip = stopTimesArray[0];
//                        }
//                    }
//
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return idForTrip;
//    }
}