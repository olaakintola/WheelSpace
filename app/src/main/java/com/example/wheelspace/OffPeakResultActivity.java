package com.example.wheelspace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wheelspace.busdata.model.BusRoute;
import com.example.wheelspace.busdata.model.Leg;
import com.example.wheelspace.busdata.model.Line;
import com.example.wheelspace.busdata.model.Route;
import com.example.wheelspace.busdata.model.Step;
import com.google.gson.Gson;
import com.jakewharton.shimo.ObjectOrderRandomizer;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
//import retrofit2.converter.moshi.MoshiConverterFactory;

import java.io.IOException;
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
//    private TextView testTxtId;
    private ClassifierModelAPIService classifierModelAPIService;
    String predictionResult = null;
    private ArrayList<UserPost> futureFreeBayList = new ArrayList<>();
    HashMap<String, String> weekDays = new HashMap<>();
    RouteGenerator routeGenerator;
    List<Route> routeNumbers = new ArrayList<>();
    ArrayList<String> listOfRouteNumbers = new ArrayList<>();


    static String url = "http://10.0.2.2:5000/";
    String busOptionUrl = "https://maps.googleapis.com/";
//    String apiKey = "AIzaSyB4pfSZwkbrKDasVSw7hmME0sYdV36C2xY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_off_peak_result);

        offPeakRecycler = findViewById(R.id.offPeakRecycler);
        offPeakRecycler.setHasFixedSize(true);
        offPeakRecycler.setLayoutManager(new LinearLayoutManager(this) );

        freeBayAdapter = new FreeBayAdapter(this, futureFreeBayList );

        offPeakRecycler.setAdapter(freeBayAdapter);

//        initViews();

        Intent intent = getIntent();

        futureDeparture = intent.getStringExtra("ftrDepartureKey");
        futureDestination = intent.getStringExtra("ftrDestinationKey");
        futureDate = intent.getStringExtra("ftrDateKey");

/*        Interceptor interceptor = new Interceptor(){

            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder().addHeader("Cache-Control", "no-cache").cacheControl(CacheControl.FORCE_NETWORK);
                request = builder.build();
                return chain.proceed(request);
            }
        };*/

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).cache(null).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url).client(okHttpClient)
                .addConverterFactory( GsonConverterFactory.create() )   // GsonConverterFactory.create()
                .build();


        // instance for interface
        classifierModelAPIService = retrofit.create(ClassifierModelAPIService.class);

        UserPost userPost = new UserPost();
        userPost.setRoute("13");
        userPost.setTimes("16:00");
        userPost.setDays("fri");

//        Moshi moshi = new Moshi.Builder()
//                .add(ObjectOrderRandomizer.create() )
//                .add( new UserPostAdapter() ).build();
//        JsonAdapter<UserPost> jsonAdapter = moshi.adapter(UserPost.class);
//
//        String json = jsonAdapter.toJson(userPost);

        weekDays.put("Monday", "mon");
        weekDays.put("Tuesday", "tues");
        weekDays.put("Wednesday", "weds");
        weekDays.put("Thursday", "thurs");
        weekDays.put("Friday", "fri");

        String chosenDay = weekDays.get(futureDate);

        ArrayList<String> routesList = new ArrayList<>();

//        routesList = routeGenerator.generatorBusRouteNuumber(futureDeparture, futureDestination);

        routesList = generatorBusRouteNuumber(futureDeparture, futureDestination);


//        routesList.add("1");
//        routesList.add("151");

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

//        for(String route: routesList ){
//            for(String tHour: timesHour ){
//                UserPost userPost1 = new UserPost();
//                userPost1.setRoute(route);
//                userPost1.setTimes(tHour);
//                userPost1.setDays(chosenDay);
//
//                processClassifierInput(userPost1);
//            }
//        }


        processClassifierInput(userPost);


    }

    private ArrayList<String> generatorBusRouteNuumber(String futureDeparture, String futureDestination) {
        ArrayList<String> busRouteList = new ArrayList<>();

        String[] temp_departure = futureDeparture.split(",");
        futureDeparture = temp_departure[0]+","+temp_departure[1].substring(1, temp_departure[1].length() );
        String[] temp_destination = futureDestination.split(",");
        futureDestination = temp_destination[0]+","+temp_destination[1].substring(1, temp_destination[1].length());

        futureDeparture = futureDeparture.replaceAll(" ","_");
        futureDestination= futureDestination.replaceAll(" ", "_");

        busRouteList = processItinerary(futureDeparture, futureDestination);

        return busRouteList;
    }

    private ArrayList<String> processItinerary(String futureDeparture, String futureDestination) {
        ArrayList<String> busOptions = new ArrayList<>();

//        String urlAddon= "json?origin="+futureDeparture+"&destination="+futureDestination+"&mode=transit&transit_mode=bus&alternatives=true&key="+apiKey;
//        String urlAddon= "json?origin="+futureDeparture+"&destination="+futureDestination+"&mode=transit&transit_mode=bus&alternatives=true"; //&key="+apiKey;
        String mode = "transit";
        String transit_mode = "bus";
        String alternatives = "true";
        String apiKey = "AIzaSyB4pfSZwkbrKDasVSw7hmME0sYdV36C2xY";

        OkHttpClient okHttpClient_bus = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).cache(null).build();

        Retrofit retrofit_bus = new Retrofit.Builder()
                .baseUrl(busOptionUrl).client(okHttpClient_bus)
                .addConverterFactory( GsonConverterFactory.create() )
                .build();

        RouteOptionsAPICaller routeOptionsAPICaller = retrofit_bus.create(RouteOptionsAPICaller.class);
//        Call<BusRoute> call_route = routeOptionsAPICaller.getBusOptions(urlAddon);
        Call<BusRoute> request = routeOptionsAPICaller.getBusOptions(futureDeparture, futureDestination, mode, transit_mode, alternatives, apiKey);


        request.enqueue(new Callback<BusRoute>() {
            @Override
            public void onResponse(Call<BusRoute> request, Response<BusRoute> response) {

                if (response.code() != 200) {
                    Toast.makeText(OffPeakResultActivity.this, "Check Connection", Toast.LENGTH_SHORT).show();
                }

                routeNumbers = response.body().getRoutes();
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
//                                listOfRouteNumbers.add(busRouteNumber);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BusRoute> request, Throwable t) {
                Log.d("Failure", "LAST TEST");
                Log.i("onfailure", "Throwable", t);
            }
        });

        busOptions = listOfRouteNumbers;
        return busOptions;
    }

    private void processClassifierInput(UserPost userPost) {
//        UserPost userPost = new UserPost("11","9:00", "weds" );

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
//                UserPost newListPost = null;
//                try {
//                    newListPost = (UserPost) jsonAdapter.fromJson(returnedApiResult);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                UserPost item = newListPost.get(0);

                predictionResult = newListPost.getPrediction();

                if(predictionResult.equals("[0]") ){
//                    testTxtId.setText("Not Array");
                    futureFreeBayList.add(userPost);
                    predictionResult = null;
                }
//                else{
////                    testTxtId.setText(predictionResult);
//                }


//                testTxtId.setText(predictionResult);
                // use for recyclerview
                predictionResult = null;
                String returnedRoute = newListPost.getRoute();
//                String returnedTimes = userPostResponse.getTimes();
//                String returnedDays = userPostResponse.getDays();
            }

            @Override
            public void onFailure(Call<UserPost> call, Throwable t) {
                Log.d("Failure", "LAST TEST");
                Log.i("onfailure", "Throwable", t);
            }
        });



//        Call<UserPost> call = classifierModelAPIService.getPrediction(userPost);
//        call.enqueue(new Callback<UserPost>() {
//            @Override
//            public void onResponse(Call<UserPost> call, Response<UserPost> response) {
//                if (response.code() != 200) {
//                    Toast.makeText(OffPeakResultActivity.this, "Check Connection", Toast.LENGTH_SHORT).show();
//                }
//                UserPost userPostResponse = response.body();
//                predictionResult = userPostResponse.getPrediction();
//                testTxtId.setText(predictionResult);
//                // use for recyclerview
//                String returnedRoute = userPostResponse.getRoute();
////                String returnedTimes = userPostResponse.getTimes();
////                String returnedDays = userPostResponse.getDays();
//            }
//
//            @Override
//            public void onFailure(Call<UserPost> call, Throwable t) {
//                testTxtId.setText(t.getMessage() );
//            }
//        });
    }

    private void initViews() {
        offPeakRecycler = findViewById(R.id.offPeakRecycler);
//        testTxtId = findViewById(R.id.testTxtId);
    }


//    public void processClassifierInput(String route, String times, String days){
//        classifierModelAPIService.getPrediction(route, times, days).enqueue(new Callback<UserPost>() {
//            @Override
//            public void onResponse(Call<UserPost> call, Response<UserPost> response) {
//                if (response.code() != 200) {
//                    Toast.makeText(OffPeakResultActivity.this, "Check Connection", Toast.LENGTH_SHORT).show();
//                }
//
//                predictionResult = response.body().getPrediction();
//            }
//
//            @Override
//            public void onFailure(Call<UserPost> call, Throwable t) {
//                Log.d("Failure", "LAST TEST");
//                Log.i("onfailure", "Throwable", t);
//            }
//
//        });
//    }


}