package com.example.facerecoginationattendence.Presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import com.example.facerecoginationattendence.Data.LocalDatabase.Attendence
import com.example.facerecoginationattendence.Domain.StudentSideVeiwModel
import io.github.boguszpawlowski.composecalendar.StaticCalendar
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StudentProfile(studentId: Long,veiwModel: StudentSideVeiwModel) {
    var month = remember { mutableStateOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM")).toString() )}
    var MonthAttendence :List<Attendence>?=null
    var StudentProfile= remember { mutableStateOf(veiwModel.getStudentProfile(studentId)) }


    LaunchedEffect(key1= month.value) {
        MonthAttendence = veiwModel.getStudentAttendenceByMonth(studentId, month.value)

    }
    Column(modifier = Modifier.fillMaxSize().padding(5.dp)) {
        Row(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.3f).padding(7.dp), horizontalArrangement = Arrangement.Center){
            Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.3f).clip(CircleShape).border(width = 2.dp, color = Color.Black,CircleShape)){
                Icon(Icons.Default.Person,null)
            }
            Spacer(Modifier.width(5.dp))
            Column(modifier = Modifier.fillMaxHeight().padding(5.dp)){
                Text(StudentProfile.value.name.toString())
                Text(StudentProfile.value.StudentID.toString())
                Text(StudentProfile.value.Class.toString())
            }
            Spacer(Modifier.width(10.dp))
            var attendencePercentage = (MonthAttendence!!.size/YearMonth.parse(month.value).lengthOfMonth())*100
            Text(attendencePercentage.toString(), modifier = Modifier.size(20.dp))

        }
        StaticCalendar(
            modifier = Modifier
                .padding(8.dp)
                .animateContentSize(),
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