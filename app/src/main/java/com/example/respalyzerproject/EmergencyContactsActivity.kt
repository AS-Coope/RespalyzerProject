package com.example.respalyzerproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class EmergencyContactsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId") // for some reason the ids of ecEmerContact1Name and the one for number are not being found in the xml although
                                        // they are there
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_contacts)

        //
        val ecEmerContNametv = findViewById<TextView>(R.id.ecEmerContact1Name)
        val ecEmerContNumtv = findViewById<TextView>(R.id.ecEmerContact1Num)
        val ecViewMedCentres = findViewById<Button>(R.id.ecScreenMedicalBtn)
        val ecDashboardScreen = findViewById<Button>(R.id.ecScreenDashboardBtn)

        // putExtra Variable Assignments
        val ecEmerContactName = intent.getStringExtra("EXTRA_EMERUSERNAME")
        val ecEmerContactNumber = intent.getStringExtra("EXTRA_EMERUSERNUMBER")

        ecEmerContNametv.text = ecEmerContactName
        ecEmerContNumtv.text = ecEmerContactNumber

        ecDashboardScreen.setOnClickListener{
            Intent(this, DashboardActivity::class.java).also{

                // starts the new activity (next screen, in this case)
                startActivity(it)
            }
        }
    }
}