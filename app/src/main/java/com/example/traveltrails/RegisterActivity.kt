package com.example.traveltrails

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.jetbrains.anko.doAsync
import java.io.IOException


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
/*
                        val shortUser = inputtedUsername.substringBefore('@')
                        val client = OkHttpClient()
                        doAsync {

                            val formBody: RequestBody = FormBody.Builder()
                                    .add("username", shortUser)
                                    .add("name", "Test")
                                    .add("profile_picture", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/85/Smiley.svg/1200px-Smiley.svg.png")
                                    .build()
                            val request: Request = Request.Builder()
                                    .url("http://coltrane.cs.seas.gwu.edu:8080/user")
                                    .post(formBody)
                                    .build()

                            try {
                                val response = client.newCall(request).execute()

                                // Do something with the response.
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                        */


                        val shortUser = inputtedUsername.substringBefore('@')
                        // Define your request parameters
                        val url = "http://coltrane.cs.seas.gwu.edu:8080/user"
                        val json = """{ "username": "$shortUser", "name": "Test", "profile_picture": "https://www.google.com/url?sa=i&url=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FSmiley&psig=AOvVaw1p_KhHwaujcXsv-vuEJLBa&ust=1678046498007000&source=images&cd=vfe&ved=0CA8QjRxqFwoTCLiJpr2Iw_0CFQAAAAAdAAAAABAF" }"""
                        val mediaType = "application/json".toMediaTypeOrNull()
                        val requestBody = json.toRequestBody(mediaType)

                        // Create OkHttp client and request object
                        val client = OkHttpClient()
                        val request = Request.Builder()
                                .url(url)
                                .post(requestBody)
                                .build()

                        // Execute the request asynchronously
                        client.newCall(request).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                e.printStackTrace()
                            }

                            override fun onResponse(call: Call, response: Response) {
                                response.use {
                                    if (!response.isSuccessful) {
                                        throw IOException("Unexpected code $response")
                                    }

                                    // Handle response
                                    val responseBody = response.body?.string() // replace with your desired response handling
                                    Log.e("register", responseBody.toString())
                                }
                            }
                        })

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