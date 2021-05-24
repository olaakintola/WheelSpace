package com.example.wheelspace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
//import org.apache.http.client.HttpClient;

import com.example.wheelspace.busdata.model.BusRoute;
import com.example.wheelspace.busdata.model.Leg;
import com.example.wheelspace.busdata.model.Line;
import com.example.wheelspace.busdata.model.Route;
import com.example.wheelspace.busdata.model.Step;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.shimo.ObjectOrderRandomizer;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
//import retrofit2.converter.moshi.MoshiConverterFactory;

//import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OffPeakResultActivity extends AppCompatActivity {

    private RecyclerView offPeakRecycler;
    FreeBayAdapter freeBayAdapter;
    String futureDeparture;
    String futureDestination;
    String futureDate;
    private ClassifierModelAPIService classifierModelAPIService;
    String predictionResult = null;
    private ArrayList<UserPost> futureFreeBayList = new ArrayList<>();
    HashMap<String, String> weekDays = new HashMap<>();
    RouteGenerator routeGenerator;
    List<Route> routeNumbers = new ArrayList<>();
    ArrayList<String> listOfRouteNumbers = new ArrayList<>();

    UserPost userPost = new UserPost();


    static String url = "http://10.0.2.2:5000/";
    String busUrl = "https://maps.googleapis.com/maps/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_off_peak_result);

        offPeakRecycler = findViewById(R.id.offPeakRecycler);
        offPeakRecycler.setHasFixedSize(true);
        offPeakRecycler.setLayoutManager(new LinearLayoutManager(this) );

        freeBayAdapter = new FreeBayAdapter();
        Intent intent = getIntent();

        ArrayList<String> rtList;
        futureDate = intent.getStringExtra("ftrDateKey");
        rtList = (ArrayList<String>)getIntent().getSerializableExtra("busRetrofitResponse");

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).cache(null).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url).client(okHttpClient)
                .addConverterFactory( GsonConverterFactory.create() )   // GsonConverterFactory.create()
                .build();

        // instance for interface
        classifierModelAPIService = retrofit.create(ClassifierModelAPIService.class);

        weekDays.put("Monday", "mon");
        weekDays.put("Tuesday", "tues");
        weekDays.put("Wednesday", "weds");
        weekDays.put("Thursday", "thurs");
        weekDays.put("Friday", "fri");

        String chosenDay = weekDays.get(futureDate);

        ArrayList<String> timesHour = new ArrayList<>();
        timesHour.add("7:00");
        timesHour.add("8:00");
        timesHour.add("9:00");
        timesHour.add("10:00");
        timesHour.add("11:00");
        timesHour.add("12:00");
        timesHour.add("13:00");
        timesHour.add("14:00");
        timesHour.add("15:00");
        timesHour.add("16:00");
        timesHour.add("17:00");
        timesHour.add("18:00");

        for(String route: rtList ){
            for(String tHour: timesHour ){
                UserPost userPost1 = new UserPost();
                userPost1.setRoute(route);
                userPost1.setTimes(tHour);
                userPost1.setDays(chosenDay);

                processClassifierInput(userPost1);
            }
        }
    }

/*
    Returns bus route number that goes from departure stop to destination stop
*/
    private ArrayList<String> generatorBusRouteNuumber(String futureDeparture, String futureDestination) {
        ArrayList<String> busRouteList = new ArrayList<>();

        String[] temp_departure = futureDeparture.split(",");
        futureDeparture = temp_departure[0]+","+temp_departure[1].substring(1, temp_departure[1].length() );
        String[] temp_destination = futureDestination.split(",");
        futureDestination = temp_destination[0]+","+temp_destination[1].substring(1, temp_destination[1].length());

        futureDeparture = futureDeparture.replaceAll(" ","_");
        futureDestination= futureDestination.replaceAll(" ", "_");

        return busRouteList;
    }

/*
    Returns an arraylist of bus routes actaual numbers
*/
    private ArrayList<String> processItinerary(List<Route> routeNumbers) {

        ArrayList<String> busOptions = new ArrayList<>();

        for (int i = 0; i < routeNumbers.size(); i++) {   // change to busArray.size  from 10
            List<Leg> legs = routeNumbers.get(i).getLegs();
            for(int j = 0; j < legs.size(); j++ ){
                List<Step> step = legs.get(j).getSteps();
                for(int x= 0; x < step.size(); x++){
                    if(step.get(x).getTravelMode().equals("TRANSIT") ){
                        String busRouteNumber = step.get(x).getTransitDetails().getLine().getShortName() ;
                        if( !(listOfRouteNumbers.contains(busRouteNumber) ) ){
                            listOfRouteNumbers.add(busRouteNumber);
                        }
                    }
                }
            }
        }

        busOptions = listOfRouteNumbers;
        return busOptions;
    }


/*    Sends the routes, time, day to the machine learning model API
    which then returns predicts whether wheelchair bay would be available or not
   and this is then sent to the function that display results in UI*/
    private void processClassifierInput(UserPost userPost) {

        List<UserPost> userPostList = new ArrayList<>();
        userPostList.add(userPost);

        Call<UserPost> call = classifierModelAPIService.getPrediction(userPostList);

        String json = new Gson().toJson(userPostList);
        Log.d("Debug",json);

        call.enqueue(new Callback<UserPost>() {
            @Override
            public void onResponse(Call<UserPost> call, Response<UserPost> response) {

                if (response.code() != 200) {
                    Toast.makeText(OffPeakResultActivity.this, "Check Connection", Toast.LENGTH_SHORT).show();
                }

                UserPost newListPost = response.body();
                predictionResult = newListPost.getPrediction();

                if(predictionResult.equals("[0]") ){
                    futureFreeBayList.add(userPost);
                    predictionResult = null;
                }

                predictionResult = null;
                String returnedRoute = newListPost.getRoute();

                freeBayAdapter.setData(futureFreeBayList);
                offPeakRecycler.setAdapter(freeBayAdapter);

            }

            @Override
            public void onFailure(Call<UserPost> call, Throwable t) {
                Log.d("Failure", "LAST TEST");
                Log.i("onfailure", "Throwable", t);
            }
        });
    }

    private void initViews() {
        offPeakRecycler = findViewById(R.id.offPeakRecycler);
    }

}