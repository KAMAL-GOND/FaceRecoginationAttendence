package com.example.facerecoginationattendence.Data.LocalDatabase

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity
data class StudentTable(
    @PrimaryKey(autoGenerate = true) val StudentID: Long,
    val name: String,
    val rollNo: String,
    val Class: String,

    @TypeConverters(FloatArrayConvertor ::class)
    val PhotoEmbedding: FloatArray,
    @Ignore
    var imageBitmap: Bitmap? = null
)

@Entity
data class ClassTable(
    @PrimaryKey(autoGenerate = true) val ClassId : Long,
    val ClassName : String
)

@Entity(
    indices = [Index(value = ["StudentID"]),
    Index(value = ["ClassID"]),
    Index(value = ["Date"])
        
    ]
)
data class AttendenceTable(
    @PrimaryKey(autoGenerate = true) val AttendenceID : Long,
    val StudentID : Long,
    val ClassID : Long,
    val Date : String,
    val Status : Boolean
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
