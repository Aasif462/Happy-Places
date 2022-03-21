package com.example.happyplaces

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_add_happy_places.*
import java.text.SimpleDateFormat
import java.util.*

class AddHappyPlaces : AppCompatActivity() , View.OnClickListener {

    private var calendar = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_happy_places)

        setSupportActionBar(toolbar)
        if(supportActionBar!=null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        dateSetListener = DatePickerDialog.OnDateSetListener{
            view , year , month , dayOfMonth ->
            calendar.set(Calendar.YEAR , year)
            calendar.set(Calendar.MONTH , month)
            calendar.set(Calendar.DAY_OF_MONTH , dayOfMonth)
            displayDate()

            dateEdt.setOnClickListener(this)
            addImage.setOnClickListener(this)

        }
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.dateEdt ->
            {
                DatePickerDialog(this@AddHappyPlaces ,
                    dateSetListener
                    , calendar.get(Calendar.YEAR)
                    , calendar.get(Calendar.MONTH)
                    ,calendar.get(Calendar.DAY_OF_MONTH)).show()
            }

            R.id.addImageTxt ->
            {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Select Action")
                val pictureDialogItems = arrayOf("Select Photo From Gallery" , "Capture Photo From Camara")
                dialog.setItems(pictureDialogItems){
                    dialog,which ->
                    when(which){
                        0 -> choosePhotoFromGallery()
                        1 -> choosePhotoFromCamara()
                    }
                }
                dialog.show()
            }
        }
    }

    private fun displayDate(){
        val myFormat = "dd / MM / yyyy"
        val sdf = SimpleDateFormat(myFormat , Locale.getDefault())
        dateEdt.setText(sdf.format(calendar.time).toString())
    }

    private fun choosePhotoFromGallery(){
        Dexter.withContext(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE
           ,Manifest.permission.WRITE_EXTERNAL_STORAGE
           ,Manifest.permission.CAMERA
        ).withListener(object:MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if(report!!.areAllPermissionsGranted()){
                    Toast.makeText(applicationContext , "All Permissions Granted", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permisions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {

            }

        }).check()

    }

    private fun choosePhotoFromCamara(){

    }
}