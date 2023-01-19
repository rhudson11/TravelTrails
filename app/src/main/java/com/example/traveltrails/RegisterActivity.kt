package com.example.traveltrails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var username_field: EditText
    private lateinit var password_field: EditText
    private lateinit var password2_field: EditText
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var signup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firebaseAuth = FirebaseAuth.getInstance()

        username_field = findViewById(R.id.username)
        password_field = findViewById(R.id.password)
        password2_field = findViewById(R.id.password2)
        signup = findViewById(R.id.signup)

        signup.setOnClickListener {
            val inputtedUsername: String = username_field.text.toString().trim()
            val inputtedPassword: String = password_field.text.toString().trim()
            val inputtedPassword2: String = password2_field.text.toString().trim()
            if(inputtedPassword == inputtedPassword2) {
                firebaseAuth
                    .createUserWithEmailAndPassword(inputtedUsername, inputtedPassword).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        Toast.makeText(
                                this,
                                "Created user: ${user!!.email}",
                                Toast.LENGTH_SHORT).
                        show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        val exception = task.exception
                        Toast.makeText(
                                this,
                                "Failed: $exception",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }
}