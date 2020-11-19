package com.example.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    //google map
    private lateinit var mMap: GoogleMap

    //permissions
    private val REQUEST_LOCATION_PERMISSION = 1

    //request location permission
    private val REQUEST_CHECK_SETTINGS = 2

    // FusedLocationProviderClient - Main class for receiving location updates
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // LocationRequest - Requirements for location updates
    private lateinit var locationRequest: LocationRequest

    // LocationCallback - Called when FusedLocationProviderClient has a new Location
    private lateinit var locationCallback: LocationCallback

    //last known location
    private var currentLocation: Location? = null

    //marker
    private var marker: Marker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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
        Log.i("map", "in onmapready")
        mMap = googleMap

        //set map type
        mMap.mapType=GoogleMap.MAP_TYPE_HYBRID

        //change to when they tap the button
        enableMyLocation()

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            createLocationRequest()
        }
        else {
            //permission not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // returns true if the app has requested this permission previously and the user denied the request

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle(R.string.dialogTitle).setMessage(R.string.dialogMessage)
                alertDialog.apply {
                    setPositiveButton(R.string.dialogOK, DialogInterface.OnClickListener { dialog, which ->
                        ActivityCompat.requestPermissions(this@MapsActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
                    })
                    setNegativeButton(R.string.dialogCancel, DialogInterface.OnClickListener {dialog, which ->
                        dialog.dismiss()
                    })
                }
                alertDialog.create().show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_LOCATION_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted
                    enableMyLocation()
                } else {
                    Snackbar.make(findViewById(R.id.map), "Location access denied", Snackbar.LENGTH_SHORT).show()
                }
            }
        //add other statements to handle other permissions your app might request
        }
    }

    private fun createLocationRequest(){
        // initialize LocationRequest object
        locationRequest = LocationRequest()
        //set the desired interval for location updates, in milliseconds
        locationRequest.interval = 10000
        //set the fastest interval for location updates, in milliseconds
        locationRequest.fastestInterval = 500
        //set priority of the request
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        // create LocationSettingsRequest object using location request object
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        // create a settings client and a task to check location settings.
        val settingsClient = LocationServices.getSettingsClient(this)
        val task = settingsClient.checkLocationSettings(builder.build())

        //start location updates if successful
        task.addOnSuccessListener {
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            // failure means the location settings have some issues
            if (e is ResolvableApiException) {
                // Location settings are not satisfied
                // prompt user to enable location in Settings
                try {
                    // prompts user to enable location permission
                    e.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates(){
        if (mMap.isMyLocationEnabled) {
            //initialize the FusedLocationProviderClient
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            //define location callback
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    onLocationChanged(locationResult.lastLocation)
                }
            }
            //request location updates
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    private fun onLocationChanged(location: Location){
        if (location != null) {
            currentLocation = location
            //define an object of the Google LatLng class with location coordinates
            val currentLatLng = LatLng(location.latitude, location.longitude)
            //check to see if there's a current marker
            if (marker == null){
                //Create a MarkerOptions object
                var markerOptions = MarkerOptions()
                //set the position to the user’s current location
                markerOptions.position(currentLatLng)
                //set the title
                markerOptions.title("you are here")
                //add marker to the map using the marker options and assign to our marker
                marker = mMap.addMarker(markerOptions)
            } else {
                //change the position of the current marker
                marker!!.position = currentLatLng
            }
            //move the map camera to the new position and set the zoom
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
        }
    }
}