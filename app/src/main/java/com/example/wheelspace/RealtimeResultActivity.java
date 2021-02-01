package com.example.wheelspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RealtimeResultActivity extends AppCompatActivity {
    
    String origin;
    String goingTo;
    String localTime;
    private RecyclerView listRecycler;
    DatabaseReference wheelchairBayDbReference = FirebaseDatabase.getInstance().getReference().child("WheelchairBayTaken");
    private BusAdapter busAdapter;
    private ArrayList<WheelBayStatus> tripList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_result);

        Intent intent = getIntent();

        origin = intent.getStringExtra("originKey");
        goingTo = intent.getStringExtra("goingToKey");
        localTime = intent.getStringExtra("localTimeKey");

        listRecycler = findViewById(R.id.listRecycler);
        listRecycler.setHasFixedSize(true);
        listRecycler.setLayoutManager(new LinearLayoutManager(this) );

        busAdapter = new BusAdapter(this, tripList );

        listRecycler.setAdapter(busAdapter);

        wheelchairBayDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren() ){
                    WheelBayStatus wheelBayStatus = dataSnapshot.getValue(WheelBayStatus.class);
                    tripList.add(wheelBayStatus);
                }
                busAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}