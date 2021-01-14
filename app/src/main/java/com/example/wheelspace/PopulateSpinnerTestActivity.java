package com.example.wheelspace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import retrofit2.Retrofit;

public class PopulateSpinnerTestActivity extends AppCompatActivity {

    TextView textJson;
    String url = "https://gtfsr.transportforireland.ie/v1/?format=json"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_populate_spinner_test);

        textJson = findViewById(R.id.textJson);

        // Retrofit Builder
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
    }
}