package com.example.facerecoginationattendence.Domain

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.facerecoginationattendence.Domain.Models.Students
import com.example.facerecoginationattendence.MyApp
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class StudentSideVeiwModel(context: Context) : ViewModel() {

    val interpreter = MyApp.interpreter


    fun AddStudent(Student : Students ) {}
    fun MarkAttendence(Class:String , image : Bitmap){}


















}



