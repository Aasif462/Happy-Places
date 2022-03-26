package com.example.happyplaces.activities.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class HappyPlacesModel(
    val title:String?,
    val image:String?,
    val desc:String?,
    val date:String?,
    val location:String?,
    val latitude:Double,
    val longitude:Double
):Parcelable
{
    var id = 0

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
        id = parcel.readInt()
    }

    constructor(id :Int , title:String , image:String , desc:String , date: String
                ,location: String,latitude: Double,longitude: Double)
            :this(title , image , desc , date , location , latitude,longitude)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(image)
        parcel.writeString(desc)
        parcel.writeString(date)
        parcel.writeString(location)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HappyPlacesModel> {
        override fun createFromParcel(parcel: Parcel): HappyPlacesModel {
            return HappyPlacesModel(parcel)
        }

        override fun newArray(size: Int): Array<HappyPlacesModel?> {
            return arrayOfNulls(size)
        }
    }
}