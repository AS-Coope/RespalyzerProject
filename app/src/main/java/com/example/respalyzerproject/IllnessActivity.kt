package com.example.respalyzerproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class IllnessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_illness)

        val profileBtn = findViewById<ImageButton>(R.id.dsUserAccount3)
        val learnMoreBtn = findViewById<Button>(R.id.iLearnMore)
        val medCentreBtn = findViewById<Button>(R.id.iMedicalCentreBtn)
        val emerContactsBtn = findViewById<Button>(R.id.iEmergencyContact)

        profileBtn.setOnClickListener{
            // switch to the name of the Analyze Audio activity when that activity is created
            Intent(this, UserProfileActivity::class.java).also{
                // starts the new activity (next screen, in this case)
                startActivity(it)
            }
        }

        learnMoreBtn.setOnClickListener {
            Intent(this, EmergencyContactsActivity::class.java).also{
                startActivity(it) // travel back to the dashboard screen
            }
        }

        emerContactsBtn.setOnClickListener {
            Intent(this, EmergencyContactsActivity::class.java).also{
                startActivity(it) // travel back to the dashboard screen
            }
        }

        medCentreBtn.setOnClickListener {
            Intent(this, MedicalCentresMapActivity::class.java).also{
                startActivity(it) // travel back to the dashboard screen
            }
        }
    }
}