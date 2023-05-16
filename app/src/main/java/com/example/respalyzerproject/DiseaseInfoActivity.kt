package com.example.respalyzerproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class DiseaseInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disease_info)

        val profileBtn = findViewById<ImageButton>(R.id.dsUserAccount3)
        val medCentreBtn = findViewById<Button>(R.id.iMedicalCentreBtn)

        profileBtn.setOnClickListener{
            // switch to the name of the Analyze Audio activity when that activity is created
            Intent(this, UserProfileActivity::class.java).also{
                // starts the new activity (next screen, in this case)
                startActivity(it)
            }
        }

        medCentreBtn.setOnClickListener {
            Intent(this, MedicalCentresMapActivity::class.java).also{
                startActivity(it) // travel back to the dashboard screen
            }
        }
    }
}