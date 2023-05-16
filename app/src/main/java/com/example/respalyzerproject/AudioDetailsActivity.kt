package com.example.respalyzerproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class AudioDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_details)

        val profileBtn = findViewById<ImageButton>(R.id.dsUserAccount3)
        val toDashboardBtn = findViewById<Button>(R.id.adDashboardBtn)

        profileBtn.setOnClickListener{
            // switch to the name of the Analyze Audio activity when that activity is created
            Intent(this, UserProfileActivity::class.java).also{
                // starts the new activity (next screen, in this case)
                startActivity(it)
            }
        }

        toDashboardBtn.setOnClickListener {
            Intent(this, DashboardActivity::class.java).also{
                startActivity(it) // travel back to the dashboard screen
            }
        }
    }
}