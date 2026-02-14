package com.example.facerecoginationattendence.Domain

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.DatePicker
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facerecoginationattendence.Data.LocalDatabase.AppDatabase
import com.example.facerecoginationattendence.Data.LocalDatabase.Attendence
import com.example.facerecoginationattendence.Data.LocalDatabase.Class
import com.example.facerecoginationattendence.Data.LocalDatabase.Students
import com.example.facerecoginationattendence.Domain.Models.isSamePerson

import com.example.facerecoginationattendence.MyApp
import com.example.facerecoginationattendence.Presentation.StudentProfile
import getEmbeddingFromBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.time.LocalDate
import kotlin.jvm.optionals.getOrNull


class StudentSideVeiwModel(appLicationcontext: Context) : ViewModel() {

    var appLicationcontext = appLicationcontext

    var db = AppDatabase.getDatabase(appLicationcontext)


    val interpreter = MyApp.interpreter


//    fun AddStudent(Student : Students ) = viewModelScope.launch(){
//         face_detector(appLicationcontext,Student.imageBitmap!!,).collect {
//             if(it.isSuccess){
//                 var croppedImage = it.getOrNull()
//                 var embedding= getEmbeddingFromBitmap(croppedImage!!,interpreter)
//                 Log.d("photoembediing",embedding.joinToString(","))
//
//             }
//        }
//        //Student.PhotoEmbedding = getEmbeddingFromBitmap(Student.imageBitmap!!, interpreter)
//        //Log.d("photoembediing",croppedImage.toString())
//    }
    fun AddStudent(student: Students,imageBitmap : Bitmap?) = viewModelScope.launch(Dispatchers.IO) {
        imageBitmap?.let { bitmap ->
            // Call face_detector which returns a Flow, and collect the result.
            // No callback is needed here.
            Single_face_detector(appLicationcontext, bitmap.copy(Bitmap.Config.ARGB_8888, true)).collect { result ->
                if (result.isSuccess) {
                    val croppedImage = result.getOrNull()
                    if (croppedImage != null) {
                        // Successfully cropped a face, now generate the embedding.
                        val embedding = getEmbeddingFromBitmap(croppedImage, interpreter)
                        val embeddingString = embedding.joinToString(",")

                        // Assign the generated embedding back to the student object.
                        student.PhotoEmbedding = embedding
                        db.studentDao().InsertStudent(student)

                        Log.d("AddStudent", "Successfully generated embedding: $embeddingString")

                    } else {
                        Log.d("AddStudent", "Face detection ran, but no face was found in the image.")
                    }
                } else {
                    Log.e("AddStudent", "Face detection failed.", result.exceptionOrNull())
                }
            }
        } ?: Log.e("AddStudent", "Student imageBitmap is null, cannot add student.")
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun MarkAttendence(Class:String, image : Bitmap)= viewModelScope.launch() {
        var StudentList = db.studentDao().GetStudentByClass(Class)
        var embeddings : ArrayList<String>
        Multiple_face_detector(appLicationcontext,image).collect{
            if(it.isSuccess){
                Log.d("MarkAttendence",it.getOrNull().toString())

                var BitMapArray = it.getOrNull()
                if (BitMapArray != null){
                    embeddings = ArrayList()
                    for(bitmap in BitMapArray){
                        var embedding = getEmbeddingFromBitmap(bitmap,interpreter)
                        for(student in StudentList){
                            if(isSamePerson(embedding,student.PhotoEmbedding!!)){
                                db.attendanceDao().insertAttendance(Attendence(StudentID = student.StudentID, ClassName = Class, Status = true, Date = LocalDate.now().toString()))
                            }
                        }
                        Log.d("MarkAttendence",embedding.joinToString(","))
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

    fun AddClass(Class: Class) = viewModelScope.launch(Dispatchers.IO){
        db.classDao().InsertClass(Class);
    }

    fun getStudentAttendenceByMonth(studeniId:Long,month:String): List<Attendence>?{
        var attendence :List<Attendence>?=null
        viewModelScope.launch {
            attendence =db.attendanceDao().getMonthlyAttendance(studeniId,month)
        }
        return attendence
    }
    fun getStudentProfile(studentid:Long):Students{
        var SudentProfile : Students?=null
        viewModelScope.launch { SudentProfile = db.studentDao().GetStudent(studentid) }
        return SudentProfile!!
    }



















}




