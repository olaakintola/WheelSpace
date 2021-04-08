package com.example.wheelspace;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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

    // explore app with room
    // explore reading text file into room
    // get the various data that user has selected and display it in snackbar
    // machine learning variables
    private EditText edtTxtTimePicker;
    private TextView txtRoute, txtDepature, txtDestination, txtStatus;
    private Spinner spinnerRoute, spinnerDepature, spinnerDestination, spinnerBusDirection;
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
    //    String destination;
    String route;
    String tripIdDuplicate = null;
    List<BusTrip> busArray = new ArrayList<>();
    List<String> dublinBusList = new ArrayList<>();
    List<String> tempDublinBusList = new ArrayList<>();
    ArrayList<String> dublinStops = new ArrayList<>();
    HashMap<String, String> stopMaps = new HashMap<String, String>();
    BusStopUtility busStopUtility = new BusStopUtility();

    List<String> directiontList = new ArrayList<>();
    ArrayAdapter<String> directionAdapter;
    ArrayAdapter<String> busDirectionAdapter;
    List<String> busDirectiontList = new ArrayList<>();

    List<String> eachValueFinal = new ArrayList<>();


    DatabaseReference wheelchairStatusDbRef;
    DatabaseReference wheelchairUsageMLData;

    String url = "https://gtfsr.transportforireland.ie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_status);

        wheelchairStatusDbRef = FirebaseDatabase.getInstance().getReference().child("WheelchairBayTaken");
        wheelchairUsageMLData = FirebaseDatabase.getInstance().getReference().child("Usage Data_ML");

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
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        edtTxtTimePicker.setText(String.format("%02d:%02d", hourOfDay, minute) + amPm);
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
                try {
                    initSend();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
    public void checkButton(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbOnBoard:
                if (checked) {
                    statusRadioBtn = findViewById(R.id.rbOnBoard);
                    Toast.makeText(MyStatusActivity.this, "Status: " + statusRadioBtn.getText(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rbGotOff:
                if (checked) {
                    statusRadioBtn = findViewById(R.id.rbGotOff);
                    Toast.makeText(MyStatusActivity.this, "Status: " + statusRadioBtn.getText(), Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
    }

    /*
        Needed checks before the entered data is sent as status
    */
    private void initSend() throws IOException {
        if (validateData()) {
            if (statusRadioBtn.getText().equals("On Board")) {
                // TRY TO GET TOAST TO DISAPLAY
                Toast.makeText(MyStatusActivity.this, "Status Sent: On Board - Generating Trip ID", Toast.LENGTH_SHORT).show();
                String departure = spinnerDepature.getSelectedItem().toString();
                String destination = spinnerDestination.getSelectedItem().toString();
                String routeFirst = spinnerRoute.getSelectedItem().toString();
                route = routeFirst;
                String tripid = generateTripId(departure, routeFirst);
                if (tripid == null) {
//                    showRouteBusComboSnackBar();
                    showSnackBar();
                }

                tripIdDuplicate = tripid;
//                tripIdDuplicate = tripid;
                String time = edtTxtTimePicker.getText().toString();
                String intermediarystops = generateIntermediaryStops(routeFirst,tripid, departure, destination);
                Toast.makeText(MyStatusActivity.this, tripid + " :Trip ID", Toast.LENGTH_SHORT).show();

                // TODO: Go and Save the Trip Id and associated Data - Deaprture stop, Destination stop and Time, Status in Firebase
                WheelBayStatus wheelBayStatus = new WheelBayStatus(tripid, route, departure, destination, time, intermediarystops);

                wheelchairStatusDbRef.push().setValue(wheelBayStatus);     // this data can be deleted by user when getting off bus
                wheelchairUsageMLData.push().setValue(wheelBayStatus);    // persistent data for the machine learning component
                Toast.makeText(MyStatusActivity.this, " Data inserted ", Toast.LENGTH_SHORT).show();

            } else {
                // TODO: Should I generate tripId again so that I can now use it to go and delete the particular trip from Firebase
//                DatabaseReference deleteWheelBayDBRef = FirebaseDatabase.getInstance().getReference("WheelchairBay Taken").child(whe);
                deleteTrip(tripIdDuplicate);
            }

        } else {
            showSnackBar();
        }
    }

    /*Delete status when user gets off bus*/
    private void deleteTrip(String tripIdDuplicate) {
        DatabaseReference deleteDBNode = FirebaseDatabase.getInstance().getReference().child("WheelchairBayTaken");
        deleteDBNode.orderByChild("tripid").equalTo(tripIdDuplicate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        data.getRef().removeValue();
                    }
                    Toast.makeText(MyStatusActivity.this, "Confirmation of Getting Off Bus - Deleting DATA", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyStatusActivity.this, "NO RECORD of Status To Delete", Toast.LENGTH_SHORT).show();
                }
//                for(DataSnapshot data: snapshot.getChildren()){
//                    data.getRef().removeValue();
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String generateIntermediaryStops(String routeFirst, String tripid, String departure, String destination) throws IOException {

        List<String> intermediaryStopList = new ArrayList<String>();

        String departureStopId = stopMaps.get(departure);
        String destinationStopId = stopMaps.get(destination);
        String departureId = departureStopId.trim();
        String destinationId = destinationStopId.trim();
        boolean addtoList = false;
        BufferedReader lineReader = null;
        String fileLine;
        Log.d("TEST", "A");

//        List<String> fileContainingRoute;
//        List<String> returnedBusValues;
//        fileContainingRoute = returnListRouteBusTimes(routeFirst);
//        returnedBusValues = busValuesFromFile(fileContainingRoute);

//        try {
//            lineReader = new BufferedReader(new InputStreamReader(getAssets().open("xaa.txt"), "UTF-8"));
////            lineReader = new BufferedReader(new InputStreamReader(getAssets().open("stop_times.txt"), "UTF-8"));
//            Log.d("TEST", "B");
//
//            while ((fileLine = lineReader.readLine()) != null) {
//        for(String eachValue: returnedBusValues){
        for(String eachValue: eachValueFinal){

                fileLine = eachValue;


                String[] stopTimesArray = fileLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
//                Log.d("TEST", "24 - AFTER REGEX");
                String tripIdFromArray = stopTimesArray[0].substring(1, (stopTimesArray[0].length() - 1)).trim();
                String stopIdTrim = stopTimesArray[3].substring(1, (stopTimesArray[3].length() - 1)).trim();

                if (tripid.equals(tripIdFromArray)) {
                    if (departureId.equals(stopIdTrim)) {
                        String timeAtStop = stopTimesArray[2].substring(1, (stopTimesArray[2].length() - 1)).trim();
                        String stopIdTimeCombo = stopIdTrim + " : " + timeAtStop;
                        intermediaryStopList.add(stopIdTimeCombo);
                        addtoList = true;
                    } else if (addtoList) {
                        String timeAtStop = stopTimesArray[2].substring(1, (stopTimesArray[2].length() - 1)).trim();
                        String stopIdTimeCombo = stopIdTrim + " : " + timeAtStop;
                        intermediaryStopList.add(stopIdTimeCombo);
                        if (destinationId.equals(stopIdTrim)) {
                            addtoList = false;
                        }
                    }
                }

            }

//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String intermidiaryStops = changeStopListToString(intermediaryStopList);
        return intermidiaryStops;
    }

    private void AppCrashSnackBar() {
        Snackbar.make(parent, "Unfortunately The App has Crashed", Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtTxtTimePicker.setText("");
                    }
                }).show();
    }

    private String changeStopListToString(List<String> intermediaryStopList) {

        String listToString = String.join(", ", intermediaryStopList);
//        String listToString = "";
//        for(String stop: intermediaryStopList){
//            listToString += stop + ", ";
//        }
        return listToString;
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

    private void showRouteBusComboSnackBar() {
        Snackbar.make(parent, "Bus Route/Bus Stop Combination NOT Possible: Retry", Snackbar.LENGTH_INDEFINITE)
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
        if (edtTxtTimePicker.getText().toString().equals("")) {
            return false;
        }
        if (spinnerDestination.getSelectedItem().toString().equals("Choose Stop")) {
            return false;
        }
        if (spinnerDepature.getSelectedItem().toString().equals("Choose Stop")) {
            return false;
        }
//        if(tripIdDuplicate.equals("error")){
//            return false;
//        }
        return true;
    }

    private void initViews() {
        edtTxtTimePicker = findViewById(R.id.edtTxtTimePicker);
        txtDepature = findViewById(R.id.txtDepature);
        txtDestination = findViewById(R.id.txtDestination);
        txtRoute = findViewById(R.id.txtRoute);
        txtStatus = findViewById(R.id.txtStatus);
        spinnerDepature = findViewById(R.id.spinnerDepartureStatus);
        spinnerDestination = findViewById(R.id.spinnerDestinationStatus);
        spinnerRoute = findViewById(R.id.spinnerRoute);
        parent = findViewById(R.id.parent);
        rgStatus = findViewById(R.id.rgStatus);
//        rbOnBoard = findViewById(R.id.rbOnBoard);
//        rbGotOff = findViewById(R.id.rbGotOff);
        btnSend = findViewById(R.id.btnSend);
        spinnerBusDirection = findViewById(R.id.spinnerBusDirection);
    }

    /*
        populates with all the active bus routes
    */
    private void populateSpinner() {
        Log.d("Test", "1");
//        textJson = findViewById(R.id.textJson);
//        spinnerBus = findViewById(R.id.spinnerBus);

        // Retrofit Builder
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // instance for interface
        BusAPICaller busAPICaller = retrofit.create(BusAPICaller.class);
        Call<BusModel> call = busAPICaller.getData();
        Log.d("TEST", "2");


//        busStopUtility.loadBusStops(stopMaps, this, dublinStops, spinnerDestination, spinnerDepature);
        busStopUtility.loadBusStops(stopMaps, this, dublinStops);


        call.enqueue(new Callback<BusModel>() {

            @Override
            public void onResponse(Call<BusModel> call, Response<BusModel> response) {
                Log.d("TEST", "3");

                // Checking for response
                if (response.code() != 200) {
                    Toast.makeText(MyStatusActivity.this, "Check Connection", Toast.LENGTH_SHORT).show();
                }

                // GEt the data into textView
                String busResponse = "";

//                busResponse = "Version :" + response.body().getHeaderDetails().getGtfs_realtime_version() +
//                        "\n Timestamp :" + response.body().getHeaderDetails().getTimestamp();
//
//                textJson.append(busResponse);
                //use getarray to get the array part
                dublinBusList.clear();
                dublinBusList.add("Choose Route");
                busArray = response.body().getBusEntities();
                for (int i = 0; i < busArray.size(); i++) {   // change to busArray.size  from 10
                    String routeId = busArray.get(i).getTripUpdateDetails().getTripDetails().getRoute_id();
                    String busId = routeId.substring(0, 2);
                    if (busId.equals("60")) {
                        busResponse = "Route Id" + routeId.substring(3, 5);
//                        textJson.append(busResponse);
                        String checkNumberInArray = routeId.substring(3, 6);
                        String checkDash = Character.toString(checkNumberInArray.charAt(1));
                        if (checkDash.equals("-")) {
                            checkNumberInArray = Character.toString(checkNumberInArray.charAt(0));
                            checkNumberInArray.trim();
                        }

                        if (checkNumberInArray.length() == 3) {
                            checkDash = Character.toString(checkNumberInArray.charAt(2));
                            if (checkDash.equals("-")) {
                                checkNumberInArray = checkNumberInArray.substring(0, 2);
                            }
                        }

                        if (!(tempDublinBusList.contains(checkNumberInArray))) {
                            tempDublinBusList.add(checkNumberInArray);
                            Collections.sort(tempDublinBusList);
                        }
                    }
                }

//        dublinStops.add(stopName);
                dublinBusList.addAll(tempDublinBusList);

//                loadBusDirection();

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MyStatusActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, dublinBusList);
                spinnerAdapter.notifyDataSetChanged();      // code for 2nd dropdown implementation
                spinnerRoute.setAdapter(spinnerAdapter);
                spinnerRoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        if(position == 0){
//                        }
//                        loadBusDirection(position);
                        String itemValue = parent.getItemAtPosition(position).toString();
                        try {
                            setListForRoute(itemValue);
                            loadBusDirection(position, itemValue);
//                            setListForRoute(itemValue);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MyStatusActivity.this, itemValue + " Selected!", Toast.LENGTH_SHORT).show();
//                        routeSelected = itemValue;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }


                });
//                String busroute = busArray.get(0).getTripUpdateDetails().getTripDetails().getRoute_id();
                // we can know which trip id we need to store in firebase. we then store the destination in the firesbase also.
//                loadBusDirection();
            }

            @Override
            public void onFailure(Call<BusModel> call, Throwable t) {
                Log.d("Failure", "LAST TEST");
                Log.i("onfailure", "Throwable", t);
            }
        });
    }

    private void loadBusDirection(int position, String route) throws IOException {
        List<String> directiontList = new ArrayList<>();
        ArrayAdapter<String> directionAdapter;
        ArrayAdapter<String> busDirectionAdapter;
        List<String> busDirectiontList = new ArrayList<>();

        List<String> listOfDirection = new ArrayList<>();
//        listOfDirection = generateBusDirection(route);

        directiontList.add("Select Route First");
        directionAdapter = new ArrayAdapter<>(MyStatusActivity.this, android.R.layout.simple_spinner_dropdown_item, directiontList);
        directionAdapter.notifyDataSetChanged();

        listOfDirection = generateBusDirection(route);

        busDirectiontList.add("Choose Direction");
        busDirectiontList.addAll(listOfDirection);

        busDirectionAdapter = new ArrayAdapter<>(MyStatusActivity.this, android.R.layout.simple_spinner_dropdown_item, busDirectiontList);
        busDirectionAdapter.notifyDataSetChanged();

        if (position == 0) {
            spinnerBusDirection.setAdapter(directionAdapter);
        } else {
            spinnerBusDirection.setAdapter(busDirectionAdapter);
        }

//        spinnerDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        spinnerBusDirection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String itemValue = parent.getItemAtPosition(position).toString();
                try {
                    generateDepartureStops(position, itemValue, route);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MyStatusActivity.this, itemValue + " Selected!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void generateDepartureStops(int position, String direction, String route) throws IOException {

        List<String> stopsOptions = new ArrayList<>();
        ArrayAdapter<String> departureAdapter;
        List<String> listOfStopsOnRoutes = new ArrayList<>();

        listOfStopsOnRoutes = returnsStops(route, direction);

        if (position == 0) {
            stopsOptions.add("Select Direction First");
            departureAdapter = new ArrayAdapter<>(MyStatusActivity.this, android.R.layout.simple_spinner_dropdown_item, stopsOptions);
            departureAdapter.notifyDataSetChanged();
            spinnerDepature.setAdapter(departureAdapter);
        } else {
            stopsOptions.add("Choose Stops");
            stopsOptions.addAll(listOfStopsOnRoutes);
            departureAdapter = new ArrayAdapter<>(MyStatusActivity.this, android.R.layout.simple_spinner_dropdown_item, stopsOptions);
            departureAdapter.notifyDataSetChanged();
            spinnerDepature.setAdapter(departureAdapter);
        }

        spinnerDepature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String itemValue = parent.getItemAtPosition(position).toString();
                generateDestinantionStops(position, stopsOptions);
                Toast.makeText(MyStatusActivity.this, itemValue + " Selected!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void generateDestinantionStops(int position, List<String> stopsOptions) {

        List<String> destinationBusStops = new ArrayList<>();
        ArrayAdapter<String> destinationAdapter;
        List<String> remainingStopsOnRoutes = new ArrayList<>();

//        remainingStopsOnRoutes = returnRemainingStops(position, stopsOptions);

        if (position == 0) {
            destinationBusStops.add("Select Departure First");
            destinationAdapter = new ArrayAdapter<>(MyStatusActivity.this, android.R.layout.simple_spinner_dropdown_item, destinationBusStops);
            destinationAdapter.notifyDataSetChanged();
            spinnerDestination.setAdapter(destinationAdapter);
        } else {
            destinationBusStops.add("Choose Stops");
            remainingStopsOnRoutes = stopsOptions.subList(position, stopsOptions.size());
            destinationBusStops.addAll(remainingStopsOnRoutes);
            destinationAdapter = new ArrayAdapter<>(MyStatusActivity.this, android.R.layout.simple_spinner_dropdown_item, destinationBusStops);
            destinationAdapter.notifyDataSetChanged();
            spinnerDestination.setAdapter(destinationAdapter);
        }

        spinnerDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = parent.getItemAtPosition(position).toString();
                Toast.makeText(MyStatusActivity.this, itemValue + " Selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

//    private List<String> returnRemainingStops(int position, List<String> stopsOptions) {
//        List<String> stopFiltered = new ArrayList<>();
//
//        if(!(position == 0)){
//            stopFiltered = stopsOptions
//
//        }else{
//            stopFiltered.add("No Options Selected");
//        }
//        return stopFiltered;
//    }

    private List<String> returnsStops(String route, String direction) throws IOException {
        List<String> stopList = new ArrayList<>();

        int sixtyPosition, startIndexOfSelectedRoute, endIndexOfSelectedRoute;
        BufferedReader lineReader = null;
        String fileLine;
        String startOfSequence = "1";
        boolean flag = false;
        String nameOfStop = null;
        String stopId = null;

//        List<String> fileContainingRoutes = new ArrayList<>();
//        List<String> returnedBusList = new ArrayList<>();
//        fileContainingRoutes = returnListRouteBusTimes(route);
//        returnedBusList = busValuesFromFile(fileContainingRoutes);

//        for(String eachValue: returnedBusList ){
        for(String eachValue: eachValueFinal ){

                fileLine = eachValue;

//        try {
////            lineReader = new BufferedReader( new InputStreamReader( getAssets().open("stop_times.txt"), "UTF-8"));
//            lineReader = new BufferedReader(new InputStreamReader(getAssets().open("xaa.txt"), "UTF-8"));
//            int i = 0;
//            while ((fileLine = lineReader.readLine()) != null) {
                String[] stopTimesArray = fileLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                sixtyPosition = stopTimesArray[0].indexOf("60-");
                startIndexOfSelectedRoute = sixtyPosition + 3;
                endIndexOfSelectedRoute = startIndexOfSelectedRoute + route.length();
                String routeFromArray = stopTimesArray[0].substring(startIndexOfSelectedRoute, endIndexOfSelectedRoute).trim();
                String stopSequence = stopTimesArray[4].substring(1, (stopTimesArray[4].length() - 1)).trim();
                String stopHeadSign = stopTimesArray[5].substring(1, (stopTimesArray[5].length() - 1)).trim();

                if (routeFromArray.equals(route) && direction.equals(stopHeadSign)) {
                    if (stopSequence.equals(startOfSequence) || (flag)) {

                        if (stopSequence.equals(startOfSequence) && flag) {
                            break;
                        }

                        if (!flag) {
                            flag = true;
                        }

                        stopId = stopTimesArray[3].substring(1, (stopTimesArray[3].length() - 1)).trim();
                        nameOfStop = busStopUtility.stopIdKeyMaps.get(stopId);
                        stopList.add(nameOfStop);
                    }
                } else {
                    if (!(stopList.isEmpty())) {
                        break;
                    }
                }
            }
//        } catch (FileNotFoundException e) {
//            Log.e("MYAPP", "exception", e);
//            e.printStackTrace();
//        } catch (IOException e) {
//            Log.e("NEWAPP", "IOEException", e);
//            e.printStackTrace();
//        }

        return stopList;
    }

    private List<String> generateBusDirection(String route) throws IOException {
        List<String> routeDirectionList = new ArrayList<>();
        List<String> seenStopIdBefore = new ArrayList<>();

        int sixtyPosition, startIndexOfSelectedRoute, endIndexOfSelectedRoute;
//        String idStop = stopMaps.get(stopName);
//        String idStop2 = idStop.trim();
        BufferedReader lineReader = null;
        String fileLine;
        String startOfSequence = "1";
        Log.d("TEST", "21");
        boolean flag = false;
        String tempBusDirection = null;
        String firstRouteStop = null;

//        List<String> textFileContainingRoute = new ArrayList<>();
//        List<String> returnedBusValues = new ArrayList<>();
//        textFileContainingRoute = returnListRouteBusTimes(route);
//        returnedBusValues = busValuesFromFile(textFileContainingRoute);

//        for(String eachValue: returnedBusValues){
         for(String eachValue: eachValueFinal){

                fileLine = eachValue;
//        try {
//            lineReader = new BufferedReader(new InputStreamReader(getAssets().open("xaa.txt"), "UTF-8"));
////            lineReader = new BufferedReader( new InputStreamReader( getAssets().open("stop_times.txt"), "UTF-8"));
//            Log.d("TEST", "22");
//            int i = 0;
//            while ((fileLine = lineReader.readLine()) != null) {


//                Log.d("TEST", "23 - WHILE LOOP");
                String[] stopTimesArray = fileLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
//                Log.d("TEST", "24 - AFTER REGEX");
                String stopTrim = stopTimesArray[3].substring(1, (stopTimesArray[3].length() - 1)).trim();
                Log.d("TEST", "60");
                sixtyPosition = stopTimesArray[0].indexOf("60-");
                startIndexOfSelectedRoute = sixtyPosition + 3;
                endIndexOfSelectedRoute = startIndexOfSelectedRoute + route.length();
                String routeFromArray = stopTimesArray[0].substring(startIndexOfSelectedRoute, endIndexOfSelectedRoute).trim();
                String stopSequence = stopTimesArray[4].substring(1, (stopTimesArray[4].length() - 1)).trim();

                if (routeFromArray.equals(route) && stopSequence.equals(startOfSequence)) {

                    Log.d("TEST", "61");
//                    String stopSequence = stopTimesArray[4].substring(1,(stopTimesArray[4].length()-1 ) ).trim() ;
//                                flag = false;

                    tempBusDirection = stopTimesArray[5].substring(1, (stopTimesArray[5].length() - 1)).trim();
                    if (!(routeDirectionList.isEmpty())) {
                        if (!(routeDirectionList.get(0).equals(tempBusDirection))) {

                            routeDirectionList.add(tempBusDirection);
                        }

                    } else {
                        routeDirectionList.add(tempBusDirection);
                    }

                    if (routeDirectionList.size() == 2) {
                        break;
                    }

                    firstRouteStop = stopTimesArray[3].substring(1, (stopTimesArray[3].length() - 1)).trim();
                    seenStopIdBefore.add(firstRouteStop);
                }

            }
//        } catch (FileNotFoundException e) {
//            Log.e("MYAPP", "exception", e);
//            e.printStackTrace();
//        } catch (IOException e) {
//            Log.e("NEWAPP", "IOEException", e);
//            e.printStackTrace();
//        }
        Log.d("TEST", "63 - return");
        return routeDirectionList;
    }


/*
    populates the destination and departure stops dropdown list
*/


/*
    populates only the destination dropdown list
*/


    /*
        generates unique identifier for each bus trip
    */
    private String generateTripId(String stopName, String routeSelected) throws IOException {
        String idForTrip = null;
        int sixtyPosition, startIndexOfSelectedRoute, endIndexOfSelectedRoute;
        String idStop = stopMaps.get(stopName);
        String idStop2 = idStop.trim();
        BufferedReader lineReader = null;
        String fileLine;
        Log.d("TEST", "21");
//        try {
//            lineReader = new BufferedReader(new InputStreamReader(getAssets().open("xaa.txt"), "UTF-8"));
////            lineReader = new BufferedReader( new InputStreamReader( getAssets().open("stop_times.txt"), "UTF-8"));

//            List<String> routeBusList = new ArrayList<>();
//            List<String> BusValues = new ArrayList<>();
//
//            routeBusList = returnListRouteBusTimes(routeSelected);
//            BusValues = busValuesFromFile(routeBusList);

            Log.d("TEST", "22");
            int i = 0;
            int minimumMinute = 1000;
            int minimumSeconds = 1000;


//            while ((fileLine = lineReader.readLine()) != null) {

//            for(String lineFromFile: BusValues){
        for(String lineFromFile: eachValueFinal){

                 fileLine = lineFromFile;

                String[] stopTimesArray = fileLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
//                Log.d("TEST", "24 - AFTER REGEX");
                String stopTrim = stopTimesArray[3].substring(1, (stopTimesArray[3].length() - 1)).trim();
                if (idStop2.equals(stopTrim)) {
                    Log.d("TEST", "60");
                    sixtyPosition = stopTimesArray[0].indexOf("60-");
                    startIndexOfSelectedRoute = sixtyPosition + 3;
                    endIndexOfSelectedRoute = startIndexOfSelectedRoute + routeSelected.length();
                    String routeFromArray = stopTimesArray[0].substring(startIndexOfSelectedRoute, endIndexOfSelectedRoute).trim();
                    if (routeFromArray.equals(routeSelected)) {
                        String timePicked = edtTxtTimePicker.getText().toString();
                        Log.d("TEST", "61");
                        String timeFromArray = stopTimesArray[2].substring(1, 5).trim();
                        String timePickedSubString = timePicked.substring(0, 4).trim();
                        if (timePickedSubString.equals(timeFromArray)) { // changing to 1, 5 instead of 0, 4
//                            if(timeFromArray.equals(timePicked.substring(0,4)) ){ // changing to 1, 5 instead of 0, 4
//                            if(stopTimesArray[2].contains(timePicked.substring(0,4)) ){ // changing to 1, 5 instead of 0, 4
//                            String arrayTime = stopTimesArray[2].substring(1,6).trim();
                            String arrayTime = stopTimesArray[2].substring(1, 9).trim();
                            String comparePickedTime = timePicked.substring(0, 5);
                            String arrayTimeSplit[] = arrayTime.split(":");
                            String comparePickedTimeSplit[] = comparePickedTime.split(":");
                            int arrayTimeMinute = Integer.parseInt(arrayTimeSplit[1]);
                            int arrayTimeSeconds = Integer.parseInt(arrayTimeSplit[2]);
                            int compareTimePickedMinute = Integer.parseInt(comparePickedTimeSplit[1]);
                            int differenceInMinutes = arrayTimeMinute - compareTimePickedMinute;
                            if (differenceInMinutes >= 0 && minimumMinute >= differenceInMinutes) {
                                if (minimumSeconds > arrayTimeSeconds) {
                                    idForTrip = stopTimesArray[0].substring(1, (stopTimesArray[0].length() - 1)).trim();
                                    minimumMinute = differenceInMinutes;
                                    minimumSeconds = arrayTimeSeconds;
                                    i++;
                                }
//                                idForTrip = stopTimesArray[0].substring(1, (stopTimesArray[0].length()-1 )).trim() ;
//                                minimumMinute = differenceInMinutes;
//                                i++;
                            }
//                            idForTrip = stopTimesArray[0].substring(1, (stopTimesArray[0].length()-1 )).trim() ;
                            Log.d("TEST", "62 - match");
                        }
                    }
                }
            }
//        } catch (FileNotFoundException e) {
//            Log.e("MYAPP", "exception", e);
//            e.printStackTrace();
//        } catch (IOException e) {
//            Log.e("NEWAPP", "IOEException", e);
//            e.printStackTrace();
//        }
//        Log.d("TEST", "63 - return");
        return idForTrip;
    }

    private List<String> busValuesFromFile(List<String> busTimeFiles) throws IOException {
        List<String> values = new ArrayList<>();

        BufferedReader lineReader;
        for (String index : busTimeFiles) {

            lineReader = new BufferedReader(new InputStreamReader(getAssets().open(index), "UTF-8"));
            String value;
            while ((value = lineReader.readLine()) != null) {
                values.add(value);
            }
        }
        return values;
    }

/*
    populates only the departure dropdown list
*/

        public List<String> returnListRouteBusTimes (String route) throws IOException {
            List<String> bustimes = new ArrayList<>();

            String[] fileArray = {"xaa.txt", "xab.txt", "xac.txt", "xad.txt", "xae.txt", "xaf.txt", "xag.txt", "xah.txt", "xai.txt", "xaj.txt", "xak.txt",
                    "xal.txt", "xam.txt", "xan.txt", "xao.txt", "xap.txt", "xaq.txt", "xar.txt", "xas.txt", "xat.txt"};

//        loop through the array and return a list of files that contains route, then the
//        route can work with the logic i implemented with old system
//        List<String> listFromFile;
            String containsRoute = null;
            for (String fileName : fileArray) {

                containsRoute = readFileData(fileName, route);
                if (containsRoute != null) {
                    bustimes.add(fileName);
                }
            }
            return bustimes;
        }

        private String readFileData (String fileName, String route) throws IOException {
            String results = null;
            BufferedReader lineReader;
            String fileLine;
            String routeCombo = ".60-" + route +"-d12";
            lineReader = new BufferedReader(new InputStreamReader(getAssets().open(fileName), "UTF-8"));
            while ((fileLine = lineReader.readLine()) != null) {
                String[] stopTimesArray = fileLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
//                if (stopTimesArray[0].contains(routeCombo)) {
                    if (stopTimesArray[0].matches("(.*)"+routeCombo+"(.*)" ) ) {
                        results = fileName;
                    break;
                }
            }
            return results;
        }

        private void setListForRoute(String route) throws IOException {

            List<String> fileContainingRoutes = new ArrayList<>();
            List<String> returnedBusList = new ArrayList<>();
            if( !(route.equals("Choose Route") ) ){
                fileContainingRoutes = returnListRouteBusTimes(route);
                returnedBusList = busValuesFromFile(fileContainingRoutes);
            }
//            fileContainingRoutes = returnListRouteBusTimes(route);
//            returnedBusList = busValuesFromFile(fileContainingRoutes);

            eachValueFinal = returnedBusList;
        }

}