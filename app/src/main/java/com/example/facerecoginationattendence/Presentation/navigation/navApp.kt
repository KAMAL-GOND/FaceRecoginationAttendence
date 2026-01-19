package com.example.facerecoginationattendence.Presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.facerecoginationattendence.Domain.StudentSideVeiwModel
import com.example.facerecoginationattendence.Presentation.AddStudentScreen
import com.example.facerecoginationattendence.Presentation.MarkAttendenceScreen

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun navApp(veiwModel: StudentSideVeiwModel) {
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
                            icon = TODO(),
                            modifier = TODO(),
                            enabled = true,
                            label = { Text(string) },
                            alwaysShowLabel = true,
                            colors = TODO(),
                            interactionSource = TODO(),
                            //selected = TODO() ,
                        )
                    }
                }
            }
        }
    ) { it->
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

