package com.example.traveltrails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void GetAll(View v) {
        startActivity(new Intent(MainActivity2.this, GetAll.class));                     // Start the activity to get all images
    }

    public void Upload(View v) {
        startActivity(new Intent(MainActivity2.this, Upload.class));                     // Start the activity to upload an image
    }

    public void GetByName(View v) {
        startActivity(new Intent(MainActivity2.this, GetByName.class));                  // Start the activity to get an image by its name
    }
}