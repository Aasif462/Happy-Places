package com.example.happyplaces.activities.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyplaces.R
import com.example.happyplaces.activities.Adapter.PlacesAdapter
import com.example.happyplaces.activities.Database.DatabaseHelper
import com.example.happyplaces.activities.models.HappyPlacesModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this , AddHappyPlaces::class.java))
        }

        displayData()
    }

    private fun displayData(){
        val db = DatabaseHelper(this)
        val arr:ArrayList<HappyPlacesModel> = db.retriveAll()
        val adapter = PlacesAdapter(arr)
        recView.layoutManager = LinearLayoutManager(this)
        recView.adapter = adapter

        adapter.setOnclickListener(object:PlacesAdapter.OnclickListner{
            override fun onClick(position: Int, model: HappyPlacesModel) {
                val intent = Intent(this@MainActivity , DetailsActivity::class.java)
                intent.putExtra(EXTRA_PLACES_DETAILD , model)
                startActivity(intent)
            }
        })
    }
    companion object{
        var EXTRA_PLACES_DETAILD = "extra_place_details"
    }
}