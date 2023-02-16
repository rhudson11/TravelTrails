package com.example.traveltrails

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

var gallery_locations: ArrayList<Location> = ArrayList<Location>()
var friends: ArrayList<Friend> = ArrayList<Friend>()

class MainActivity : AppCompatActivity() {

    private lateinit var login_user_field: EditText
    private lateinit var login_pass_field: EditText
    private lateinit var login_button: Button
    private lateinit var register_button: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        login_user_field = findViewById(R.id.login_user_field)
        login_pass_field = findViewById(R.id.login_pass_field)
        login_button = findViewById(R.id.login_button)
        register_button = findViewById(R.id.register_button)

        preferences = getSharedPreferences("traveltrails", Context.MODE_PRIVATE)

        login_button.setOnClickListener { v: View ->
            preferences.edit().putString("username", login_user_field.text.toString().substringBefore('@')).apply()

            val inputtedUsername = login_user_field.text.toString().trim()
            val inputtedPassword = login_pass_field.text.toString().trim()

            firebaseAuth.signInWithEmailAndPassword(inputtedUsername, inputtedPassword) .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Toast.makeText(this, "Logged in as user: ${user!!.email}", Toast.LENGTH_SHORT).show() // Go to the next Activity ...
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                } else {
                    val exception = task.exception
                    Toast.makeText(this, "Failed: $exception", Toast.LENGTH_SHORT).show() }
            }
        }

        register_button.setOnClickListener { v: View ->
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            /*
            doAsync {
                ServerManager().run();
            }
             */
        }
    }
}