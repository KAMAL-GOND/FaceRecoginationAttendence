package com.example.facerecoginationattendence.Domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp

// Rewritten to use Flow for modern, asynchronous handling.
fun Single_face_detector(context: Context, image: Bitmap): Flow<Result<Bitmap?>> = callbackFlow {
    val highAccuracyOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
        .build()

    val detector = FaceDetection.getClient(highAccuracyOpts)
    val inputImage = InputImage.fromBitmap(image, 0)

    detector.process(inputImage)
        .addOnSuccessListener { faces ->
            if (faces.isNotEmpty()) {
                // Face found, crop it and send the successful result.
                var mainFace = faces.maxByOrNull { it.boundingBox.height() * it.boundingBox.width() }
                val croppedFace = alignAndCropFace(image, mainFace!!)
                trySend(Result.success(croppedFace))
            } else {
                // No face found, send a successful result with null.
                trySend(Result.success(null))
            }
            close() // Close the flow after sending the result.
        }
        .addOnFailureListener { e ->
            // An error occurred, send a failure result.
            trySend(Result.failure(e))
            close() // Close the flow on failure.
        }

    // This will be called when the flow is cancelled.
    awaitClose { detector.close() }
}

// Simplified to be a synchronous function.
fun crop_face(image: Bitmap, face: Face): Bitmap {
    return Bitmap.createBitmap(
        image,
        face.boundingBox.left,
        face.boundingBox.top,
        face.boundingBox.width(),
        face.boundingBox.height()
    )
}


fun alignAndCropFace(original: Bitmap, face: Face, margin: Int = 10): Bitmap {
    // 1. Get landmarks (eyes)
    val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)?.position
    val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)?.position

    if (leftEye == null || rightEye == null) {
        // No landmark → return simple crop
        return crop_face(original, face)
    }

    // 2. Calculate rotation angle based on eye positions
    val dx = (rightEye.x - leftEye.x).toDouble()
    val dy = (rightEye.y - leftEye.y).toDouble()
    val angle = Math.toDegrees(Math.atan2(dy, dx)).toFloat()

    // 3. Rotate the entire bitmap around the center
    val matrix = Matrix()
    matrix.postRotate(angle, original.width / 2f, original.height / 2f)
    val rotated = Bitmap.createBitmap(original, 0, 0, original.width, original.height, matrix, true)

    // 4. Rotate bounding box → approximate (simple + works well)
    val rotatedBox = face.boundingBox

    // 5. Add margin
    val x = (rotatedBox.left - margin).coerceAtLeast(0)
    val y = (rotatedBox.top - margin).coerceAtLeast(0)
    val w = (rotatedBox.width() + 2 * margin).coerceAtMost(rotated.width - x)
    val h = (rotatedBox.height() + 2 * margin).coerceAtMost(rotated.height - y)

    // 6. Final crop
    return Bitmap.createBitmap(rotated, x, y, w, h)
}
fun Multiple_face_detector(context: Context, image: Bitmap): Flow<Result<ArrayList<Bitmap>?>> = callbackFlow {
    val highAccuracyOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
        .build()

    val detector = FaceDetection.getClient(highAccuracyOpts)
    val inputImage = InputImage.fromBitmap(image, 0)
    var FaceList : ArrayList<Bitmap>

    detector.process(inputImage)
        .addOnSuccessListener { faces ->
            if (faces.isNotEmpty()) {
                FaceList  = ArrayList()
                // Face found, crop it and send the successful result.
                for(face in faces){
                    val croppedFace = alignAndCropFace(image, face)
                    FaceList.add(croppedFace)
                    Log.d("EachFace",croppedFace.toString())
                }
                //val croppedFace = alignAndCropFace(image, face)
                trySend(Result.success(FaceList))
            } else {
                // No face found, send a successful result with null.
                trySend(Result.success(null))
            }
            close() // Close the flow after sending the result.
        }
        .addOnFailureListener { e ->
            // An error occurred, send a failure result.
            trySend(Result.failure(e))
            close() // Close the flow on failure.
        }

    // This will be called when the flow is cancelled.
    awaitClose { detector.close() }
}

