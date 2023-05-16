package com.example.respalyzerproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AudioHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_history)

        val toDashboardBtn = findViewById<Button>(R.id.ahDashboardBtn)

        toDashboardBtn.setOnClickListener {
            Intent(this, DashboardActivity::class.java).also{
                startActivity(it) // travel back to the dashboard screen
            }
        }
    }
}