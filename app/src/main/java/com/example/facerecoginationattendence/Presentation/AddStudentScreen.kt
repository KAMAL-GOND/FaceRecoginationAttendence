package com.example.facerecoginationattendence.Presentation

import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person

import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.facerecoginationattendence.Data.LocalDatabase.AppDatabase
import com.example.facerecoginationattendence.Data.LocalDatabase.Students

import com.example.facerecoginationattendence.Domain.StudentSideVeiwModel
import com.example.facerecoginationattendence.Presentation.navigation.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.R)
@Composable
// FOR SINGLE STUDENT ADD
fun AddStudentScreen(veiwModel: StudentSideVeiwModel){
    val db = AppDatabase.getDatabase(veiwModel.appLicationcontext)
    var classes by remember { mutableStateOf<List<com.example.facerecoginationattendence.Data.LocalDatabase.Class>>(emptyList()) }
    var dropDownMenuExpansion by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        classes = withContext(Dispatchers.IO) { db.classDao().GetAllClass() }
    }

    val imageBitmapState = remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    var showPhotoManager by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var rollNo by remember { mutableStateOf("") }
    var ClassName by remember { mutableStateOf("") }

    var AddStudentStatus =  veiwModel.AddStudentSuccessFLow.collectAsState();
    if(
        AddStudentStatus.value!=null
    ){
        Toast.makeText(LocalContext.current,AddStudentStatus.value.toString(),Toast.LENGTH_LONG).show()
        veiwModel.resetAddStudentSuccessValue();
    }

    // FUNCTION TO PASS FOR MAKING SHOW POHTOTO MANAGER TRUE SO THAT AGAIN , IT DONT REMAIN TRUE WHEN  BOTTOM SHEET IS CLOSSE , FOR MAKING NEXT ATTEMPT TO SHOW PHOTO MANAGER

    fun OnDismiss(){
        showPhotoManager = false
    }
   


    if (showPhotoManager) {

        val returnedBitmap = PhotoManager(context = context, OnDismiss = {OnDismiss()},veiwModel.appLicationcontext)


        LaunchedEffect(returnedBitmap) {
            if (returnedBitmap != null) {
                imageBitmapState.value = returnedBitmap

                showPhotoManager = false
            }
            else{
                //Toast.makeText(context,"No Image Selected", Toast.LENGTH_SHORT).show()
                //showPhotoManager = false
            }

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Box(modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .border(2.dp, Color.Gray, CircleShape)
            .clickable {

                showPhotoManager = true
            }
        ) {
            if (imageBitmapState.value != null) {
                Image(
                    bitmap = imageBitmapState.value!!.asImageBitmap(),
                    contentDescription = "Student Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Add Photo Placeholder",
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.Center)
                )

            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = name, onValueChange = {name = it}, label = { Text(text = "Student Name")})
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = rollNo, onValueChange = {rollNo = it.toString()}, label = { Text(text = "Roll No")}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        Spacer(modifier = Modifier.height(16.dp))
        Box {
            OutlinedTextField(
                modifier = Modifier.clickable(onClick = { dropDownMenuExpansion = true }),
                value = ClassName,
                readOnly = true,
                onValueChange = { ClassName = it },
                label = { Text(text = "Class") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                        modifier = Modifier.clickable { dropDownMenuExpansion = true }
                    )
                }
            )
            DropdownMenu(expanded = dropDownMenuExpansion, onDismissRequest = {dropDownMenuExpansion = false}) {
                classes.forEach { a->
                    DropdownMenuItem(
                        text = {Text(a.ClassName.toString())},
                        onClick = {ClassName=a.ClassName.toString()
                            dropDownMenuExpansion = false}
                    )
                }
            }
        }


        if(name.isNotEmpty() && rollNo.isNotEmpty() && ClassName.isNotEmpty() && imageBitmapState.value != null){
            Button(onClick = {veiwModel.AddStudent(
                Students(
                    name = name,
                    //rollNo = rollNo,
                    Class = ClassName,

                    StudentID = rollNo.toLong(),
                    //PhotoEmbedding = TODO(),
                ),imageBitmap = imageBitmapState.value,
            )  }) {
                Text(text = "Add Student")
            }
        }

    }
}
