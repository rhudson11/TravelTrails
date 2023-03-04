package com.example.traveltrails

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.*


class CameraActivity : AppCompatActivity() {

    private lateinit var take_picture_button: Button
    private lateinit var camera_gallery_button: Button
    private lateinit var view_suggestions_button: Button
    private lateinit var location: String
    private lateinit var modelID: String
    private lateinit var imageUri: Uri
    private lateinit var image_viewer: ImageView
    private lateinit var loc_text: TextView
    private lateinit var serverURL: String
    private lateinit var username: String
    val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        take_picture_button = findViewById(R.id.take_picture_button)
        camera_gallery_button = findViewById(R.id.camera_gallery_button)
        view_suggestions_button = findViewById(R.id.view_suggestion_button)
        image_viewer = findViewById(R.id.sample_image)
        loc_text = findViewById(R.id.loc_text)
        location = intent.getStringExtra("Title")!!
        loc_text.setText(location)

        val preferences = getSharedPreferences("traveltrails", Context.MODE_PRIVATE)
        username = preferences.getString("username", "")!!
        serverURL = "http://coltrane.cs.seas.gwu.edu:8080/user/$username/visit/"

        image_viewer.setImageResource(R.drawable.earth);

        take_picture_button.setOnClickListener { v: View ->
            val values = ContentValues()

            imageUri = this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!

            // Create camera intent
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

            // Launch intent
            startActivityForResult(intent, 1001)
        }

        camera_gallery_button.setOnClickListener { v: View ->
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1000)
        }

        view_suggestions_button.setOnClickListener { v: View ->
            val sceneViewerIntent = Intent(Intent.ACTION_VIEW)
            val modelID = intent.getStringExtra("ModelID")!!
            sceneViewerIntent.data = Uri.parse("https://arvr.google.com/scene-viewer/1.0?file=http://coltrane.cs.seas.gwu.edu:8080/location/$modelID/heatmap.gltf")
            sceneViewerIntent.setPackage("com.google.android.googlequicksearchbox")
            v.getContext().startActivity(sceneViewerIntent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Callback from camera intent
        if (resultCode == Activity.RESULT_OK) {
                if(requestCode == 1001) {
                    UploadUtility(this, location, modelID).uploadFile(imageUri)

                    // Define your request parameters
                    val url = serverURL + modelID
                    val json = """{ "$username": "$modelID" }""" // replace with your actual JSON payload
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
                                println(responseBody)
                            }
                        }
                    })
                }
                if(requestCode == 1000) {
                    if (data != null) {
                        imageUri = data.data!!
                        UploadUtility(this, location, modelID).uploadFile(imageUri)

                        // Define your request parameters
                        val url = serverURL + modelID
                        val json = """{ "$username": "$modelID" }""" // replace with your actual JSON payload
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
                                    println(responseBody)
                                }
                            }
                        })
                    }
                }

                val intent = Intent(this, MapsActivity::class.java)

                if(!hasLoc(location))
                    gallery_locations.add(Location(location, modelID, 25, "https://npf-prod.imgix.net/uploads/USA-Franklin_Delano_Roosevelt_Memorial_2022-06-13-204044_cjlw.jpg?auto=compress%2Cformat&fit=max&q=80&w=1600"))
                startActivity(intent)
        }
    }

    fun hasLoc(s: String) : Boolean {
        for(x in gallery_locations) {
            if(x.title.equals(s))
                return true
        }
        return false
    }
}