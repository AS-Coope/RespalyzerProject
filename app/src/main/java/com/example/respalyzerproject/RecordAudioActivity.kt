package com.example.respalyzerproject

import android.Manifest
import android.annotation.SuppressLint
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
import com.example.respalyzerproject.audiodatabase.AudioDatabase
import com.example.respalyzerproject.audiodatabase.AudioEntity
import com.example.respalyzerproject.audioplayback.AndroidAudioPlayer
import com.example.respalyzerproject.audiorecord.AndroidAudioRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// okhttp3 imports
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.AbstractMap.SimpleEntry
import java.util.Base64
import java.util.Date

class RecordAudioActivity : AppCompatActivity(), AudioTimer.OnTimerTickListener {



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

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_audio)

        // requesting permission to record the user's audio
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0
        )

        // database related initializations
        val audioDao = AudioDatabase.getDatabase(application).audioEntityDao()
        /*
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // testing that all records gets deleted once run
                audioDao.deleteAllAudioRecords()
            } catch (e: Exception) {
                // Handle any exceptions that occur during the database operation
                e.printStackTrace()
            }
        }

         */
        timer = AudioTimer(this)

        val raRecButton = findViewById<ImageButton>(R.id.raRecAudiobtn)
        val raStopRecButton = findViewById<ImageButton>(R.id.raStopRecAudiobtn)
        //val raPlayButton = findViewById<Button>(R.id.raPlayAudiobtn)
        //val raStopPlayButton = findViewById<Button>(R.id.raStopPlayAudiobtn)
        val raDashboardScreenButton = findViewById<Button>(R.id.raScreenDashboardBtn)

        // needed to disable and enable the buttons once
        raRecButton.isClickable = true
        raStopRecButton.isClickable = false


        var theDate = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss").format(Date())
        //making the file name (unique)
        filePath = ""
        // println(filePath) // for testing purposes

        // creating a null file to contain the file later
        var theFile: File? = null

        // Start Recording
        raRecButton.setOnClickListener {

            // distinguishing file names - using the date down to the second to make it unique
            val dateFormat = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss")
            theDate = dateFormat.format(Date())
            filePath = "audio_rec_101_${theDate}"
            File(cacheDir, "$filePath.mp3").also{

                Toast.makeText(
                    applicationContext,
                    "Recording of $it Has Begun",
                    Toast.LENGTH_SHORT
                ).show()
                recorder.start(it) // recording the audio to the file
                /*
                println("Printing the file path in the start recording button")
                println(it)
                 */
                theFile = it

                /*
                println("Printing the file path to external file directory")
                println(cacheDir)

                 */
             }
            //println(theFile)
            timer.start()

            // enabling and disabling buttons where relevant
            raRecButton.isClickable = false
            raStopRecButton.isClickable = true
        }

        // Stop Recording
        raStopRecButton.setOnClickListener{
            raStopRecButton.isClickable = false
            recorder.stop()
            Toast.makeText(applicationContext, "Recording of $theFile Has Stopped", Toast.LENGTH_SHORT).show()
            // getting the length of the audio recording before the timer is reset (in the 00:00.00 format)
            val fileTimeDuration = timer.format()
            timer.stop()

            onTimerTick("00:00.00")
            raRecButton.isClickable = true

            // uploading file to Flask API
            /*
            println("Printing the file path in the stop recording button")
            println(theFile)
             */
            val nonNullableFile: File = theFile ?: File("default/path/to/file")

            // saving the file in the database
            val fileToSave = nonNullableFile.length() / 1024 // getting file size in bytes and converting it to KiloBytes
            var sizeOfFileToStr = "${fileToSave}KB"
            val dateRecorded = theDate
            //val dateRecorded = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss").format(Date())

            // now storing the recording in the database
            val audioRec = AudioEntity(0, 1, nonNullableFile.toString(), fileTimeDuration, sizeOfFileToStr, "N/A", dateRecorded)

            // read/ write operations to a database can be resource intensive; it requires a separate thread from that of main
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Storing file in a database
                    audioDao.insert(audioRec)
                } catch (e: Exception) {
                    // Handle any exceptions that occur during the database operation
                    e.printStackTrace()
                }
            }


            // length of recording
            /*
            println("Non Nullable Print")
            println(nonNullableFile)
             */

            // some operations, like network operations, cannot work on the main thread, sending data across an api is one
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Perform your network operation here
                    //println("Before uploadAudioFile")
                    uploadAudioFile(nonNullableFile, "http://192.168.100.73:8080/record")
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

    // function to handle sending audio data to remote server and api
//    private fun uploadAudioFile(audioFile: File, url: String) {
//        val client = OkHttpClient()
//        println("Here in the okhttp function works")
//
//        println(audioFile)
//        println(url)
//
//        // converting the audio file to bytes
//        val audioBytes = audioFile.readBytes()
//        println(audioBytes)
//        val audioBase64 = Base64.getEncoder().encodeToString(audioBytes) // converting to base 64 string
//        println(audioBase64)
//        // we have a JSON Object being built specifically to store the audio
//        val jsonBody = JSONObject()
//        jsonBody.put("audio", audioBase64)
//
//        // storing the request body as a json body
//        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
//
//        // building the request
//        val request = Request.Builder()
//            .url(url)
//            .post(requestBody)
//            .header("Content-Type", "application/json")
//            .build()
//        println(requestBody)
//
//        try {
//            val response: Response = client.newCall(request).execute()
//            // Handle the response from the Flask API
//            val responseBody = response.body?.string() //will contain the response body
//            println(responseBody)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
    private fun uploadAudioFile(audioFile: File, url: String) {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("recording", audioFile.name, audioFile.asRequestBody("audio/mp3".toMediaType()))
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            val response: Response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            println(responseBody)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onTimerTick(duration: String) {
        // updates the timer on screen
        val tvTimer = findViewById<TextView>(R.id.tvTimer)
        tvTimer.text = duration
    }
}