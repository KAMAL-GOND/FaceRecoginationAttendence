package com.example.facerecoginationattendence.Presentation

import android.Manifest
import android.Manifest.*
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Camera
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.Surface
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.pm.ShortcutInfoCompat
import kotlinx.coroutines.launch

var xyz = "panner"
@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoManager (context : Context , OnDismiss: () -> Unit , applicationContext: Context) : Bitmap?{
    var BottomSheetState = rememberModalBottomSheetState(
    )
    var rotation : Int? = 0;
    var a = context.checkSelfPermission(Manifest.permission.CAMERA)
    var b = PackageManager.PERMISSION_GRANTED
    Log.d("kamal",a.toString() + b.toString())
    var scope = rememberCoroutineScope()
//    var a = BottomSheetState.show()
    var ShowBottomSheet = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        ShowBottomSheet.value = true

    }
    var uri by remember{mutableStateOf<Uri?>(null) };
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }


    var PickPhoto = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()){ it->
        if (it != null){
            uri = it;
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver,it)
            rotation = getRotationFromExif(it,context)
           // bitmap = MediaStore.Images.Media.getBitmap()

        }
        else{
            Toast.makeText(context,"No Image Selected",Toast.LENGTH_SHORT).show()
            OnDismiss()

        }
       // OnDismiss()



        }


    var CapturePhoto = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()){ it ->
        if (it != null){
            bitmap = it
            rotation = when (applicationContext.display?.rotation) {
                Surface.ROTATION_0 -> 0
                Surface.ROTATION_90 -> 90
                Surface.ROTATION_180 -> 180
                Surface.ROTATION_270 -> 270
                else -> 0
            }


        }
        else{
            Toast.makeText(context,"No Photo Captured",Toast.LENGTH_SHORT).show()
            OnDismiss()

        }
        //OnDismiss()


    }
    var permission = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
        if (it){
            CapturePhoto.launch()
        }
        else{
            Toast.makeText(context,"Permission Denied",Toast.LENGTH_SHORT).show()
            OnDismiss()

        }
    }

    if(ShowBottomSheet.value == true){
        ModalBottomSheet(
           onDismissRequest = { ShowBottomSheet.value = false
               OnDismiss()
           },
            //onDismissRequest = { ShowBottomSheet.value = true},
            sheetState = BottomSheetState
        ) {
            Column (
                modifier = Modifier.fillMaxWidth().padding(12.dp)
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {
                            scope.launch { BottomSheetState.hide() }.invokeOnCompletion {
                                if (!BottomSheetState.isVisible) {
                                    ShowBottomSheet.value = false
                                }
                            }

                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    android.Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                CapturePhoto.launch()
                            } else {
                                permission.launch(android.Manifest.permission.CAMERA)
                            }

                        }),
                    verticalAlignment = Alignment.CenterVertically // Center items vertically
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Camera")
                    Spacer(modifier = Modifier.width(16.dp)) // Add space between icon and text
                    Text("Camera")
                }

                Spacer(modifier = Modifier.height(16.dp)) // Add space between the rows

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {
                            scope.launch {BottomSheetState.hide() }.invokeOnCompletion {
                                if (!BottomSheetState.isVisible) {
                                    ShowBottomSheet.value = false
                                }
                            }

                            PickPhoto.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )

                        }),
                    verticalAlignment = Alignment.CenterVertically // Center items vertically
                ) {
                    Icon(imageVector = Icons.Outlined.AccountBox, contentDescription = "Gallery")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Gallery")
                }
            }
        }
    }
    if(bitmap!=null && rotation != null){
        return rotateBitmap(bitmap,rotation)
    }
    else{
        return null
    }






}
fun rotateBitmap(bitmap: Bitmap?, rotationDegrees: Int?): Bitmap? {
    val matrix = Matrix().apply { postRotate(rotationDegrees!!.toFloat()) }
    return Bitmap.createBitmap(bitmap!!, 0, 0, bitmap.width, bitmap.height, matrix, true)
}
fun getRotationFromExif(uri: Uri, context: Context): Int {
    val inputStream = context.contentResolver.openInputStream(uri)
    val exif = ExifInterface(inputStream!!)
    return when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90
        ExifInterface.ORIENTATION_ROTATE_180 -> 180
        ExifInterface.ORIENTATION_ROTATE_270 -> 270
        else -> 0
    }
}
@RequiresApi(Build.VERSION_CODES.R)
fun getRotationFromPickPhoto(context: Application):Int{
    var rotation = when (context.applicationContext.display?.rotation) {
        Surface.ROTATION_0 -> 0
        Surface.ROTATION_90 -> 90
        Surface.ROTATION_180 -> 180
        Surface.ROTATION_270 -> 270
        else -> 0
    }
    return rotation


}