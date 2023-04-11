package com.example.respalyzerproject

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.respalyzerproject.audioplayback.AndroidAudioPlayer
import com.example.respalyzerproject.audiorecord.AndroidAudioRecorder
import java.io.File

class RecordAudioActivity : AppCompatActivity() {

    private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }

    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    private var audioFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0
        )
        setContentView(R.layout.activity_record_audio)

        val raRecButton = findViewById<Button>(R.id.raRecAudiobtn)
        val raStopRecButton = findViewById<Button>(R.id.raStopRecAudiobtn)
        val raPlayButton = findViewById<Button>(R.id.raPlayAudiobtn)
        val raStopPlayButton = findViewById<Button>(R.id.raStopPlayAudiobtn)
        val raDashboardScreenButton = findViewById<Button>(R.id.raScreenDashboardBtn)

        // Start Recording
        raRecButton.setOnClickListener{
            File(cacheDir, "audio.mp3").also{
                recorder.start(it)
                audioFile = it
                Toast.makeText(applicationContext, "Recording Has Begun", Toast.LENGTH_SHORT).show()
            }
        }

        // Stop Recording
        raStopRecButton.setOnClickListener{
            recorder.stop()
            Toast.makeText(applicationContext, "Recording Has Stopped", Toast.LENGTH_SHORT).show()
        }

        // Play Audio
        raPlayButton.setOnClickListener {
            player.playFile(audioFile?: return@setOnClickListener)
            Toast.makeText(applicationContext, "Playing Has Begun", Toast.LENGTH_SHORT).show()
        }

        // Stop Playing Audio
        raStopPlayButton.setOnClickListener {
            player.stop()
            Toast.makeText(applicationContext, "Playing Has Stopped", Toast.LENGTH_SHORT).show()
        }

        // Stop Playing Audio
        raDashboardScreenButton.setOnClickListener {
            Intent(this, DashboardActivity::class.java).also{

                // starts the new activity (next screen, in this case)
                startActivity(it)
            }
        }
    }
}