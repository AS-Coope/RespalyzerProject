package com.example.respalyzerproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class WelcomeScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)

        val showUser = findViewById<TextView>(R.id.tvUserInfo)

        val pUserName = intent.getStringExtra("EXTRA_USERNAME")
        val pUserAge = intent.getIntExtra("EXTRA_USERAGE", 0)
        val pUserGender = intent.getStringExtra("EXTRA_USERGENDER")
        val pUserWeight = intent.getFloatExtra("EXTRA_USERWEIGHT", 0.0f)
        val pUserHeight = intent.getFloatExtra("EXTRA_USERHEIGHT",  0.0f)
        val pUserKnownIllnesses = intent.getStringExtra("EXTRA_KNOWNILLNESSES")
        val pUserEmerName = intent.getStringExtra("EXTRA_EMERGNAME")
        val pUserEmerNumber = intent.getStringExtra("EXTRA_EMERGNUMBER")

        showUser.text = "Welcome, $pUserName.\nThe following data was registered about you: " +
                        "Age: $pUserAge\nGender: $pUserGender\nWeight: $pUserWeight\nHeight: $pUserHeight\nKnown Illnesses: $pUserKnownIllnesses\n" +
                        "Emergency Contact Name: $pUserEmerName\nEmergency Contact Number: $pUserEmerNumber"
    }
}