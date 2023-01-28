package com.example.traveltrails

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var home_button: ImageButton
    private lateinit var currentLocation: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        home_button = findViewById(R.id.button1)
        home_button.setOnClickListener { v: View ->
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        /*
        val okHttpClient: OkHttpClient
        val builder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)
        okHttpClient = builder.build()

        val request =
                Request.Builder()
                        .get()
                        .url("http://coltrane.cs.seas.gwu.edu:8080/")
                        .build()

        val response: Response = okHttpClient.newCall(request).execute()
        val responseBody = response.body?.string()

        if(response.isSuccessful && !responseBody.isNullOrBlank()) {
            val json = JSONObject(responseBody)
            val results = json.getJSONArray("locations")
            for(i in 0 until results.length()) {
                val curr = results.getJSONObject(i)
                val latLng = LatLng()
                mMap.addMarker(MarkerOptions().position(FDR).title("FDR Statue").snippet("2"))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(FDR))
            }
        }
        */

        val FDR = LatLng(38.8837648, -77.044136)
        mMap.addMarker(MarkerOptions().position(FDR).title("FDR Statue").snippet("2"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(FDR))


        mMap.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                val intent = Intent(this, CameraActivity::class.java)
                intent.putExtra("Title", marker.title)
                startActivity(intent)
            }
            true
        }
    }
}