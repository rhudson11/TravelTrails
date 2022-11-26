package com.example.traveltrails

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView

class HomeActivity : AppCompatActivity() {

    private lateinit var gallery_button: ImageButton
    private lateinit var leaderboard_button: Button
    private lateinit var view_map_button: ImageButton
    private lateinit var friends_button: Button
    private lateinit var profile_button: ImageButton
    private lateinit var welcome: TextView
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        preferences = getSharedPreferences("traveltrails", Context.MODE_PRIVATE)
        val username = preferences.getString("username","")

       // camera_button = findViewById(R.id.camera_button)
        gallery_button = findViewById(R.id.gallery_button)
       // leaderboard_button = findViewById(R.id.leaderboard_button)
        view_map_button = findViewById(R.id.view_map_button)
       // friends_button = findViewById(R.id.friends_button)
        profile_button = findViewById(R.id.profile_button)

        welcome = findViewById(R.id.welcome_text)
        welcome.setText("Welcome,\n" + username)

        gallery_button.setOnClickListener { v: View ->
            val intent = Intent(this, GalleryActivity2::class.java)
            startActivity(intent)
        }

        view_map_button.setOnClickListener { v: View ->
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

    }
}