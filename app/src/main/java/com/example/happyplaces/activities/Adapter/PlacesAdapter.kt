package com.example.happyplaces.activities.Adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.activities.models.HappyPlacesModel
import com.example.happyplaces.databinding.PlacesLayoutBinding

class PlacesAdapter(val places:ArrayList<HappyPlacesModel>) :RecyclerView.Adapter<PlacesAdapter.PlacesHolder>(){

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