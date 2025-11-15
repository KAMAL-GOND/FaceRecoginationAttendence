package com.example.facerecoginationattendence.Domain

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import kotlin.math.sqrt

suspend fun getEmbeddingFromBitmap(
    bitmap: Bitmap,
    interpreter: Interpreter,
    inputSize: Int = 112
): FloatArray = withContext(Dispatchers.Default) {

    val resized = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)

    // Correct input tensor shape [1,112,112,3]
    val input = Array(1) { Array(inputSize) { Array(inputSize) { FloatArray(3) } } }

    for (y in 0 until inputSize) {
        for (x in 0 until inputSize) {
            val pixel = resized.getPixel(x, y)
            input[0][y][x][0] = Color.red(pixel) / 255f
            input[0][y][x][1] = Color.green(pixel) / 255f
            input[0][y][x][2] = Color.blue(pixel) / 255f
        }
    }

    // Output: 192-dim
    val output = Array(1) { FloatArray(192) }

    // Run TFLite
    interpreter.run(input, output)

    Log.d("raw",output.joinToString(","))

    // Normalize and return
    l2Normalize(output[0])
}


// Helper: Normalize vector
fun l2Normalize(vector: FloatArray): FloatArray {
    var sum = 0f
    for (v in vector) sum += v * v
    val norm = sqrt(sum)
    val out = FloatArray(vector.size)
    for (i in vector.indices) out[i] = vector[i] / norm
    return out
}
