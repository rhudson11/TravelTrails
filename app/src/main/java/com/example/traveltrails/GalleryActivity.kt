package com.example.traveltrails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class GalleryActivity : AppCompatActivity() {

    private lateinit var start_trail_button: Button
    private lateinit var view3Dbutton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        start_trail_button = findViewById(R.id.start_trail_button)
        view3Dbutton = findViewById(R.id.view3Dexample)

        start_trail_button.setOnClickListener { v: View ->
            val intent = Intent(this, StartTrailActivity::class.java)
            startActivity(intent)
        }

        view3Dbutton.setOnClickListener { v: View ->
            val sceneViewerIntent = Intent(Intent.ACTION_VIEW)
            sceneViewerIntent.data = Uri.parse("https://arvr.google.com/scene-viewer/1.0?file=https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Avocado/glTF/Avocado.gltf")
            sceneViewerIntent.setPackage("com.google.android.googlequicksearchbox")
            startActivity(sceneViewerIntent)
        }
    }
}