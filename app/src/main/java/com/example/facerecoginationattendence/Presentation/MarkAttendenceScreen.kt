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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.window.Dialog
import com.example.facerecoginationattendence.Data.LocalDatabase.AppDatabase
import com.example.facerecoginationattendence.Data.LocalDatabase.Class

import com.example.facerecoginationattendence.Domain.StudentSideVeiwModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.time.LocalDate
import kotlin.coroutines.CoroutineContext


@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun MarkAttendenceScreen(veiwModel: StudentSideVeiwModel) {
    //dropdown menu
    var dropDownMenuExpansion by remember { mutableStateOf(false) }
    // db class
    val db = AppDatabase.getDatabase(veiwModel.appLicationcontext)
    var classes by remember { mutableStateOf<List<Class>>(emptyList()) }
    var showPhotoManager by remember { mutableStateOf(false) }
    var DialogBoxState by remember { mutableStateOf<Boolean>(false) }

    LaunchedEffect(DialogBoxState) {
        classes = withContext(Dispatchers.IO) { db.classDao().GetAllClass() }
    }

    val imageBitmapState = remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current


    var Class by remember { mutableStateOf("") }

    fun OnDismiss() {
        showPhotoManager = false
    }


    var ClassName by remember { mutableStateOf("") }
    var TeacherName by remember { mutableStateOf("") }

    if (showPhotoManager) {

        val returnedBitmap = PhotoManager(
            context = context,
            OnDismiss = { OnDismiss() },
            veiwModel.appLicationcontext
        )


        LaunchedEffect(returnedBitmap) {
            if (returnedBitmap != null) {
                imageBitmapState.value = returnedBitmap

                showPhotoManager = false
            } else {
                //Toast.makeText(context,"No Image Selected", Toast.LENGTH_SHORT).show()
                //showPhotoManager = false
            }

        }
    }
    Scaffold(
        floatingActionButton = {FloatingActionButton(onClick = {DialogBoxState=true}) {
            Column {
                Icon(imageVector = Icons.Default.Add,"add")
                Text("Add Class")
            }

        }}
    ) { its->

        if(DialogBoxState){

            Dialog(onDismissRequest = {DialogBoxState = false},) {
                Card (modifier = Modifier.fillMaxWidth(0.7f).fillMaxHeight(0.6f),  ) {
                    Column() {
                        OutlinedTextField(
                            value = ClassName,
                            onValueChange = { ClassName = it },
                            label = {Text(text = "Class Name")},
                        )
                        OutlinedTextField(
                            value = TeacherName,
                            onValueChange = { TeacherName = it },
                            label = {Text(text = "TEACHER Name")},
                        )
                        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth().padding(its)){
                            Button(onClick = {veiwModel.AddClass(com.example.facerecoginationattendence.Data.LocalDatabase.Class(ClassName=ClassName,TeachersName=TeacherName))
                            DialogBoxState=false}) {
                                Text("add")
                            }
                            Button(onClick = {DialogBoxState = false}) {
                                Text("Dismiss")
                            }
                        }
                    }
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
            Box(
                modifier = Modifier
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
            Box {
                OutlinedTextField(
                    modifier = Modifier.clickable(onClick = { dropDownMenuExpansion = true }),
                    value = Class,
                    readOnly = true,
                    onValueChange = { Class = it },
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
                            onClick = {Class=a.ClassName.toString()
                            dropDownMenuExpansion = false}
                        )
                    }
                }
            }

            if (Class.isNotEmpty() && imageBitmapState.value != null) {
                Button(onClick = {
                    veiwModel.MarkAttendence(
                        Class = Class,
                        image = imageBitmapState.value!!
                    )
                }) {
                    Text(text = "Mark Attendence")
                }
            }

        }
    }
}