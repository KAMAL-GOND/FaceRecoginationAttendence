package com.example.facerecoginationattendence.Presentation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope


import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.facerecoginationattendence.Data.LocalDatabase.Attendence

import com.example.facerecoginationattendence.Domain.StudentSideVeiwModel
import com.example.facerecoginationattendence.Presentation.navigation.Routes
import kotlinx.coroutines.launch

@Composable
fun StudentPresentScreen(veiwModel: StudentSideVeiwModel,navController: NavController) {

    var context = LocalContext.current

    val state by veiwModel.getClassDayAttendenceState.collectAsState()
    var scope = rememberCoroutineScope() ;
//    LaunchedEffect(state.error) {
//        Toast.makeText(
//            context, "$state.error" , Toast.LENGTH_LONG
//        ).show()
//
//    }

    Scaffold(){it->
       Column(modifier = Modifier
           .fillMaxSize()
           .padding(it), horizontalAlignment = Alignment.CenterHorizontally) {
           Text(
               text = "StudentsPresent",
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(vertical = 16.dp),
               textAlign = TextAlign.Center,
               fontSize = 20.sp,
               fontWeight = FontWeight.Bold,
               fontFamily = FontFamily.Cursive
           )



           if(state.loading == true){
               Box(
                   modifier = Modifier.fillMaxSize(),
                   contentAlignment = Alignment.Center
               ) { LinearProgressIndicator() }
           }
               //state.loading  ->
           else if(state.error != null){
               Box(
                   modifier = Modifier.fillMaxSize(),
                   contentAlignment = Alignment.Center
               ) {

                   Text("Error: ${state.error}")
               }

           }

               //state.error != null ->

           else if(state.success != null){
//               Row(
//                   modifier = Modifier
//                       .fillMaxWidth()
//                       .fillMaxHeight(0.1f),
//                   horizontalArrangement = Arrangement.Center
//               ) {
//                   Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
//                       Text(
//                           "StudentsPresent", modifier = Modifier
//                               .fillMaxHeight()
//                               .fillMaxWidth(),
//
//                           fontSize = 20.sp,
//                           fontWeight = FontWeight.Bold,
//                           fontFamily = FontFamily.Cursive
//                       )
//                   }
//               }
               Row(modifier = Modifier
                   .fillMaxWidth()
                   .height(50.dp), horizontalArrangement = Arrangement.Center) {

                   Box(modifier = Modifier
                       .fillMaxWidth(0.3f)
                       .fillMaxHeight()) { Text("Student Id") }

                   Box(modifier = Modifier
                       .fillMaxWidth(0.4f)
                       .fillMaxHeight()) {
                       Text("Name")
                   }

                   Box(modifier = Modifier
                       .fillMaxWidth(0.3f)
                       .fillMaxHeight()) { Text("Class ") }

               }
               LazyColumn() {
                   items(state.success as List<Attendence>) { item ->
                       Row(modifier = Modifier
                           .fillMaxWidth()
                           .height(50.dp)
                           .clickable(onClick = {
                               navController.navigate(Routes.StudentProfile(item.StudentID))
                           }), horizontalArrangement = Arrangement.Center) {
//                           Box(
//                               modifier = Modifier
//                                   .fillMaxHeight()
//                                   .fillMaxWidth(0.3f)
//                           ) { Text(item.StudentID.toString()) }
//                           Box(
//                               modifier = Modifier
//                                   .fillMaxHeight()
//                                   .fillMaxWidth(0.3f)
//                           ) { Text(item.ClassName.toString()) }
//                           Box(
//                               modifier = Modifier
//                                   .fillMaxHeight()
//                                   .fillMaxWidth(0.3f)
//                           ) { Text(item.ClassName.toString()) }

                           Box(modifier = Modifier
                               .fillMaxWidth(0.3f)
                               .fillMaxHeight()) { Text(item.StudentID.toString()) }
                           var name  = veiwModel.getStudentName(item.StudentID)
                           Box(modifier = Modifier
                               .fillMaxWidth(0.4f)
                               .fillMaxHeight()) { if(name!=null){
                               Text(name.toString())
                           }
                           else{
                               Text("null")
                           }}
                           Box(modifier = Modifier
                               .fillMaxWidth(0.3f)
                               .fillMaxHeight()) { Text(item.ClassName.toString()) }
                       }

                   }

               }

           }
           else{
               Box(
                   modifier = Modifier.fillMaxSize(),
                   contentAlignment = Alignment.Center
               ) { Text("no students")
                   CircularProgressIndicator()
               }
           }

               //state.success != null ->


           }
       }
   }


