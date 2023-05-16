package com.example.respalyzerproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.respalyzerproject.audioplayback.AndroidAudioPlayer
import com.example.respalyzerproject.audiorecord.AndroidAudioRecorder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.AbstractMap.SimpleEntry
import java.util.Date

class RecordAudioActivity : AppCompatActivity() {

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

    // setting up file path where the audio will be stored
    private var directoryPath = ""
    private var filePath = ""
    private var fileName = ""
    private var recordingNowCheck = false
    private var stoppedNowCheck = false

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                0
        )

        setContentView(R.layout.activity_record_audio)

        val raRecButton = findViewById<ImageButton>(R.id.raRecAudiobtn)
        val raStopRecButton = findViewById<ImageButton>(R.id.raStopRecAudiobtn)
        //val raPlayButton = findViewById<Button>(R.id.raPlayAudiobtn)
        //val raStopPlayButton = findViewById<Button>(R.id.raStopPlayAudiobtn)
        val raDashboardScreenButton = findViewById<Button>(R.id.raScreenDashboardBtn)

        // not needed
        //directoryPath = "${externalCacheDir?.absolutePath}/"

        // distinguishing file names - using the date down to the second to make it unique
        var simpleDateFormat = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss")
        var theDate = simpleDateFormat.format(Date())

        //making the file name
        directoryPath = getExternalFilesDir(null)?.absolutePath ?: ""
        filePath = "audio_rec_101$theDate"
        fileName = "$directoryPath/$filePath.mp3"

        // Start Recording
        raRecButton.setOnClickListener {
            File(directoryPath, "$filePath").also{ file ->

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
             }
        }

        // Stop Recording
        raStopRecButton.setOnClickListener{
            recorder.stop()
            Toast.makeText(applicationContext, "Recording of $fileName Has Stopped", Toast.LENGTH_SHORT).show()

            val client = OkHttpClient()
            // Create a JSON object to hold the data
            val json = JSONObject()
            json.put("recording", fileName)
            val requestBody = json.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaType())

            // Define the request object
            val request = Request.Builder()
                .url("http://192.168.100.73:8080/record")
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
            })
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
}