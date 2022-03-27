package com.example.happyplaces.activities.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.R
import com.example.happyplaces.activities.Adapter.PlacesAdapter
import com.example.happyplaces.activities.Database.DatabaseHelper
import com.example.happyplaces.activities.models.HappyPlacesModel
import com.happyplaces.utils.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_main.*
import pl.kitek.rvswipetodelete.SwipeToEditCallback

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
        val db = DatabaseHelper(applicationContext)
        val list :ArrayList<HappyPlacesModel> = db.retriveAll()

        if(list.size > 0){
            recView.visibility = View.VISIBLE
            textView.visibility = View.GONE
            setupRecyclerView(list)
        }
        else{
            recView.visibility = View.GONE
            textView.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView(happyPlacesList:ArrayList<HappyPlacesModel>){
        val adapter = PlacesAdapter(happyPlacesList)
        recView.layoutManager = LinearLayoutManager(this)
        recView.setHasFixedSize(true)
        recView.adapter = adapter

        adapter.setOnclickListener(object:PlacesAdapter.OnclickListener{
            override fun onClick(position: Int, model: HappyPlacesModel) {
                val intent = Intent(this@MainActivity , DetailsActivity::class.java)
                intent.putExtra(EXTRA_PLACES_DETAILED , model)
                startActivity(intent)
            }
        })

        val editSwipeHandler = object :SwipeToEditCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recView.adapter as PlacesAdapter
                adapter.notifyEditItem(applicationContext , this@MainActivity , viewHolder.adapterPosition ,
                    ADD_PLACES_DETAILS )
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(recView)


        val deleteSwipeHandler = object :SwipeToDeleteCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recView.adapter as PlacesAdapter
                adapter.notifyDeleteItem(applicationContext ,this@MainActivity , viewHolder.adapterPosition)
            }
        }

        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(recView)
    }

    companion object{
        var EXTRA_PLACES_DETAILED = "extra_place_details"
        var ADD_PLACES_DETAILS = 2
    }
}