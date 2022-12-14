package com.example.traveltrails

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.doAsync
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class CameraActivity : AppCompatActivity() {

    private lateinit var take_picture_button: Button
    private lateinit var camera_gallery_button: Button
    private lateinit var location: String
    private lateinit var imageUri: Uri
    private lateinit var image_viewer: ImageView
    private lateinit var loc_text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        take_picture_button = findViewById(R.id.take_picture_button)
        camera_gallery_button = findViewById(R.id.camera_gallery_button)
        image_viewer = findViewById(R.id.sample_image)
        loc_text = findViewById(R.id.loc_text)
        location = intent.getStringExtra("Title")!!
        loc_text.setText(location)

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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Callback from camera intent
        if (resultCode == Activity.RESULT_OK) {
                if(requestCode == 1001)
                    UploadUtility(this, location).uploadFile(imageUri)
                if(requestCode == 1000) {
                    if (data != null) {
                        imageUri = data.data!!
                        UploadUtility(this, location).uploadFile(imageUri)
                    }
                }

                val intent = Intent(this, MapsActivity::class.java)

                if(!hasLoc(location))
                    gallery_locations.add(Location(location,25,"https://npf-prod.imgix.net/uploads/USA-Franklin_Delano_Roosevelt_Memorial_2022-06-13-204044_cjlw.jpg?auto=compress%2Cformat&fit=max&q=80&w=1600"))
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