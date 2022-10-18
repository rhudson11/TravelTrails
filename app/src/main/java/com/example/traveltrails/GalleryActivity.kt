package com.example.traveltrails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class GalleryActivity : AppCompatActivity() {

    private lateinit var start_trail_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        start_trail_button = findViewById(R.id.start_trail_button)

        start_trail_button.setOnClickListener { v: View ->
            val intent = Intent(this, StartTrailActivity::class.java)
            startActivity(intent)
        }
    }
}