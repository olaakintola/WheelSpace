package com.example.wheelspace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RaiseAnIssueActivity extends AppCompatActivity {

    private Button btnFeedback;
    private Button btnSearchIssue;
    private EditText edtTxtSearch;
    private RecyclerView recyclerRaiseIssue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_an_issue);

        initViews();

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RaiseAnIssueActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });


    }

    private void initViews() {
        btnFeedback = findViewById(R.id.btnFeedback);
        btnSearchIssue = findViewById(R.id.btnSearchIssue);
        edtTxtSearch = findViewById(R.id.edtTxtSearch);
        recyclerRaiseIssue = findViewById(R.id.recyclerRaiseIssue);
    }
}