package com.example.facerecoginationattendence.Data.LocalDatabase

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity
data class Students(
    @PrimaryKey(autoGenerate = true) val StudentID: Long,
    var name: String?,
    //var rollNo: Int ?,
    var Class: String?,


    @TypeConverters(FloatArrayConvertor ::class)
    var PhotoEmbedding: FloatArray? = null,
    @Ignore
    var imageBitmap: Bitmap? = null
)

@Entity
data class Class(
    @PrimaryKey(autoGenerate = true) var ClassId : Long=0L,
    var ClassName : String?
)

@Entity(
    indices = [Index(value = ["StudentID"]),
    Index(value = ["ClassID"]),
    Index(value = ["Date"])
        
    ]
)
data class Attendence(
    @PrimaryKey(autoGenerate = true) val AttendenceID : Long=0L,
    var StudentID : Long,
    var ClassID : Long,
    var Date : String?,
    var Status : Boolean?
)

// TypeConvertors
class  FloatArrayConvertor{
    @TypeConverter
    fun fromFloatArray(array: FloatArray): String {
        return array.joinToString(",")
    }
    @TypeConverter
    fun toFloatArray(string: String): FloatArray {
        return string.split(",").map { it.toFloat() }.toFloatArray()
    }


}

// TypeConvertor
