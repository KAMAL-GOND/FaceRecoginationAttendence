package com.example.facerecoginationattendence.Presentation.navigation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.facerecoginationattendence.Data.LocalDatabase.AppDatabase
import com.example.facerecoginationattendence.Data.LocalDatabase.Class
import com.example.facerecoginationattendence.Domain.StudentSideVeiwModel
import com.example.facerecoginationattendence.Presentation.AddStudentScreen
import com.example.facerecoginationattendence.Presentation.MarkAttendenceScreen
import com.example.facerecoginationattendence.Presentation.StudentPresentScreen
import com.example.facerecoginationattendence.Presentation.StudentProfile
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import io.github.boguszpawlowski.composecalendar.selection.EmptySelectionState.onDateSelected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId


@OptIn(ExperimentalMaterial3Api::class)
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
    val db = AppDatabase.getDatabase(veiwModel.appLicationcontext)
    var classes by remember { mutableStateOf<List<Class>>(emptyList()) }

    var expanded by remember { mutableStateOf(false) }
    var datePickerState = rememberDatePickerState()

    var classpicked by remember { mutableStateOf<String?>(null) }

    var search by remember { mutableStateOf<String>("")  }
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var scope = rememberCoroutineScope ();

    LaunchedEffect(Unit) {
        classes = withContext(Dispatchers.IO) { db.classDao().GetAllClass() }
    }

    ModalNavigationDrawer(
        modifier = Modifier.fillMaxWidth(),
        drawerState = drawerState,

        drawerContent = {ModalDrawerSheet(Modifier.fillMaxWidth(0.7f)) {
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = search,
                onValueChange = {search = it},
                label = {Text("enrollment number")},
                placeholder = {Text("enrollment number")},
                //visualTransformation = visu,

                trailingIcon = {Icon(Icons.Default.Search,"", modifier = Modifier.clickable(onClick = {
                    scope.launch {  drawerState.close() }
                    navController.navigate(Routes.StudentProfile(search.toLong()))

                }))},


            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(){


                classes.forEach { it->
                    item{
                        ModalNavigationItem(it , {
                            classpicked = it.ClassName
                            expanded=true;


                        })
//                        Text(it.ClassName.toString()+"->"+it.TeachersName.toString(), modifier = Modifier.clickable(onClick = {
//                            classpicked = it.ClassName
//                            //navController.navigate(Routes.StudentProfile(it.)
//                            expanded = true;
//
//
//                        }))
                        HorizontalDivider(thickness = 2.dp)
                    }

                }
            }
        }}
    ) {
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

        }},

    ) { it->
        if(expanded){
            DatePickerDialog(
                {expanded = false}, {
                    TextButton(onClick = {
                        veiwModel.getClassDayAttendence(classpicked.toString(),millisToDateString(datePickerState.selectedDateMillis!! ))

                        expanded = false
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.StudentPresent)
                    }) {
                        Text("OK")
                    }
                }, dismissButton = {
                    TextButton(onClick = {expanded = false}) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
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
                            Button(onClick = {veiwModel.AddClass(Class(ClassName=ClassName,TeachersName=TeacherName))
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
                MarkAttendenceScreen(veiwModel,navController)
            }
            composable <Routes.AddStudent>{
                AddStudentScreen(veiwModel)
            }

            composable<Routes.StudentProfile> {
                var id = it.toRoute<Routes.StudentProfile>()
                StudentProfile(id.id,veiwModel)
            }

            composable<Routes.StudentPresent>{
                StudentPresentScreen(veiwModel,navController)
            }


        }
    }}
}
@RequiresApi(Build.VERSION_CODES.O)
fun millisToDateString(millis: Long): String {
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault()) // Important
        .toLocalDate()
        .toString() // already yyyy-MM-dd
}



@Composable
fun ModalNavigationItem(it : Class , onclick:() -> Unit){
    Column(modifier = Modifier.fillMaxWidth().clickable(onClick =   onclick , )) {
        Text(text ="Teacher :${it.TeachersName}", fontStyle = FontStyle.Italic , fontSize = 20.sp , fontWeight = FontWeight.W400)
        Text(text ="class :${it.ClassName}", fontStyle = FontStyle.Italic , fontSize = 20.sp , fontWeight = FontWeight.W400)
    }
}