package com.example.facerecoginationattendence.Presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.facerecoginationattendence.Domain.StudentSideVeiwModel
import com.example.facerecoginationattendence.Presentation.AddStudentScreen
import com.example.facerecoginationattendence.Presentation.MarkAttendenceScreen
import kotlinx.serialization.Serializable

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun navApp(veiwModel: StudentSideVeiwModel) {
    var DialogBoxState by remember { mutableStateOf<Boolean>(false) }
    var ClassName by remember { mutableStateOf("") }
    var TeacherName by remember { mutableStateOf("") }
    var navController = rememberNavController()
    //var backStackEntry : = remember{mutableListOf().toMutableStateList()}
    var selectedItem by remember { mutableIntStateOf(0) }
    var BottomNavItem = listOf("Mark_Attendence","Add_Student")
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomAppBar() {
                NavigationBar() {
                    BottomNavItem.forEachIndexed {index, string ->
                        NavigationBarItem(
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                when (index) {
                                    0 -> {
                                        navController.navigate(Routes.MarkAttendence)
                                    }

                                    1 -> {
                                        navController.navigate((Routes.AddStudent))
                                    }
                                }
                            },
                            icon =  {Icons.Default.Add},
                            modifier = Modifier.fillMaxWidth(),
                            enabled = true,
                            label = { Text(string) },
                            alwaysShowLabel = true,
                            //colors = TODO(),
                           // interactionSource = TODO(),
                            //selected = TODO() ,
                        )
                    }
                }
            }
        },
                floatingActionButton = {FloatingActionButton(onClick = {DialogBoxState=true}) {
            Column (modifier = Modifier.fillMaxSize(0.1f).padding(2.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top){
                Icon(imageVector = Icons.Default.Add,"add")
                Text("Add Class")
            }

        }}
    ) { it->
        if(DialogBoxState){

            Dialog(onDismissRequest = {DialogBoxState = false},) {
                Card (modifier = Modifier.fillMaxWidth(0.7f).fillMaxHeight(0.5f),  ) {
                    Column(modifier = Modifier.fillMaxSize().padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        OutlinedTextField(
                            value = ClassName,
                            onValueChange = { ClassName = it },
                            label = {Text(text = "Class Name")},
                        )
                        OutlinedTextField(
                            value = TeacherName,
                            onValueChange = { TeacherName = it },
                            label = {Text(text = "Teacher Name")},
                        )
                        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth().padding(it)){
                            Button(onClick = {veiwModel.AddClass(com.example.facerecoginationattendence.Data.LocalDatabase.Class(ClassName=ClassName,TeachersName=TeacherName))
                                DialogBoxState=false}) {
                                Text("Add")
                            }
                            Button(onClick = {DialogBoxState = false}) {
                                Text("Dismiss")
                            }
                        }
                    }
                }
            }
        }
        //Box(){}

        NavHost(navController=navController, startDestination = Routes.MarkAttendence){

            composable <Routes.MarkAttendence>{
                MarkAttendenceScreen(veiwModel)
            }
            composable <Routes.AddStudent>{
                AddStudentScreen(veiwModel)
            }
        }
    }
}

