package com.example.respalyzerproject.audiorecord

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream

// concrete implementation of what the audio recorder shall do
class AndroidAudioRecorder(
    private val context: Context
): AudioRecorder{
    // removed the

    private var recorder: MediaRecorder? = null

    // remove these later.. not needed anymore
    //private var directoryPath = ""
    // private var filePath = ""

    @RequiresApi(Build.VERSION_CODES.S)
    private fun createRecorder(): MediaRecorder{
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            MediaRecorder(context)
        } else MediaRecorder()
    }

    //directoryPath = "${getExternalCacheDir.absolutePath}/"
    @RequiresApi(Build.VERSION_CODES.S)
    override fun start(outputFile: File) {

        createRecorder().apply{
            setAudioSource(MediaRecorder.AudioSource.MIC) // where the audio will be received (source)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) // apparently MPEG_4 = MP3
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)
            //setOutputFile("$outputFile.mp3") // where the file path is stored

            prepare() // preparing to record
            start() // starts the recording

            recorder = this
        }
    }

    override fun stop() {
        recorder?.stop()
        recorder?.reset() // allows recording again
        recorder = null
    }

}