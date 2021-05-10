package com.example.wheelspace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wheelspace.busdata.model.BusRoute;
import com.example.wheelspace.busdata.model.Leg;
import com.example.wheelspace.busdata.model.Route;
import com.example.wheelspace.busdata.model.Step;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlanMyJourneyActivity extends AppCompatActivity {

    private TextView txtDate, txtLeavingFrom, txtArrivingAt;
    private EditText edtTxtDate, edtTxtFutureDeparture, edtTxtFutureDestination;
    private Button btnFindOffPeak;
    private ConstraintLayout parentPlanMyJourney;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    Spinner spinnerFtrDeparture;
    Spinner spinnerFtrDestination;
    ArrayList<String> dublinStops = new ArrayList<>();
    HashMap<String, String> stopMaps = new HashMap<String, String>();
    BusStopUtility busStopUtility = new BusStopUtility();

    List<Route> routeNumbers;
    ArrayList<String> listOfRouteNumbers = new ArrayList<>();

    String busUrl = "https://maps.googleapis.com/maps/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_my_journey);

        initViews();

        busStopUtility.loadBusStops(stopMaps, this, dublinStops, spinnerFtrDestination, spinnerFtrDeparture);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        edtTxtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        PlanMyJourneyActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        edtTxtDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        btnFindOffPeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFindOffPeak();
            }
        });

    }

    private void initFindOffPeak() {
        if(validateData() ){
            Toast.makeText(this, "Processing", Toast.LENGTH_SHORT).show();

            String ftrDeparture = spinnerFtrDeparture.getSelectedItem().toString();
            String ftrDestination = spinnerFtrDestination.getSelectedItem().toString();
            String dateInput = edtTxtDate.getText().toString() ;
            String ftrDate = getWeekDay(dateInput);

            ArrayList<String> routesList = new ArrayList<>();

//            routesList = generatorBusRouteNuumber(ftrDeparture, ftrDestination);

            generatorBusRouteNuumber(ftrDeparture, ftrDestination, ftrDate);

//            Intent intent = new Intent(PlanMyJourneyActivity.this, OffPeakResultActivity.class);

//            intent.putExtra("ftrDepartureKey", ftrDeparture);
//            intent.putExtra("ftrDestinationKey", ftrDestination);
//            intent.putExtra("ftrDateKey", ftrDate);
//            startActivity(intent);

        }else{
            showSnackBar();
        }
    }

    private void generatorBusRouteNuumber(String ftrDeparture, String ftrDestination, String ftrDate) {

        ArrayList<String> busRouteList = new ArrayList<>();

        String[] temp_departure = ftrDeparture.split(",");
        ftrDeparture = temp_departure[0]+","+temp_departure[1].substring(1, temp_departure[1].length() );
        String[] temp_destination = ftrDestination.split(",");
        ftrDestination = temp_destination[0]+","+temp_destination[1].substring(1, temp_destination[1].length());

        ftrDeparture = ftrDeparture.replaceAll(" ","_");
        ftrDestination= ftrDestination.replaceAll(" ", "_");

//        busRouteList = processItinerary(ftrDeparture, ftrDestination, ftrDate);

        processItinerary(ftrDeparture, ftrDestination, ftrDate);
//        return busRouteList;

    }

    private void processItinerary(String ftrDeparture, String ftrDestination, String ftrDate) {

        ArrayList<String> busOptions = new ArrayList<>();
//
//        Proxy retroProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("157.245.142.71",80));
//
        OkHttpClient okHttpClient_bus = new OkHttpClient.Builder()//.proxy(retroProxy)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();


        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit_bus = new Retrofit.Builder()
                .baseUrl(busUrl).client(okHttpClient_bus)
                .addConverterFactory( GsonConverterFactory.create(gson) )
                .build();

        RouteOptionsAPICaller routeOptionsAPICaller = retrofit_bus.create(RouteOptionsAPICaller.class);
        Call<BusRoute> request = routeOptionsAPICaller.getBusOptions(ftrDeparture, ftrDestination);
//
        request.enqueue(new Callback<BusRoute>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<BusRoute> request, Response<BusRoute> response) {

                if (response.code() != 200) {
                    Toast.makeText(PlanMyJourneyActivity.this, "Check Connection", Toast.LENGTH_SHORT).show();
                }

                ArrayList<String> returnedRoute;
                routeNumbers = response.body().getRoutes();
                returnedRoute = getRoute(routeNumbers);
                Intent intent = new Intent(PlanMyJourneyActivity.this, OffPeakResultActivity.class);
                intent.putExtra("ftrDateKey", ftrDate);
//                intent.putExtra("busRetrofitResponse", (Parcelable) routeNumbers);
//                ArrayList<String> test = new ArrayList<String>();
                //intent.putParcelableArrayListExtra("busRetrofitResponse", returnedRoute);
                intent.putExtra("busRetrofitResponse", returnedRoute);

                startActivity(intent);


//                routeNumbers = response.body().getRoutes();
//                for (int i = 0; i < routeNumbers.size(); i++) {   // change to busArray.size  from 10
//                    List<Leg> legs = routeNumbers.get(i).getLegs();
//                    for(int j = 0; j < legs.size(); j++ ){
//                        List<Step> step = legs.get(j).getSteps();
//                        for(int x= 0; x < step.size(); x++){
//                            if(step.get(x).getTravelMode().equals("TRANSIT") ){
//                                String busRouteNumber = step.get(x).getTransitDetails().getLine().getShortName() ;
//                                if( !(listOfRouteNumbers.contains(busRouteNumber) ) ){
//                                    listOfRouteNumbers.add(busRouteNumber);
//                                }
////                                listOfRouteNumbers.add(busRouteNumber);
//                            }
//                        }
//                    }
//                }
//                processClassifierInput(userPost);
            }

            @Override
            public void onFailure(Call<BusRoute> request, Throwable t) {
                Log.d("Failure", "LAST TEST");
                Log.i("onfailure", "Throwable", t);
            }
        });

//        busOptions = listOfRouteNumbers;
//        return busOptions;

    }

    private ArrayList<String> getRoute(List<Route> routeNumbers) {
        ArrayList<String> busChoices = new ArrayList<>();

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
                busChoices = listOfRouteNumbers;
        return busChoices;
    }

    private String getWeekDay(String dateInput) {
        SimpleDateFormat simpleDateFmt = new SimpleDateFormat("dd/MM/yyyy" );
        Date newDate = null;
        try {
            newDate = simpleDateFmt.parse(dateInput);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat dateFormat = new SimpleDateFormat("EEEE");
        String dayOfWeek = dateFormat.format(newDate);
        return dayOfWeek;
    }

    private void showSnackBar() {
        Snackbar.make(parentPlanMyJourney,"Error: Complete All Fields", Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtTxtFutureDestination.setText("");
                        edtTxtFutureDeparture.setText("");
                        edtTxtDate.setText("");
                    }
                }).show();
    }

    private boolean validateData() {
        if(spinnerFtrDeparture.getSelectedItem().toString().equals("")){
            return false;
        }
        if(spinnerFtrDestination.getSelectedItem().toString().equals("")){
            return false;
        }
        if(edtTxtDate.getText().toString().equals("")){
            return false;
        }
        return true;
    }

    private void initViews() {
        txtDate = findViewById(R.id.txtDate);
        txtArrivingAt = findViewById(R.id.txtArrivingAt);
        txtLeavingFrom = findViewById(R.id.txtLeavingFrom);
        edtTxtDate = findViewById(R.id.edtTxtDate);
//        edtTxtFutureDeparture = findViewById(R.id.edtTxtFutureDeparture);
//        edtTxtFutureDestination = findViewById(R.id.edtTxtFutureDestination);
        parentPlanMyJourney = findViewById(R.id.parentPlanMyJourney);
        btnFindOffPeak = findViewById(R.id.btnFindOffPeak);
        spinnerFtrDeparture = findViewById(R.id.spinnerFtrDeparture);
        spinnerFtrDestination = findViewById(R.id.spinnerFtrDestination);
    }



}