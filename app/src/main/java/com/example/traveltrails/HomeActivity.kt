package com.example.traveltrails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class HomeActivity : AppCompatActivity() {

    private lateinit var camera_button: Button
    private lateinit var gallery_button: Button
    private lateinit var leaderboard_button: Button
    private lateinit var view_map_button: Button
    private lateinit var friends_button: Button
    private lateinit var profile_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        camera_button = findViewById(R.id.camera_button)
        gallery_button = findViewById(R.id.gallery_button)
        leaderboard_button = findViewById(R.id.leaderboard_button)
        view_map_button = findViewById(R.id.view_map_button)
        friends_button = findViewById(R.id.friends_button)
        profile_button = findViewById(R.id.profile_button)

        gallery_button.setOnClickListener { v: View ->
            val intent = Intent(this, Upload::class.java)
            startActivity(intent)
        }

        camera_button.setOnClickListener { v: View ->
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        view_map_button.setOnClickListener { v: View ->
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

    }
}