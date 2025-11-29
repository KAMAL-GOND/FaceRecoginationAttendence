import android.graphics.Bitmap

import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp

// function to get embedding
fun getEmbeddingFromBitmap(bitmap: Bitmap, interpreter: Interpreter): FloatArray {
    // Step 1: Create a TensorImage object from the bitmap.
    var tensorImage = TensorImage.fromBitmap(bitmap)

    // Step 2: Create an ImageProcessor to define the preprocessing steps.
    // FaceNet expects a 160x160 image.
    // The pixel values should be normalized from [0, 255] to [-1, 1].
    val imageProcessor = ImageProcessor.Builder()
        .add(ResizeOp(160, 160, ResizeOp.ResizeMethod.BILINEAR))
        .add(NormalizeOp(127.5f, 127.5f)) // Correct normalization for [-1, 1] range
        .build()


    // Step 3: Process the image.
    tensorImage = imageProcessor.process(tensorImage)

    // Step 4: Create the output buffer for the embedding.

    val embedding = Array(1) { FloatArray(128) }

    // Step 5: Run inference.
    interpreter.run(tensorImage.buffer, embedding)

    // Step 6: Return the embedding.
    return l2Normalize(embedding[0])
}

//function to normalize it so that it become comparavble
fun l2Normalize(embedding: FloatArray): FloatArray {
    var sum = 0f
    for (v in embedding) sum += v * v
    val norm = kotlin.math.sqrt(sum)
    return embedding.map { it / norm }.toFloatArray()
}