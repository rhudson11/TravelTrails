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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.doAsync
import org.json.JSONArray

var gallery_locations: ArrayList<Location> = ArrayList<Location>()
var location_lookup: HashMap<String,String> = hashMapOf<String,String>()
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

                    val okHttpClient: OkHttpClient
                    val builder = OkHttpClient.Builder()
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY
                    builder.addInterceptor(logging)
                    okHttpClient = builder.build()

                    doAsync {
                        var request =
                                Request.Builder()
                                        .get()
                                        .url("http://coltrane.cs.seas.gwu.edu:8080/locations")
                                        .build()

                        var response: Response = okHttpClient.newCall(request).execute()
                        var responseBody = response.body?.string()

                        if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                            val json = JSONArray(responseBody)
                            //   val results = json.getJSONArray("")
                            for (i in 0 until json.length()) {
                                val curr = json.getJSONObject(i)
                                val name: String = curr.getString("name").toString()
                                val modelID: String = curr.getString("id").toString()
                                location_lookup.put(modelID, name)
                            }
                        }

                        val shortUser = inputtedUsername.substringBefore('@')
                        request =
                                Request.Builder()
                                        .get()
                                        .url("http://coltrane.cs.seas.gwu.edu:8080/user/$shortUser/visits")
                                        .build()

                        response = okHttpClient.newCall(request).execute()
                        responseBody = response.body?.string()

                        if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                            val json = JSONArray(responseBody)
                            for (i in 0 until json.length()) {
                                val curr = json.getJSONObject(i)
                                val id: Int = curr.getInt("location_id")

                                gallery_locations.add(Location(location_lookup.get(id.toString())!!,id.toString(),25,"https://images.assetsdelivery.com/compings_v2/jemastock/jemastock1909/jemastock190937249.jpg"))
                            }
                        }

                    }

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