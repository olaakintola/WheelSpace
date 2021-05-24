package com.example.wheelspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class RealtimeResultActivity extends AppCompatActivity {
    
    String origin;
    String goingTo;
    String localTime;
    private RecyclerView listRecycler;
    DatabaseReference wheelchairBayDbReference = FirebaseDatabase.getInstance().getReference().child("WheelchairBayTaken");
    private BusAdapter busAdapter;
    private ArrayList<WheelBayStatus> tripList = new ArrayList<>();
    private HashMap<String, String> stopNameToCodeMap = new HashMap<String, String>();
    private HashMap<String, String> stopCodeToNameMap = new HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_result);

        busStopHashMap();
        Intent intent = getIntent();

        origin = intent.getStringExtra("originKey");
        goingTo = intent.getStringExtra("goingToKey");
        String localTime = intent.getStringExtra("localTimeKey");

        String originId = stopNameToCodeMap.get(origin);
        String goingToId = stopNameToCodeMap.get(goingTo);

        listRecycler = findViewById(R.id.listRecycler);
        listRecycler.setHasFixedSize(true);
        listRecycler.setLayoutManager(new LinearLayoutManager(this) );

        busAdapter = new BusAdapter(this, tripList );

        listRecycler.setAdapter(busAdapter);

/*        Functions handles whether incoming bus has a wheelchair bay availabe
         comparing departure and destination stops with intermediary stops from firebase */
        wheelchairBayDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren() ){
                    WheelBayStatus wheelBayStatus = dataSnapshot.getValue(WheelBayStatus.class);
                    String intermediaryStops = wheelBayStatus.getIntermediarystops();
                    String[] intermediaryStopArray = intermediaryStops.split(",");
                    int i;
                    String firstStop = null;
                    String timeAtStop = null;
                    for( i= 0; i < intermediaryStopArray.length; i++){
                        firstStop = intermediaryStopArray[i].substring(0, 13).trim() ;
                        timeAtStop = intermediaryStopArray[i].substring(15, 21).trim() ;

                        String[] hoursAndMin = timeAtStop.split(":");
                        int arrayTimeHour = Integer.parseInt(hoursAndMin[0]);
                        int arrayTimeMinute = Integer.parseInt(hoursAndMin[1]);
                        String[] currentLocalTime = localTime.split(":");
                        int localTimeHour = Integer.parseInt(currentLocalTime[0].substring(0,2).trim() );

                        int localTimeMinute = Integer.parseInt(currentLocalTime[1].substring(0,2).trim() );
                        int durationHour = (arrayTimeHour - localTimeHour ) * 60;
                        int durationMinute = (arrayTimeMinute - localTimeMinute);
                        int totalDuration = durationHour + durationMinute ;

                        if(originId.equals(firstStop) && 60 > totalDuration){
                            int j ;
                            String secondStop = null;
                            for(j = i +1; j < intermediaryStopArray.length; j++){
                                secondStop = intermediaryStopArray[j].substring(1, 13).trim() ;
                                if(goingToId.equals(secondStop) ){
                                    String busAtStopTime = String.valueOf(totalDuration);
                                    String[] originSplit = origin.split(",");
                                    String displayStopName = originSplit[0].trim() + "  :  " + busAtStopTime + " min";
                                    wheelBayStatus.setIntermediarystops( displayStopName );
                                    tripList.add(wheelBayStatus);
                                }
                            }
                        }
                    }
                }
                busAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

/*
    Creates an HashMap of bus stop code and bus stop name
*/
    private void busStopHashMap() {
            Log.d("TEST", "10");
            BufferedReader bufferedReader = null;
            String lineFromFile;
            Log.d("TEST", "11");
            try {
                bufferedReader = new BufferedReader( new InputStreamReader( getAssets().open("stops.txt"), "UTF-8"));
                Log.d("TEST", "12");
                while( (lineFromFile = bufferedReader.readLine() ) != null){
                    String[] stopString = lineFromFile.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    String stopName = stopString[1].substring(1, (stopString[1].length()-1 )).trim() ;
                    String stopId = stopString[0].substring(1, (stopString[0].length()-1 )).trim() ;

                    stopNameToCodeMap.put(stopName, stopId);

                    stopCodeToNameMap.put(stopId, stopName);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }


}