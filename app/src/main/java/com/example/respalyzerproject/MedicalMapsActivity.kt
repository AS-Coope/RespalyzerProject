package com.example.respalyzerproject

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.respalyzerproject.databinding.ActivityMedicalMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
//import com.example.map_custom_marker.databinding.ActivityMapsBinding

class MedicalMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMedicalMapsBinding

    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode =101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMedicalMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        getCurrentLocationUser()
    }

    class MedicalCenter(name: String, location: LatLng) {
        val hosp_name=name
        val hosp_location=location

    }
    
    val MedicalCenters
        get() = listOf(
            MedicalCenter("Noel Holmes Hospital", LatLng(18.45169954182161, -78.16801457670194)),
            MedicalCenter("Savanna-La-Mar Hospital", LatLng(18.226152868732573, -78.12900512948241)),
            MedicalCenter("Cornwall Regional Hospital", LatLng(18.470663180591878, -77.90997304023793)),
            MedicalCenter("Falmouth Hospital", LatLng(18.49671723487968, -77.66115590931446)),
            MedicalCenter("Mandeville Hospital", LatLng(18.042816248228185, -77.5091725587995)),
            MedicalCenter("Port Maria Hospital", LatLng(18.35849890743657, -76.89517403558202)),
            MedicalCenter("Annotto Bay Hospital", LatLng(18.270641023884465, -76.77040569485729)),
            MedicalCenter("Port Antonio Hospital", LatLng(18.176546988991348, -76.4560944048941)),
            MedicalCenter("Princess Margaret Hospital", LatLng(17.880869824218568, -76.39047145363735)),
            MedicalCenter("Percy Junior Community Hospital", LatLng(18.155419758272206, -77.46483564378326)),
            MedicalCenter("Lionel Town Community Hospital", LatLng(17.80804759570096, -77.2413314048941)),
            MedicalCenter("Linstead Hospital", LatLng(18.13337671979542, -77.03173479140207)),
            MedicalCenter("Spanish Town Hospital", LatLng(17.992729899766314, -76.94812382023805)),
            MedicalCenter("St. Ann's Bay Hospital", LatLng(18.437382796994015, -77.20996756256609)),
            MedicalCenter("May Pen Hospital", LatLng(17.96485702688156, -77.24334082023806)),
            MedicalCenter("Victoria Jubilee Maternity Hospital", LatLng(17.9767389117795, -76.79479719140207)),
            MedicalCenter("Kingston Public Hospital", LatLng(17.97724954335577, -76.79497070611954)),
            MedicalCenter("Andrews Memorial Seventh-Day Adventist Hospital", LatLng(18.02055912091625, -76.77598757327554)),
            MedicalCenter("University Hospital of the West Indies", LatLng(18.006200310100592, -76.7381904784387)),
            MedicalCenter("Bustamante Hospital for Children", LatLng(17.99969187130106, -76.77821123743391)),
            MedicalCenter("National Chest Hospital", LatLng(18.022923325704806, -76.76128685475868)),
            MedicalCenter("Black River Hospital", LatLng(18.0267645393253, -77.85894774722213))
        )




    private fun getCurrentLocationUser() {
        if(ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION)!=
            PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),permissionCode)
            return
        }

        val getLocation= fusedLocationProviderClient.lastLocation.addOnSuccessListener {

            location ->

            if(location != null){

                currentLocation =location

                Toast.makeText(applicationContext, currentLocation.latitude.toString() + "" +
                currentLocation.longitude.toString(), Toast.LENGTH_LONG).show()

                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)

            }
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){

            permissionCode -> if(grantResults.isNotEmpty() && grantResults[0]==
                    PackageManager.PERMISSION_GRANTED){

                getCurrentLocationUser()
            }
        }
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
        
        for (MedicalCenter in MedicalCenters) {
            mMap.addMarker(
                MarkerOptions()
                    .position(MedicalCenter.hosp_location)
                    .title(MedicalCenter.hosp_name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
        }


        val latLng =LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions= MarkerOptions().position(latLng).title("Current Location")
        val userLocation = LatLng(currentLocation.latitude, currentLocation.longitude)
        // Add markers for medical centers
//        markerOptions.icon(BitmapFromVector(getApplicationContext(), R.drawable.outline_local_hospital_24))
        //add marker
        mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10f))

        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
        googleMap?.addMarker(markerOptions)
    }

    private fun BitmapFromVector(context: Context, vectorResId:Int): BitmapDescriptor? {
        //drawable generator
        var vectorDrawable: Drawable
        vectorDrawable= ContextCompat.getDrawable(context,vectorResId)!!
        vectorDrawable.setBounds(0,0,vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight)
        //bitmap genarator
        var bitmap: Bitmap
        bitmap= Bitmap.createBitmap(vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
        //canvas genaret
        var canvas: Canvas
        //pass bitmap in canvas constructor
        canvas= Canvas(bitmap)
        //pass canvas in drawable
        vectorDrawable.draw(canvas)
        //return BitmapDescriptorFactory
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}


