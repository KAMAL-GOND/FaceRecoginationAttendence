package com.example.facerecoginationattendence.Domain

import android.graphics.Bitmap
import android.graphics.Color
import org.tensorflow.lite.Interpreter
import kotlin.math.sqrt

fun getEmbeddingFromBitmap(
    bitmap: Bitmap,
    interpreter: Interpreter,
    inputSize: Int = 112        // Your model’s required size
): FloatArray {


    val resized = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)

    // Step 2: Create float input of shape [1,112,112,3]
    val input = Array(1) { Array(inputSize) { Array(inputSize) { FloatArray(3) } } }

    for (y in 0 until inputSize) {
        for (x in 0 until inputSize) {
            val pixel = resized.getPixel(x, y)

            // Normalize 0..255 to 0..1
            input[0][y][x][0] = Color.red(pixel) / 255f
            input[0][y][x][1] = Color.green(pixel) / 255f
            input[0][y][x][2] = Color.blue(pixel) / 255f
        }
    }

    // Step 3: Prepare output for 192D embedding
    val embedding = Array(1) { FloatArray(192) }

    // Step 4: Run inference
    interpreter.run(input, embedding)

    // Step 5: L2 normalize (recommended)
    return l2Normalize(embedding[0])
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
