package com.example.facerecoginationattendence.Domain.Models

import android.util.Log
import androidx.compose.ui.unit.dp

fun euclideanDistance(a: FloatArray, b: FloatArray): Float {
    var sum = 0f
    for (i in a.indices) {
        val diff = a[i] - b[i]
        sum += diff * diff
    }
    return kotlin.math.sqrt(sum)
}
fun isSamePerson(a: FloatArray,b: FloatArray,treshold: Float =1.0f): Boolean{
    var distance = euclideanDistance(a,b);
    Log.d("euclidian distance",distance.toString())

    return euclideanDistance(a,b)<=treshold

}