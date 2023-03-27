package com.example.traveltrails

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
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
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import org.json.JSONObject


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var home_button: ImageButton
    private lateinit var currentLocation: ImageButton
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        home_button = findViewById(R.id.button1)
        home_button.setOnClickListener { v: View ->
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    GoogleLocationActivity.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        } else {
            getCurrentLocation()
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

        val okHttpClient: OkHttpClient
        val builder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)
        okHttpClient = builder.build()

        doAsync {
            val request =
                    Request.Builder()
                            .get()
                            .url("http://coltrane.cs.seas.gwu.edu:8080/locations")
                            .build()

            val response: Response = okHttpClient.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                val json = JSONArray(responseBody)
             //   val results = json.getJSONArray("")
                for (i in 0 until json.length()) {
                    val curr = json.getJSONObject(i)
                    val name: String = curr.getString("name").toString()
                    val modelID: String = curr.getString("id").toString()
                    val lat: Double = (curr.getDouble("latitude"))
                    val lon: Double = (curr.getDouble("longitude"))
                    var latLng = LatLng(lat, lon)
                    runOnUiThread {
                        if(name != "FDR Memorial" && name != "Washington Monument")
                            mMap.addMarker(MarkerOptions().position(latLng).title(name).snippet(modelID))
                    }
                }
            }
        }

        mMap.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                val intent = Intent(this, CameraActivity::class.java)
                intent.putExtra("ModelID", marker.snippet)
                intent.putExtra("Title", marker.title)
                if(marker.title != "Your Location")
                    startActivity(intent)
                else
                    marker.showInfoWindow()
            }
            true
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            val latitude = location.latitude
                            val longitude = location.longitude
                            Toast.makeText(this, "Latitude: $latitude\nLongitude: $longitude", Toast.LENGTH_SHORT).show()
                            mMap.addMarker(MarkerOptions().position(LatLng(latitude, longitude)).title("Your Location"))?.showInfoWindow()
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)))
                            mMap.setMinZoomPreference(5.0F)
                        } else {
                            Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show()
                        }
                    }
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getCurrentLocation()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100
    }
}