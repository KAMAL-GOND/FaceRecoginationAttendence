package com.example.facerecoginationattendence.Domain.Models

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap

data class Students(
    var id : String? = null,
    var name : String? = null,
    var rollNo : String? = null,
//    var department : String? = null,
//    var semester : String? = null,
    var Class : String? = null,
    var PhotoEmbedding : String ?= null,
    var imageBitmap: Bitmap? = null
)
