package com.example.happyplaces.activities.Utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import java.lang.StringBuilder
import java.util.*

class GetAddressFromLatLong(context: Context , private val latitude:Double , private val longitude:Double) :AsyncTask<Void , String , String>() {

    private val geoCoder = Geocoder(context, Locale.getDefault())
    private lateinit var mAddressListner:AddressListner
    override fun doInBackground(vararg p0: Void?): String {

        try {


            val addressList: List<Address>? = geoCoder.getFromLocation(latitude, longitude, 1)

            if (addressList!!.isNotEmpty() && addressList != null) {
                val address: Address = addressList[0]
                val sb = StringBuilder()

                for (i in 0..address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i)).append("")
                }
                sb.deleteCharAt(sb.length - 1)
                return sb.toString()
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        return ""
    }

    override fun onPostExecute(result: String?) {
        if(result == null){
            mAddressListner.onError()
        }
        else{
            mAddressListner.onAddressFound(result)
        }
        super.onPostExecute(result)

    }

    fun setAddressListener(addressListener: AddressListner){
        mAddressListner = addressListener
    }

    fun getAddress(){
        execute()
    }

    interface AddressListner{
        fun onAddressFound(address:String?)
        fun onError()
    }
}