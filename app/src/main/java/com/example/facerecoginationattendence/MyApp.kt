package com.example.facerecoginationattendence

import android.app.Application
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class MyApp : Application() {

    companion object {
        lateinit var interpreter: Interpreter
        private const val MODEL_NAME = "facenet.tflite"
    }

    override fun onCreate() {
        super.onCreate()

        // Create interpreter ONCE for the whole app
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
