package com.example.respalyzerproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.respalyzerproject.audioplayback.AndroidAudioPlayer

class AudioDetailsActivity : AppCompatActivity() {
    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_details)

        val stopAudioButton = findViewById<ImageButton>(R.id.imageButton3)
        val audioPlayButton = findViewById<ImageButton>(R.id.imageButton2)
        val audioName = findViewById<TextView>(R.id.adTitle)

        // switch audioName.text to have the name stored in the database
        audioPlayButton.setOnClickListener {
            //player.playFile(audioFile?: return@setOnClickListener)
            Toast.makeText(applicationContext, "Playing Has Begun", Toast.LENGTH_SHORT).show()
        }

        // Stop Playing Audio
        stopAudioButton.setOnClickListener {
            player.stop()
            Toast.makeText(applicationContext, "Playing Has Stopped", Toast.LENGTH_SHORT).show()
        }
    }
}