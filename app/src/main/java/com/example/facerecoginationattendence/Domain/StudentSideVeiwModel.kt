package com.example.facerecoginationattendence.Domain

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.facerecoginationattendence.Domain.Models.Students
import com.example.facerecoginationattendence.MyApp
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class StudentSideVeiwModel(appLicationcontext: Context) : ViewModel() {

    var appLicationcontext = appLicationcontext


    val interpreter = MyApp.interpreter


    fun AddStudent(Student : Students ) {
        var croppedImage = ML_Kit_Face_Detection.AddStudent(Student.imageBitmap!!,0)
        Student.PhotoEmbedding = getEmbeddingFromBitmap(croppedImage!!, interpreter)
        Log.d("photoembediing",Student.PhotoEmbedding.toString())
    }
    fun MarkAttendence(Class:String , image : Bitmap){}


















}



