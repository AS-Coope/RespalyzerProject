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
<<<<<<< HEAD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
=======
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
>>>>>>> ca791c0a32d290e2bdd33a5edeb87f63b9a24697
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
<<<<<<< HEAD
    private var fileName = ""
    private var recordingNowCheck = false
    private var stoppedNowCheck = false
=======

    private lateinit var timer: AudioTimer
>>>>>>> ca791c0a32d290e2bdd33a5edeb87f63b9a24697

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
        val profileBtn = findViewById<ImageButton>(R.id.dsUserAccount3)

        raRecButton.isClickable = true
        raStopRecButton.isClickable = false
        // not needed
        //directoryPath = "${externalCacheDir?.absolutePath}/"

        // distinguishing file names - using the date down to the second to make it unique
        var simpleDateFormat = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss")
        var theDate = simpleDateFormat.format(Date())

        //making the file name
<<<<<<< HEAD
        directoryPath = getExternalFilesDir(null)?.absolutePath ?: ""
        filePath = "audio_rec_101$theDate"
        fileName = "$directoryPath/$filePath.mp3"
        val filet = File(directoryPath, "$filePath.mp3")

        // Start Recording
        raRecButton.setOnClickListener {
            File(directoryPath, "$filePath.mp3").also{ file ->

                Toast.makeText(
                    applicationContext,
                    "Recording of $fileName Has Begun",
                    Toast.LENGTH_SHORT
                ).show()
                /*
                // Shows the file path
                Toast.makeText(
                    applicationContext,
                    "Path: $it",
                    Toast.LENGTH_SHORT
                ).show()

                 */
                recorder.start(file)
                println(file)
=======
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
>>>>>>> ca791c0a32d290e2bdd33a5edeb87f63b9a24697
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
<<<<<<< HEAD
            Toast.makeText(applicationContext, "Recording of $fileName Has Stopped", Toast.LENGTH_SHORT).show()

            uploadAudioFile(filet)


            /*val client = OkHttpClient()
            // Create a JSON object to hold the data
            val json = JSONObject()
            json.put("recording", filet)
            println(filet)
            val requestBody = json.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaType())

            // Define the request object
            val request = Request.Builder()
                .url("http://192.168.100.81:8080/record")
                .post(requestBody)
                .build()

            // Make the request asynchronously
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Handle the failure case
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    // Handle the response
                    val responseBody = response.body?.string()
                    if (response.isSuccessful) {
                        // The request was successful
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "Recording added",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // The request failed
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                responseBody,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })*/
=======
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

>>>>>>> ca791c0a32d290e2bdd33a5edeb87f63b9a24697
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

        profileBtn.setOnClickListener{
            // switch to the name of the Analyze Audio activity when that activity is created
            Intent(this, UserProfileActivity::class.java).also{
                // starts the new activity (next screen, in this case)
                startActivity(it)
            }
        }
    }

<<<<<<< HEAD
    fun uploadAudioFile(file: File) {
        val request = Request.Builder()
            .url("http://192.168.100.81:8080/record") // Replace with your Flask API endpoint
            .post(RequestBody.create("audio/wav".toMediaTypeOrNull(), file))
            .build()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val response = client.newCall(request).execute()

                // Handle the response as needed
                if (response.isSuccessful) {
                    // Successful response
                } else {
                    // Error handling for unsuccessful response
                }
            } catch (e: IOException) {
                e.printStackTrace()
                // Error handling for network error
            }
        }
    }

    // Usage: Call this function when you want to upload the audio file
    val audioFile = File("path/to/audio/file.wav") // Replace with the path to your audio file
=======
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
>>>>>>> ca791c0a32d290e2bdd33a5edeb87f63b9a24697
}