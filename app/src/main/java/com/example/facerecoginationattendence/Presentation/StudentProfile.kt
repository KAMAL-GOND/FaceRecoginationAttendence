package com.example.facerecoginationattendence.Presentation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.facerecoginationattendence.Data.LocalDatabase.Attendence
import com.example.facerecoginationattendence.Data.LocalDatabase.Students
import com.example.facerecoginationattendence.Domain.StudentSideVeiwModel
import io.github.boguszpawlowski.composecalendar.StaticCalendar
import io.github.boguszpawlowski.composecalendar.rememberCalendarState

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StudentProfile(studentId: Long,veiwModel: StudentSideVeiwModel) {
    var month = remember { mutableStateOf(YearMonth.now())}
    var MonthAttendenceList: MutableState<MutableList<String>> = remember { mutableStateOf(mutableListOf("0")) }
    var StudentProfile = veiwModel.getStudentPrfofileState.collectAsState()
    val calendarState = rememberCalendarState(

    )
    var MonthAttendance = veiwModel.getAttendanceFlowState.collectAsState()


    LaunchedEffect(key1= calendarState.monthState.currentMonth.toString()) {
        veiwModel.getStudentProfile(studentId)
        veiwModel.getStudentAttendenceByMonth(studentId, calendarState.monthState.currentMonth.toString())
//        var AttendenceList = veiwModel.getStudentAttendenceByMonth(studentId, calendarState.monthState.currentMonth.toString()) as List<Attendence >
//        AttendenceList.forEach{MonthAttendence.value.add(it.Date!!)}

    }
    if(StudentProfile.value.error!= null || MonthAttendance.value.error!= null ){
        Toast.makeText(LocalContext.current,"${StudentProfile.value.error}+${MonthAttendance.value.error}",Toast.LENGTH_LONG)
        Text("null error")
    }
    else if (MonthAttendance.value.success != null){
        (MonthAttendance.value. success as List<Attendence>).forEach {
            MonthAttendenceList.value.add(it.Date!!.toString())
        }

    Column(modifier = Modifier.fillMaxSize().padding(5.dp)) {
        Row(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.3f).padding(7.dp), horizontalArrangement = Arrangement.Center){
            Box(modifier = Modifier.fillMaxSize(0.5f).clip(CircleShape).border(width = 2.dp, color = Color.Black,CircleShape), contentAlignment = Alignment.Center){
              //  Icon(Icons.Default.Person,null)
                Icon(Icons.Rounded.Person,null, modifier = Modifier.fillMaxSize())
            }
            Spacer(Modifier.width(5.dp))
            Column(modifier = Modifier.fillMaxHeight().padding(5.dp)){
                Text((StudentProfile.value.success as Students).name.toString())
                Text((StudentProfile.value.success as Students).Class.toString())
                Text((StudentProfile.value.success as Students).StudentID.toString())
            }
            Spacer(Modifier.width(10.dp))
            var attendencePercentage = (MonthAttendenceList.value.size/calendarState.monthState.currentMonth.lengthOfMonth())*100
            Text(attendencePercentage.toString(), modifier = Modifier.size(20.dp))}


        StaticCalendar (
            modifier = Modifier
                .padding(8.dp)
                .animateContentSize(),
            dayContent = {day->
                val color = when{
                    MonthAttendenceList.value.contains(day.date.toString())-> {
                        Color.Green
                    }
                    else-> Color.Transparent

                }
                Box(
                    modifier = Modifier
                        .background(color)
                        .padding(4.dp)
                ) {
                    Text(day.date.dayOfMonth.toString())
                }

            },
            calendarState = calendarState


        ) }


    }
    else{
        Text(MonthAttendance.value.toString())
        Text(StudentProfile.value.toString())
        StaticCalendar (
            modifier = Modifier
                .padding(8.dp)
                .animateContentSize(),
            dayContent = {day->
                val color = when{
                    MonthAttendenceList.value.contains(day.date.toString())-> {
                        Color.Green
                    }
                    else-> Color.Transparent

                }
                Box(
                    modifier = Modifier
                        .background(color)
                        .padding(4.dp)
                ) {
                    Text(day.date.dayOfMonth.toString())
                }

            },
            calendarState = calendarState


        )
    }



}
@RequiresApi(Build.VERSION_CODES.O)
fun IncreseMonth(month:String): String{
    return YearMonth.parse(month).plusMonths(1).toString()
}
@RequiresApi(Build.VERSION_CODES.O)
fun decreseMonth(month:String): String{
    return YearMonth.parse(month).minusMonths(1).toString()
}
