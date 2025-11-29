import android.graphics.Bitmap

import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp

//package com.example.facerecoginationattendence.Domain
//
//import android.graphics.Bitmap
//import android.graphics.Color
//import android.util.Log
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.withContext
//import org.tensorflow.lite.Interpreter
//import kotlin.math.sqrt
//
//suspend fun getEmbeddingFromBitmap(
//    bitmap: Bitmap,
//    interpreter: Interpreter,
//    inputSize: Int = 112
//): FloatArray = withContext(Dispatchers.Default) {
//
//    val resized = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)
//
//    // Correct input tensor shape [1,112,112,3]
//    val input = Array(1) { Array(inputSize) { Array(inputSize) { FloatArray(3) } } }
//
//    for (y in 0 until inputSize) {
//        for (x in 0 until inputSize) {
//            val pixel = resized.getPixel(x, y)
//            input[0][y][x][0] = Color.red(pixel) / 255f
//            input[0][y][x][1] = Color.green(pixel) / 255f
//            input[0][y][x][2] = Color.blue(pixel) / 255f
//        }
//    }
//
//    // Output: 192-dim
//    val output = Array(1) { FloatArray(192) }
//
//    // Run TFLite
//    interpreter.run(input, output)
//
//    Log.d("raw",output.joinToString(","))
//
//    // Normalize and return
//    l2Normalize(output[0])
//}
//
//
//// Helper: Normalize vector
//fun l2Normalize(vector: FloatArray): FloatArray {
//    var sum = 0f
//    for (v in vector) sum += v * v
//    val norm = sqrt(sum)
//    val out = FloatArray(vector.size)
//    for (i in vector.indices) out[i] = vector[i] / norm
//    return out
//}
fun getEmbeddingFromBitmap(bitmap: Bitmap, interpreter: Interpreter): FloatArray {
    // Step 1: Create a TensorImage object from the bitmap.
    var tensorImage = TensorImage.fromBitmap(bitmap)

    // Step 2: Create an ImageProcessor to define the preprocessing steps.
    // MobileFaceNet expects a 112x112 image.
    // The pixel values should be normalized from [0, 255] to [-1, 1].
    val imageProcessor = ImageProcessor.Builder()
        .add(ResizeOp(160, 160, ResizeOp.ResizeMethod.BILINEAR))
        .add(NormalizeOp(127.5f, 127.5f)) // Correct normalization for [-1, 1] range
        .build()


    // Step 3: Process the image.
    tensorImage = imageProcessor.process(tensorImage)

    // Step 4: Create the output buffer for the embedding.
    // MobileFaceNet produces a 192-element embedding.
    val embedding = Array(1) { FloatArray(128) }

    // Step 5: Run inference.
    interpreter.run(tensorImage.buffer, embedding)

    // Step 6: Return the embedding.
    return l2Normalize(embedding[0])
}
fun l2Normalize(embedding: FloatArray): FloatArray {
    var sum = 0f
    for (v in embedding) sum += v * v
    val norm = kotlin.math.sqrt(sum)
    return embedding.map { it / norm }.toFloatArray()
}