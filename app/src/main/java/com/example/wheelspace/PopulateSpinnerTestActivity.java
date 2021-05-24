package com.example.wheelspace;
// This File was used for testing
// DELETE FILE
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

                //use getarray to get the array part
                busArray = response.body().getBusEntities();
                for(int i = 0; i < busArray.size(); i++){   // change to busArray.size  from 10
                    String routeId = busArray.get(i).getTripUpdateDetails().getTripDetails().getRoute_id();
                    String busId = routeId.substring(0, 2);
                    if(busId.equals("60")){
                        busResponse = "Route Id" + routeId.substring(3, 5);
                        String checkNumberInArray = routeId.substring(3, 5);
                        String checkDash = Character.toString(checkNumberInArray.charAt(1));
                        if(checkDash.equals("-")){
                            checkNumberInArray = Character.toString(checkNumberInArray.charAt(0));
                            checkNumberInArray.trim();
                        }
                    }
                }
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
}