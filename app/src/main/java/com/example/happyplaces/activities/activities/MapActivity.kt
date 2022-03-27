package com.example.happyplaces.activities.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyplaces.R
import com.example.happyplaces.activities.models.HappyPlacesModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_add_happy_places.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback{

    private var mHappyDetails:HappyPlacesModel ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        if(intent.hasExtra(MainActivity.EXTRA_PLACES_DETAILED)){
            mHappyDetails = intent.getParcelableExtra(MainActivity.EXTRA_PLACES_DETAILED)
        }

        if(mHappyDetails != null){
            setSupportActionBar(toolbar)
            if(supportActionBar!=null){
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                toolbar.title = mHappyDetails!!.title

                toolbar.setNavigationOnClickListener {
                    onBackPressed()
                }

                val supportMapFragment:SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                supportMapFragment.getMapAsync(this)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val position = LatLng(mHappyDetails!!.latitude , mHappyDetails!!.longitude)
        googleMap.addMarker(MarkerOptions().position(position).title(mHappyDetails!!.title))
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(position , 15f)
        googleMap.animateCamera(newLatLngZoom)
    }
}