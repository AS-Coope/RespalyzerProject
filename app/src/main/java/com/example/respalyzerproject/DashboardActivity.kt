package com.example.respalyzerproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val emerContactsBtn = findViewById<Button>(R.id.dbScreenEmergencyBtn)
        val medCentreBtn = findViewById<Button>(R.id.dbScreenMedicalBtn)
        val audioHistoryBtn = findViewById<Button>(R.id.dbScreenAudioBtn)
        val analyzeAudioBtn = findViewById<Button>(R.id.dbScreenAnalyzeBtn)

        // putExtra variable assignments
        val dbEmerContactName = intent.getStringExtra("EXTRA_EMERUSERNAME")
        val dbEmerContactNumber = intent.getStringExtra("EXTRA_EMERUSERNUMBER")

        emerContactsBtn.setOnClickListener{
            Intent(this, EmergencyContactsActivity::class.java).also{
                // emergency contact info that needs to be transferred across the screens
                it.putExtra("EXTRA_EMERUSERNAME", dbEmerContactName.toString())
                it.putExtra("EXTRA_EMERUSERNUMBER", dbEmerContactNumber.toString())

                // starts the new activity (next screen, in this case)
                startActivity(it)
            }
        }

        medCentreBtn.setOnClickListener{
                                    // switch to the name of the Med Centres activity when that activity is created
            Intent(this, EmergencyContactsActivity::class.java).also{
                // starts the new activity (next screen, in this case)
                startActivity(it)
            }
        }

        audioHistoryBtn.setOnClickListener{
                                    // switch to the name of the Audio History activity when that activity is created
            Intent(this, EmergencyContactsActivity::class.java).also{
                // starts the new activity (next screen, in this case)
                startActivity(it)
            }
        }

        analyzeAudioBtn.setOnClickListener{
                                    // switch to the name of the Analyze Audio activity when that activity is created
            Intent(this, EmergencyContactsActivity::class.java).also{
                // starts the new activity (next screen, in this case)
                startActivity(it)
            }
        }
    }
}