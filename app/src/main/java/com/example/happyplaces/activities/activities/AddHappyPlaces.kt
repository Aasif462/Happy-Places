package com.example.happyplaces.activities.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.appcompat.app.AppCompatActivity
import com.example.happyplaces.R
import com.example.happyplaces.activities.Database.DatabaseHelper
import com.example.happyplaces.activities.models.HappyPlacesModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_add_happy_places.*
import kotlinx.android.synthetic.main.places_layout.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddHappyPlaces : AppCompatActivity() , View.OnClickListener {

    private var calendar = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
//    private lateinit var saveImagetoInternal:Uri
    private var mLatitude = 0.0
    private var mLongitude = 0.0
    private var saveImageToInternalStorage: Uri? = null


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

        }
        displayDate()
        dateEdt.setOnClickListener(this)
        placeImage.setOnClickListener(this)
        saveBtn.setOnClickListener(this)
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
            R.id.saveBtn ->
            {
                val checkValues = checkValues()
                if(checkValues){
                    insertData()
                    startActivity(Intent(applicationContext , MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun displayDate(){
        val myFormat = "dd / MM / yyyy"
        val sdf = SimpleDateFormat(myFormat , Locale.getDefault())
        dateEdt.setText(sdf.format(calendar.time).toString())
    }



    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        // Here this is used to get an bitmap from URI
                        @Suppress("DEPRECATION")
                        val selectedImageBitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)

                        saveImageToInternalStorage =
                            saveImageToInternalStorage(selectedImageBitmap)
//                        Log.e("Saved Image : ", "Path :: $saveImageToInternalStorage")

                        placeImage!!.setImageBitmap(selectedImageBitmap) // Set the selected image from GALLERY to imageView.
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(applicationContext, "Failed!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else if (requestCode == CAMERA) {

                val thumbnail: Bitmap = data!!.extras!!.get("data") as Bitmap // Bitmap from camera

                saveImageToInternalStorage =
                    saveImageToInternalStorage(thumbnail)
                Log.e("Saved Image : ", "Path :: $saveImageToInternalStorage")

                placeImage!!.setImageBitmap(thumbnail) // Set to the imageView.
            }
        }
    }

    private fun choosePhotoFromGallery() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                    // Here after all the permission are granted launch the gallery to select and image.
                    if (report!!.areAllPermissionsGranted()) {

                        val galleryIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )

                        startActivityForResult(galleryIntent, GALLERY)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialog()
                }
            }).onSameThread()
            .check()
    }

    private fun showRationalDialog(){
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage("It Looks Like You have turned off Permission required"+
                "For this Feature. It can be enabled Under the "+"Application Settings")
        dialog.setPositiveButton("GO TO SETTINGS"){
            dialog_,which ->
            try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package",packageName ,null)
                    intent.data = uri
                    startActivity(intent)
                }
                catch (e:ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
        dialog.setNegativeButton("Cancel"){
            dialog,_ ->
                dialog.dismiss()
        }
        dialog.show()

    }

    private fun choosePhotoFromCamara() {

        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    // Here after all the permission are granted launch the CAMERA to capture an image.
                    if (report!!.areAllPermissionsGranted()) {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(intent, CAMERA)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialog()
                }
            }).onSameThread()
            .check()
    }

    companion object{
        private const val GALLERY = 1
        private const val CAMERA = 2
        private const val IMAGE_DIRECTORY = "HappyPlacesImages"
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {

        // Get the context wrapper instance
        val wrapper = ContextWrapper(applicationContext)

        // Initializing a new file
        // The bellow line return a directory in internal storage
        /**
         * The Mode Private here is
         * File creation mode: the default mode, where the created file can only
         * be accessed by the calling application (or all applications sharing the
         * same user ID).
         */
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)

        // Create a file to save the image
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException) { // Catch the exception
            e.printStackTrace()
        }

        // Return the saved image uri
        return Uri.parse(file.absolutePath)
    }

    private fun insertData(){
        val title = titleEdt.text.toString()
        val desc = descEdt.text.toString()
        val date = dateEdt.text.toString()
        val location = locationEdt.text.toString()
        val image = saveImageToInternalStorage.toString()

        val items = HappyPlacesModel(title , image , desc,date,location, mLatitude, mLongitude )
        val db = DatabaseHelper(applicationContext)
        val flag = db.insert(items)

        if(flag > 0){
            Toast.makeText(applicationContext, "Insert Successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkValues() : Boolean{
        var check:Boolean = false
        if(titleEdt.text!!.isEmpty()){
            titleEdt.error = "Please Enter Valid Title!"
        }

        else if(descEdt.text!!.isEmpty()){
            descEdt.error = "Please Enter Valid Description!"
        }

        else if(locationEdt.text!!.isEmpty()){
            locationEdt.error = "Please Enter Valid Location!"
        }
        else{
            check = true
        }
        return check
    }

}