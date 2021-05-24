package com.example.wheelspace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class RaiseAnIssueActivity extends AppCompatActivity {

    private Button btnFeedback;
    private Button btnSearchIssue;
    private EditText edtTxtSearch;
    private RecyclerView recyclerRaiseIssue;
    private DatabaseReference feedbackDbReference;
    ArrayList<Feedback> feedbackList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_an_issue);

        feedbackDbReference = FirebaseDatabase.getInstance().getReference("Feedback Message");

        initViews();

        recyclerRaiseIssue.setHasFixedSize(true);
        recyclerRaiseIssue.setLayoutManager(new LinearLayoutManager(RaiseAnIssueActivity.this));

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtTxtSearch.setText("");
                Intent intent = new Intent(RaiseAnIssueActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });

    }

/*    Returns the list of complaints containing bus routes entered into the search field
    in Raise An Issue Activity*/
    @Override
    protected void onStart() {
        super.onStart();

        if(feedbackDbReference != null){
            feedbackDbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists() ){
                        feedbackList.clear();
                        String dateCheck = generateFeedbackSentDate();
                        for(DataSnapshot child: snapshot.getChildren() ){
                            Feedback feedback = child.getValue(Feedback.class);
                            String dateOfFeedback = feedback.getGeneratedDate();
                            if(dateCheck.equals(dateOfFeedback)){
                                feedbackList.add(feedback );
                            }
                        }
                        RaiseAnIssueAdapter raiseAnIssueAdapter = new RaiseAnIssueAdapter(feedbackList);
                        recyclerRaiseIssue.setAdapter(raiseAnIssueAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        btnSearchIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String filterFeedback = edtTxtSearch.getText().toString();
                feedbackSearch(filterFeedback);
            }
        });
    }

/*
    Returns a timestamp for current date
*/
    private String generateFeedbackSentDate() {
        String localTime = null;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00") );
        Date currentLocalDate= calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy");
        localTime  = dateFormat.format(currentLocalDate);

        String localDateDayCombo = localTime.substring(0,10);
        String localYear = localTime.substring(29,33);

        localTime = localDateDayCombo + " " + localYear;
        return localTime;
    }

/*
    Displays the returned list of complaints to the UI
*/
    private void feedbackSearch(String filterFeedback) {
        ArrayList<Feedback> routeFilter = new ArrayList<>();
        for(Feedback feedback: feedbackList){
            if(filterFeedback.equals(feedback.getRouteFeedback() ) ){
                routeFilter.add(feedback);
            }
        }
        RaiseAnIssueAdapter raiseAnIssueAdapter = new RaiseAnIssueAdapter(routeFilter);
        recyclerRaiseIssue.setAdapter(raiseAnIssueAdapter);
    }

    private void initViews() {
        btnFeedback = findViewById(R.id.btnFeedback);
        btnSearchIssue = findViewById(R.id.btnSearchIssue);
        edtTxtSearch = findViewById(R.id.edtTxtSearch);
        recyclerRaiseIssue = findViewById(R.id.recyclerRaiseIssue);
    }

}