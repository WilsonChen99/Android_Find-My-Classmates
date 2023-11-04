package com.example.csci310_group15;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RatePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("RATE");
    }
}