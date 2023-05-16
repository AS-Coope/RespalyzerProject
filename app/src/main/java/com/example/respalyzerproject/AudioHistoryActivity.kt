package com.example.respalyzerproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class AudioHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_history)

        val toDashboardBtn = findViewById<Button>(R.id.ahDashboardBtn)
        val profileBtn = findViewById<ImageButton>(R.id.dsUserAccount3)

        toDashboardBtn.setOnClickListener {
            Intent(this, DashboardActivity::class.java).also{
                startActivity(it) // travel back to the dashboard screen
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
}