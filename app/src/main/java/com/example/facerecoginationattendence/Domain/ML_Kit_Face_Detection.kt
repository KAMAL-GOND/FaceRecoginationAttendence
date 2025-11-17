package com.example.facerecoginationattendence.Domain

import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

object ML_Kit_Face_Detection {



    var Option = FaceDetectorOptions.Builder().setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE).enableTracking().build()



    val detector = FaceDetection.getClient(Option)




    fun AddStudent(BitMap: Bitmap,rotation:Int) : Flow<Result<Bitmap>> = callbackFlow{
        //trySend(Result.)
        Log.d("mlKitLogs","1 entry bitmap data is" + BitMap.toString())
        //var CroppedImage: Bitmap? = null
        var image = InputImage.fromBitmap(BitMap, rotation)
        Log.d("mlKitLogs","2 InputImage data is" + image.toString() )

        detector.process(image).addOnSuccessListener { Faces ->
            Log.d("mlKitLogs", "3 face data is"+Faces.toString())
            if (Faces != null) {

                var mainFace =
                    Faces.maxByOrNull { it.boundingBox.height() * it.boundingBox.height() }
                Log.d("mlKitLogs", "4 main face is"+mainFace.toString())
                mainFace?.let { face ->
                    val box = face.boundingBox
                    Log.d("mlKitLogs", "5 box is"+box.toString())
                    val croppedFace = Bitmap.createBitmap(
                        BitMap,
                        box.left.coerceAtLeast(0),
                        box.top.coerceAtLeast(0),
                        box.width().coerceAtMost(BitMap.width - box.left),
                        box.height().coerceAtMost(BitMap.height - box.top)
                    )
                    Log.d("mlKitLogs", "6 cropped face is"+croppedFace.toString())
                    trySend(Result.success(croppedFace))

                    //CroppedImage = croppedFace

                }


            } else {
                trySend(Result.failure<Bitmap>(Exception("No faces detected")))
                Log.d("mlKitLogs", "7 no face")
            }
        }.addOnFailureListener { a ->
            Log.d("mlKitLogs", "8"+a.toString())
            Result.failure<Bitmap>(a)
        }
        awaitClose {  close()}

        //Log.d("mlKitLogs", "9"+CroppedImage.toString())

       //return CroppedImage
    }


    fun MarkAttendence(BitMap: Bitmap , rotation: Int):Flow<Result<ArrayList<Bitmap>?>> = callbackFlow{
        var Faces : ArrayList<Bitmap>

        var imputImage = InputImage.fromBitmap(BitMap,rotation)
        Log.d("MarkAttendenceMlkit",imputImage.toString())
        detector.process(imputImage).addOnSuccessListener{
            Log.d("MarkAttendenceMlkit",it.toString())


            if(it != null){
                 Faces = ArrayList()
                for(face in it){
                    var croppedImage = cropFaceFromBitmap(BitMap , face.boundingBox)
                    Log.d("MarkAttendenceMlkit",croppedImage.toString())
                    Faces.add(croppedImage)
                }
                Log.d("MarkAttendenceMlkit",Faces.toString())
                trySend(Result.success(Faces))
            }

            else {
                Log.d("MarkAttendenceMlkit","no faces there")
                trySend(Result.failure<ArrayList<Bitmap>?>(Exception("No faces detected")))
            }

        }.addOnFailureListener {
            Log.d("MarkAttendenceMlkit",it.toString())
            trySend(Result.failure<ArrayList<Bitmap>?>(it))
        }
        awaitClose{close()}
    }
    fun cropFaceFromBitmap(source: Bitmap, box: Rect): Bitmap {
        val left = box.left.coerceAtLeast(0)
        val top = box.top.coerceAtLeast(0)
        val width = box.width().coerceAtMost(source.width - left)
        val height = box.height().coerceAtMost(source.height - top)
        Log.d("MarkAttendenceMlkit","cropFaceFromBitmap"+box.toString())

        return Bitmap.createBitmap(source, left, top, width, height)
    }
}