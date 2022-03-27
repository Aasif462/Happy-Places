package com.example.happyplaces.activities.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.activities.Database.DatabaseHelper
import com.example.happyplaces.activities.activities.AddHappyPlaces
import com.example.happyplaces.activities.activities.MainActivity
import com.example.happyplaces.activities.models.HappyPlacesModel
import com.example.happyplaces.databinding.PlacesLayoutBinding

class PlacesAdapter( private val places:ArrayList<HappyPlacesModel>) : RecyclerView.Adapter<PlacesAdapter.PlacesHolder>(){

    private var onclickListener:OnclickListener ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesHolder {
        return PlacesHolder(PlacesLayoutBinding.inflate(LayoutInflater.from(parent.context)))
    }

    fun setOnclickListener(onclickListener: OnclickListener){
        this.onclickListener = onclickListener
    }

    override fun onBindViewHolder(holder: PlacesHolder, position: Int) {
        val model = places[position]
        holder.image.setImageURI(Uri.parse(model.image))
        holder.placeName.text = model.title
        holder.description.text = model.desc

        holder.card.setOnClickListener{
            if(onclickListener!=null){
                onclickListener!!.onClick(position , model)
            }
        }
    }

    fun notifyEditItem(context: Context,activity: Activity, position: Int, requestCode:Int){
        val intent = Intent(context , AddHappyPlaces::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACES_DETAILED , places[position])
        activity.startActivityForResult(intent,requestCode)
        notifyItemChanged(position)
    }

    fun notifyDeleteItem(context: Context ,  activity: Activity , position: Int){
        val db = DatabaseHelper(context)
        val isDelete = db.delete(places[position])
        if(isDelete >0){
            places.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return places.size
    }

    interface OnclickListener{
        fun onClick(position: Int , model: HappyPlacesModel)
    }

    class PlacesHolder(binding:PlacesLayoutBinding):RecyclerView.ViewHolder(binding.root){
        val image: ImageView = binding.profileImage
        val placeName = binding.placeName
        val description = binding.description
        val card = binding.card
    }
}