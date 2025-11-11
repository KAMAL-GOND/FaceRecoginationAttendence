package com.example.facerecoginationattendence.Presentation


import android.graphics.Bitmap
import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.facerecoginationattendence.Domain.Models.Students
import com.example.facerecoginationattendence.Domain.StudentSideVeiwModel

@Composable
fun MarkAttendenceScreen(veiwModel: StudentSideVeiwModel){
    val imageBitmapState = remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    var showPhotoManager by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var rollNo by remember { mutableStateOf("") }
    var Class by remember { mutableStateOf("") }

    fun OnDismiss(){
        showPhotoManager = false
    }


    if (showPhotoManager) {

        val returnedBitmap = PhotoManager(context = context, OnDismiss = {OnDismiss()})


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
            .clip(RectangleShape)
            .border(2.dp, Color.Gray, RectangleShape)
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
//        Spacer(modifier = Modifier.height(16.dp))
//        OutlinedTextField(value = name, onValueChange = {name = it}, label = { Text(text = "Student Name")})
//        Spacer(modifier = Modifier.height(16.dp))
//        OutlinedTextField(value = rollNo, onValueChange = {rollNo = it}, label = { Text(text = "Roll No")})
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = Class, onValueChange = {Class = it}, label = { Text(text = "Class")})

        if( Class.isNotEmpty() && imageBitmapState.value != null){
            Button(onClick = {veiwModel.MarkAttendence(Class = Class, image = imageBitmapState.value!! )}) {
                Text(text = "Add Student")
            }
        }

    }
}
