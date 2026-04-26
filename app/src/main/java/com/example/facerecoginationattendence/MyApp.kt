package com.example.facerecoginationattendence

import android.app.Application
import androidx.room.Room
import com.example.facerecoginationattendence.Data.LocalDatabase.AppDatabase
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class MyApp : Application() {
    var context = this

    companion object {
        lateinit var interpreter: Interpreter
        private const val MODEL_NAME = "facenet.tflite"

       // lateinit var db= AppDatabase
    }

    override fun onCreate() {
        super.onCreate()


        interpreter = Interpreter(loadModelFile(MODEL_NAME))



    }

    private fun loadModelFile(fileName: String): MappedByteBuffer {
        val fileDescriptor = assets.openFd(fileName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        return fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            fileDescriptor.startOffset,
            fileDescriptor.declaredLength
        )
    }
}
