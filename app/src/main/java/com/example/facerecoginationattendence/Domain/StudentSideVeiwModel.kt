package com.example.facerecoginationattendence.Domain

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.facerecoginationattendence.Domain.Models.Students

class StudentSideVeiwModel : ViewModel() {

    fun AddStudent(Student : Students ) {}
    fun MarkAttendence(Class:String , image : Bitmap){}

}