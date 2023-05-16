package com.example.respalyzerproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.respalyzerproject.audioplayback.AndroidAudioPlayer
import com.example.respalyzerproject.audiorecord.AndroidAudioRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// okhttp3 imports
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.AbstractMap.SimpleEntry
import java.util.Base64
import java.util.Date

class RecordAudioActivity : AppCompatActivity(), AudioTimer.OnTimerTickListener {

    // setting up the permissions
    // user has to verify that they want to record audio on each use
    //private var userPermission = arrayOf(Manifest.permission.RECORD_AUDIO)
    //private var hasUserPermission = false

    private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }

    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    //private var audioFile: File? = null

    // setting up file path where the audio will be store
    private var filePath = ""

    private lateinit var timer: AudioTimer

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_audio)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0
        )

        timer = AudioTimer(this)

        val raRecButton = findViewById<ImageButton>(R.id.raRecAudiobtn)
        val raStopRecButton = findViewById<ImageButton>(R.id.raStopRecAudiobtn)
        //val raPlayButton = findViewById<Button>(R.id.raPlayAudiobtn)
        //val raStopPlayButton = findViewById<Button>(R.id.raStopPlayAudiobtn)
        val raDashboardScreenButton = findViewById<Button>(R.id.raScreenDashboardBtn)

        raRecButton.isClickable = true
        raStopRecButton.isClickable = false
        // not needed
        //directoryPath = "${externalCacheDir?.absolutePath}/"

        // distinguishing file names - using the date down to the second to make it unique
        var simpleDateFormat = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss")
        var theDate = simpleDateFormat.format(Date())

        //making the file name
        filePath = "audio_rec_101_$theDate"
        println(filePath)
        var theFile: File? = null
        // Start Recording
        raRecButton.setOnClickListener {

            File(cacheDir, "$filePath.mp3").also{

                Toast.makeText(
                    applicationContext,
                    "Recording of $it Has Begun",
                    Toast.LENGTH_SHORT
                ).show()
                recorder.start(it)
                println("Printing the file path in the start recording button")
                println(it)
                theFile = it

                println("Printing the file path to external file directory")
                println(cacheDir)
            }
            println(theFile)
            timer.start()
            raRecButton.isClickable = false
            raStopRecButton.isClickable = true
        }

        // Stop Recording
        raStopRecButton.setOnClickListener{
            raStopRecButton.isClickable = false
            recorder.stop()
            Toast.makeText(applicationContext, "Recording of $theFile Has Stopped", Toast.LENGTH_SHORT).show()
            timer.stop()
            onTimerTick("00:00.00")
            raRecButton.isClickable = true

            // uploading file to Flask API
            println("Printing the file path in the stop recording button")
            println(theFile)
            val nonNullableFile: File = theFile ?: File("default/path/to/file")
            println("Non Nullable Print")
            println(nonNullableFile)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Perform your network operation here
                    println("Before uploadAudioFile")
                    uploadAudioFile(nonNullableFile, "http://192.168.100.81:8080/")
                } catch (e: Exception) {
                    // Handle any exceptions that occur during the network operation
                    e.printStackTrace()
                }
            }

        }

        // Audio Playing is not done on this activity anymore
        /*

        // Play Audio
        raPlayButton.setOnClickListener {
            //player.playFile(audioFile?: return@setOnClickListener)
            Toast.makeText(applicationContext, "Playing Has Begun", Toast.LENGTH_SHORT).show()
        }

        // Stop Playing Audio
        raStopPlayButton.setOnClickListener {
            player.stop()
            Toast.makeText(applicationContext, "Playing Has Stopped", Toast.LENGTH_SHORT).show()
        }

         */

        // Stop Playing Audio
        raDashboardScreenButton.setOnClickListener {
            Intent(this, DashboardActivity::class.java).also{

                // starts the new activity (next screen, in this case)
                startActivity(it)
            }
        }
    }

    private fun uploadAudioFile(audioFile: File, url: String) {
        val client = OkHttpClient()
        println("Here in the okhttp function works")

        println(audioFile)
        println(url)

        // converting the audio file to bytes
        val audioBytes = audioFile.readBytes()
        println(audioBytes)
        val audioBase64 = Base64.getEncoder().encodeToString(audioBytes) // converting to base 64 string
        println(audioBase64)
        // we have a JSON Object being built specifically to store the audio
        val jsonBody = JSONObject()
        jsonBody.put("audio", audioBase64)

        // storing the request body as a json body
        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())

        // building the request
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            val response: Response = client.newCall(request).execute()
            // Handle the response from the Flask API
            response.body?.string() //will contain the response body
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onTimerTick(duration: String) {
        val tvTimer = findViewById<TextView>(R.id.tvTimer)
        tvTimer.text = duration

    }
}