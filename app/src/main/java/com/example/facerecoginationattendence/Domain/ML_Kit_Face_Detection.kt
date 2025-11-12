package com.example.facerecoginationattendence.Domain

import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

object ML_Kit_Face_Detection {
    var Option = FaceDetectorOptions.Builder().setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE).enableTracking().build()

    val detector = FaceDetection.getClient(Option)


    fun AddStudent(BitMap: Bitmap,rotation:Int) : Bitmap? {
        var CroppedImage: Bitmap? = null
        var image = InputImage.fromBitmap(BitMap, rotation)
        detector.process(image).addOnSuccessListener { Faces ->
            if (Faces != null) {

                var mainFace =
                    Faces.maxByOrNull { it.boundingBox.height() * it.boundingBox.height() }
                mainFace?.let { face ->
                    val box = face.boundingBox
                    val croppedFace = Bitmap.createBitmap(
                        BitMap,
                        box.left.coerceAtLeast(0),
                        box.top.coerceAtLeast(0),
                        box.width().coerceAtMost(BitMap.width - box.left),
                        box.height().coerceAtMost(BitMap.height - box.top)
                    )
                    CroppedImage = croppedFace
                }


            } else {
                Log.d("AddStudentMLKItdetection", "no face")
            }

        }.addOnFailureListener { a ->
            Log.d("AddStudentMLKItdetection", a.toString())
        }
       return BitMap!!
    }
    fun cropFaceFromBitmap(source: Bitmap, box: Rect): Bitmap {
        val left = box.left.coerceAtLeast(0)
        val top = box.top.coerceAtLeast(0)
        val width = box.width().coerceAtMost(source.width - left)
        val height = box.height().coerceAtMost(source.height - top)
        return Bitmap.createBitmap(source, left, top, width, height)
    }

    fun MarkAttendence(BitMap: Bitmap , rotation: Int){
        var Faces : ArrayList<Bitmap>?=null

        var imputImage = InputImage.fromBitmap(BitMap,rotation)
        detector.process(imputImage).addOnSuccessListener{
            if(it != null){
                for(face in it){
                    var croppedImage = cropFaceFromBitmap(BitMap , face.boundingBox)
                    Faces?.add(croppedImage)
                }
            }

            else {
                Log.d("MarkAttendenceMlkit","no faces there")
            }

        }.addOnFailureListener {
            Log.d("MarkAttendenceMlkit",it.toString())
        }
    }
}