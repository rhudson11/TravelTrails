package com.example.traveltrails

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView

class CameraActivity : AppCompatActivity() {

    private lateinit var take_picture_button: Button
    private lateinit var camera_gallery_button: Button
    private var imageUri: Uri? = null
    private lateinit var image_viewer: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        take_picture_button = findViewById(R.id.take_picture_button)
        image_viewer = findViewById(R.id.image_viewer)

        take_picture_button.setOnClickListener { v: View ->
            val values = ContentValues()
         //   values.put(MediaStore.Images.Media.TITLE, R.string.take_picture)
          //  values.put(MediaStore.Images.Media.DESCRIPTION, R.string.take_picture_description)
            imageUri = this?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            // Create camera intent
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

            // Launch intent
            startActivityForResult(intent, 1001)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Callback from camera intent
        if (resultCode == Activity.RESULT_OK){
            // Set image captured to image view
            image_viewer.setImageURI(imageUri)
        }
        else {
            // Failed to take picture
            print("Failed to take camera picture")
        }
    }
}