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
                        if(!(dublinBusList.contains(checkNumberInArray) ) ){
                           dublinBusList.add(checkNumberInArray);
                            Collections.sort(dublinBusList);
                        }
                    }
                }

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(PopulateSpinnerTestActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, dublinBusList);
                spinnerBus.setAdapter(spinnerAdapter);
                spinnerBus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String itemValue = parent.getItemAtPosition(position).toString();
                        Toast.makeText(PopulateSpinnerTestActivity.this, itemValue + " Selected!", Toast.LENGTH_SHORT).show();
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

}