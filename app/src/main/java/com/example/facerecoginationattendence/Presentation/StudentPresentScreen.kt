package com.example.facerecoginationattendence.Presentation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.facerecoginationattendence.Data.LocalDatabase.Attendence
import com.example.facerecoginationattendence.Data.LocalDatabase.Class
import com.example.facerecoginationattendence.Data.LocalDatabase.Students
import com.example.facerecoginationattendence.Domain.StudentSideVeiwModel
import com.example.facerecoginationattendence.Presentation.navigation.Routes

@Composable
fun StudentPresentScreen(veiwModel: StudentSideVeiwModel,navController: NavController) {



    var state = veiwModel.getClassDayAttendenceState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.1f),
            contentAlignment = Alignment.Center
        ) {
            Text("StudentsPresent", modifier = Modifier.fillMaxHeight().fillMaxWidth(0.5f))
        }


        when {
            state.value.loading == true -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { LinearProgressIndicator() }

            state.value.error != null -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Toast.makeText(
                    LocalContext.current, state.value.error as String, Toast.LENGTH_LONG
                )
            }

            state.value.success != null -> LazyColumn() {
                items(state.value.success as List<Attendence>) { item ->
                    Row(modifier = Modifier.fillMaxWidth().height(15.dp).clickable(onClick = {
                        navController.navigate(Routes.StudentProfile(item.StudentID))
                    })) {
                        Box(
                            modifier = Modifier.fillMaxHeight().fillMaxWidth(0.3f)
                        ) { Text(item.StudentID.toString()) }
                        Box(
                            modifier = Modifier.fillMaxHeight().fillMaxWidth(0.3f)
                        ) { Text(item.ClassName.toString()) }
                        Box(
                            modifier = Modifier.fillMaxHeight().fillMaxWidth(0.3f)
                        ) { Text(item.ClassName.toString()) }
                    }
                }

            }


        }
    }
}

