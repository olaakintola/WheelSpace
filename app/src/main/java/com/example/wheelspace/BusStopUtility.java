package com.example.wheelspace;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BusStopUtility {

    HashMap<String, String> stopIdKeyMaps = new HashMap<>();

/*
    Calls the method that populates the departure and destination fields in other Activities beside My Status Activity without the use of destination
    and departure spinner
*/
    public void loadBusStops(HashMap<String, String> stopMaps, Context context, ArrayList<String> dublinStops ) {

        ArrayList<String> unsortedDublinStops = new ArrayList<>();
        Log.d("TEST", "10");
        BufferedReader bufferedReader = null;
        String lineFromFile;
        dublinStops.clear();
        dublinStops.add("Choose Stop");
        Log.d("TEST", "11");
        try {
            bufferedReader = new BufferedReader( new InputStreamReader( context.getAssets().open("stops.txt"), "UTF-8"));
            Log.d("TEST", "12");
            while( (lineFromFile = bufferedReader.readLine() ) != null){
                String[] stopString = lineFromFile.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                String stopName = stopString[1].substring(1, (stopString[1].length()-1 )).trim() ;
                String stopId = stopString[0].substring(1, (stopString[0].length()-1 )).trim() ;

                unsortedDublinStops.add(stopName);
                stopMaps.put(stopName, stopId);
                stopIdKeyMaps.put(stopId,stopName);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(unsortedDublinStops);
        dublinStops.addAll(unsortedDublinStops);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item, dublinStops);
    }

/*
    Populates the Destination Fields in some Activities
*/
    private void loadDestinationStops(List<String> dublinStops, ArrayAdapter<String> spinnerAdapter, Spinner spinnerDestination, Context context) {
        spinnerDestination.setAdapter(spinnerAdapter);
        Log.d("TEST", "13");
        spinnerDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = parent.getItemAtPosition(position).toString();
                Toast.makeText(context, itemValue + " Selected!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("TEST", "14");
            }
        });
    }

    /*
    Populates the Destination Fields in some Activities
*/
    private void loadDepartureStops(List<String> dublinStops, ArrayAdapter<String> spinnerAdapter, Spinner spinnerDepature, Context context) {
        spinnerDepature.setAdapter(spinnerAdapter);
        Log.d("TEST", "13");
        spinnerDepature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = parent.getItemAtPosition(position).toString();
                Toast.makeText(context, itemValue + " Selected!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("TEST", "14");
            }
        });
    }

    /*
    Calls the method that populates the departure and destination fields in other Activities beside My Status Activity WITH the use of destination
    and departure spinner
*/
    public void loadBusStops(HashMap<String, String> stopMaps, Context context, ArrayList<String> dublinStops, Spinner spinnerDestination, Spinner spinnerDeparture ) {
        ArrayList<String> unsortedDublinStops = new ArrayList<>();
        Log.d("TEST", "10");
        BufferedReader bufferedReader = null;
        String lineFromFile;
        dublinStops.clear();
        dublinStops.add("Choose Stop");
        Log.d("TEST", "11");
        try {
            bufferedReader = new BufferedReader( new InputStreamReader( context.getAssets().open("stops.txt"), "UTF-8"));
            Log.d("TEST", "12");
            while( (lineFromFile = bufferedReader.readLine() ) != null){
                String[] stopString = lineFromFile.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                String stopName = stopString[1].substring(1, (stopString[1].length()-1 )).trim() ;
                String stopId = stopString[0].substring(1, (stopString[0].length()-1 )).trim() ;

                unsortedDublinStops.add(stopName);
                stopMaps.put(stopName, stopId);
                stopIdKeyMaps.put(stopId,stopName);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(unsortedDublinStops);
        dublinStops.addAll(unsortedDublinStops);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item, dublinStops);
        loadDepartureStops(dublinStops, spinnerAdapter, spinnerDeparture, context);
        loadDestinationStops(dublinStops, spinnerAdapter, spinnerDestination, context );
    }
}
