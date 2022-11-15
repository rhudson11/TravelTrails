package com.example.traveltrails

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.hardware.Camera
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files.exists
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity() {

    private lateinit var take_picture_button: Button
    private lateinit var camera_gallery_button: Button
    //private var imageUri: Uri? = null
    private lateinit var imageUri: Uri
    private lateinit var image_viewer: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        take_picture_button = findViewById(R.id.take_picture_button)
        camera_gallery_button = findViewById(R.id.camera_gallery_button)
        image_viewer = findViewById(R.id.image_viewer)

        take_picture_button.setOnClickListener { v: View ->
            val values = ContentValues()
         //   values.put(MediaStore.Images.Media.TITLE, R.string.take_picture)
          //  values.put(MediaStore.Images.Media.DESCRIPTION, R.string.take_picture_description)
            imageUri = this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
           // UploadUtility(this).uploadFile(imageUri) // Either Uri, File or String file path

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
        if (resultCode == Activity.RESULT_OK){
            // Set image captured to image view
            val pictureFile: File = getOutputMediaFile(MEDIA_TYPE_IMAGE)!!
            JSONParser.uploadImage(pictureFile.absolutePath)
            image_viewer.setImageURI(imageUri)
        }
        else {
            // Failed to take picture
            print("Failed to take camera picture")
        }
    }

    private fun getOutputMediaFile(type: Int): File? {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        val mediaStorageDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp"
        )
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        mediaStorageDir.apply {
            if (!exists()) {
                if (!mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory")
                    return null
                }
            }
        }

        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return when (type) {
            MEDIA_TYPE_IMAGE -> {
                File("${mediaStorageDir.path}${File.separator}IMG_$timeStamp.jpg")
            }
            MEDIA_TYPE_VIDEO -> {
                File("${mediaStorageDir.path}${File.separator}VID_$timeStamp.mp4")
            }
            else -> null
        }
    }
}