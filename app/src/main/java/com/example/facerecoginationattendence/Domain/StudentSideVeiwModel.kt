package com.example.facerecoginationattendence.Domain

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facerecoginationattendence.Domain.Models.Students
import com.example.facerecoginationattendence.MyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class StudentSideVeiwModel(appLicationcontext: Context) : ViewModel() {

    var appLicationcontext = appLicationcontext


    val interpreter = MyApp.interpreter


    fun AddStudent(Student : Students ) = viewModelScope.launch(){
         ML_Kit_Face_Detection.AddStudent(Student.imageBitmap!!,0).collect {
             if(it.isSuccess){
                 var croppedImage = it.getOrNull()
                 var embedding= getEmbeddingFromBitmap(croppedImage!!,interpreter)
                 Log.d("photoembediing",embedding.joinToString(","))

             }
        }
        //Student.PhotoEmbedding = getEmbeddingFromBitmap(Student.imageBitmap!!, interpreter)
        //Log.d("photoembediing",croppedImage.toString())
    }
    fun MarkAttendence(Class:String , image : Bitmap)= viewModelScope.launch() {
        var embeddings : ArrayList<String>
        ML_Kit_Face_Detection.MarkAttendence(image,0).collect{
            if(it.isSuccess){
                Log.d("MarkAttendence",it.getOrNull().toString())

                var BitMapArray = it.getOrNull()
                if (BitMapArray != null){
                    embeddings = ArrayList()
                    for(bitmap in BitMapArray){
                        var embedding = getEmbeddingFromBitmap(bitmap,interpreter)
                        embeddings?.add(embedding.joinToString (","))


                    }
                    Log.d("MarkAttendenceaa",embeddings.toString())


                }
                else{
                    Log.d("MarkAttendence","BitMapArray is null")

                }
            }
            else{
                Log.d("MarkAttendence","no success"+it.exceptionOrNull().toString())

            }

        }
    }


















}




