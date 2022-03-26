package com.example.happyplaces.activities.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyplaces.R
import com.example.happyplaces.activities.models.HappyPlacesModel
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        var happyPlacesDetailModel:HappyPlacesModel?=null

        if(intent.hasExtra(MainActivity.EXTRA_PLACES_DETAILD)){
            happyPlacesDetailModel = intent.getParcelableExtra(MainActivity.EXTRA_PLACES_DETAILD)
        }

        if(happyPlacesDetailModel!=null){
            setSupportActionBar(toolbar_happy_place_detail)
            if(supportActionBar!=null){
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            }
            toolbar_happy_place_detail.setNavigationOnClickListener {
                onBackPressed()
            }
            setImage.setImageURI(Uri.parse(happyPlacesDetailModel.image))
            setDescription.text = happyPlacesDetailModel.desc
            setLocation.text = happyPlacesDetailModel.location
        }

    }
}