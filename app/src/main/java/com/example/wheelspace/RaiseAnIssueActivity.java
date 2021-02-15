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

import java.util.ArrayList;
import java.util.List;

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

//        btnSearchIssue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String filterFeedback = edtTxtSearch.getText().toString();
//                feedbackSearch(filterFeedback);
//            }
//        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(feedbackDbReference != null){
            feedbackDbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists() ){
                        feedbackList.clear();
                        for(DataSnapshot child: snapshot.getChildren() ){
                            feedbackList.add(child.getValue(Feedback.class) );
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


//        FirebaseRecyclerOptions<Feedback> options = new FirebaseRecyclerOptions.Builder<Feedback>()
//                .setQuery(feedbackDbReference, Feedback.class)
//                .build();

//        FirebaseRecyclerAdapter<Feedback, FeedbackViewHolder> feedbackRecyclerAdapter = new FirebaseRecyclerAdapter<Feedback, FeedbackViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position, @NonNull Feedback model) {
//
//                holder.initialiseFeedbackVariables(model.getRouteFeedback(), model.getIssueFeedback() );
//            }
//
//            @NonNull
//            @Override
//            public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext() ).inflate(R.layout.feedback_list, parent, false);
//
//                return new FeedbackViewHolder(view);
//            }
//        };

//        feedbackRecyclerAdapter.startListening();
//        recyclerRaiseIssue.setAdapter(feedbackRecyclerAdapter);

    }

    private void feedbackSearch(String filterFeedback) {
        ArrayList<Feedback> routeFilter = new ArrayList<>();
        for(Feedback feedback: feedbackList){
            if(filterFeedback.equals(feedback.getRouteFeedback() ) ){
                routeFilter.add(feedback);
            }
        }
        RaiseAnIssueAdapter raiseAnIssueAdapter = new RaiseAnIssueAdapter(routeFilter);
        recyclerRaiseIssue.setAdapter(raiseAnIssueAdapter);

//        if(filterFeedback != null){
//        );
    }

    private void initViews() {
        btnFeedback = findViewById(R.id.btnFeedback);
        btnSearchIssue = findViewById(R.id.btnSearchIssue);
        edtTxtSearch = findViewById(R.id.edtTxtSearch);
        recyclerRaiseIssue = findViewById(R.id.recyclerRaiseIssue);
    }

//    public class FeedbackViewHolder extends RecyclerView.ViewHolder{
//
//        View feedbackView;
//
//        public FeedbackViewHolder(@NonNull View itemView) {
//            super(itemView);
//            feedbackView = itemView;
//        }
//
//        public void initialiseFeedbackVariables(String routeFeedback, String issueFeedback){
//            TextView feedback_route = feedbackView.findViewById(R.id.feedback_route);
//            TextView feedback_subject = feedbackView.findViewById(R.id.feedback_subject);
//
//            feedback_route.setText(routeFeedback);
//            feedback_subject.setText(issueFeedback);
//        }
//
//    }
}