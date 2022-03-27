package com.example.happyplaces.activities.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.happyplaces.activities.models.HappyPlacesModel

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context , DB_NAME , null , DB_VERSION ){
    companion object{
        private const val DB_NAME="PlaceDB"
        private const val DB_VERSION=1
        private const val TB_NAME="Places"
        private const val ID="id"
        private const val TITLE="title"
        private const val IMAGE="image"
        private const val DESC="description"
        private const val DATE="date"
        private const val LOCATION="location"
        private const val LATITUDE="latitude"
        private const val LONGITUDE="longitude"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val query="CREATE TABLE $TB_NAME($ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$TITLE TEXT , $IMAGE TEXT , $DESC TEXT , $DATE TEXT , $LOCATION TEXT , $LATITUDE DOUBLE , $LONGITUDE DOUBLE )"
        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        var query="DROP TABLE $TB_NAME IF Exists"
        p0?.execSQL(query)
        onCreate(p0)
    }

    fun insert(p:HappyPlacesModel):Long
    {
        val db=writableDatabase
        val cv= ContentValues()
        cv.put(TITLE,p.title)
        cv.put(IMAGE,p.image)
        cv.put(DESC,p.desc)
        cv.put(DATE,p.date)
        cv.put(LOCATION,p.location)
        cv.put(LATITUDE,p.latitude)
        cv.put(LONGITUDE,p.longitude)

        val flag=db.insert(TB_NAME,null , cv)
        db.close()
        return flag
    }

    fun update(p:HappyPlacesModel):Int
    {
        val db=writableDatabase
        val cv= ContentValues()
        cv.put(TITLE,p.title)
        cv.put(IMAGE,p.image)
        cv.put(DESC,p.desc)
        cv.put(DATE,p.date)
        cv.put(LOCATION,p.location)
        cv.put(LATITUDE,p.latitude)
        cv.put(LONGITUDE,p.longitude)

        val success = db.update(TB_NAME , cv , ID + "=" + p.id , null)
        db.close()
        return success
    }

    fun delete(happyPlacesModel: HappyPlacesModel):Int{
        val db=writableDatabase
        val delete = db.delete(TB_NAME , ID + "=" + happyPlacesModel.id  , null)
        db.close()
        return delete
    }


    fun retriveAll():ArrayList<HappyPlacesModel>
    {
        val arr=ArrayList<HappyPlacesModel>()
        val db=readableDatabase
        val cursor=db.query(TB_NAME,null,null,null,null,null,null)
        while(cursor.moveToNext())
        {

            val id=cursor.getInt(0)
            val title = cursor.getString(1)
            val image = cursor.getString(2)
            val desc = cursor.getString(3)
            val date = cursor.getString(4)
            val location = cursor.getString(5)

            val p = HappyPlacesModel(id,title,image, desc , date , location , 0.0 , 0.0)
            arr.add(p)
        }
        return arr

    }
}