package com.example.respalyzerproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.TextView
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // references to the various widgets on (from the activity_main file)
        val userName = findViewById<EditText>(R.id.userNameField)
        val userAge = findViewById<EditText>(R.id.userAge)
        val userWeight = findViewById<EditText>(R.id.userWeight)
        val userHeight = findViewById<EditText>(R.id.userHeight)
        val knownIllnesses = findViewById<EditText>(R.id.userHeight)
        val userEmContactName = findViewById<EditText>(R.id.userEmergencyContactName)
        val userEmContactNumber = findViewById<EditText>(R.id.userEmergencyContactNumber)
        val profileScreenSubmitBtn = findViewById<Button>(R.id.profileScreen1NextBtn)
        val displayUser = findViewById<TextView>(R.id.displayUser)

        profileScreenSubmitBtn.setOnClickListener{
            Toast.makeText(this@MainActivity, "Name: ${userName.getText().toString()} :: Age: ${userAge.getText().toString()} :: " +
                    "Weight: ${userWeight.getText().toString()} :: Height: ${userHeight.getText().toString()} :: Known Illnesses: ${knownIllnesses.getText().toString()} :: " +
                    "Emergency Contact Name: ${userEmContactName.getText().toString()} :: Emergency Contact Number: ${userEmContactNumber.getText().toString()}", Toast.LENGTH_LONG).show()
            displayUser.text = "Name: ${userName.getText().toString()} :: Age: ${userAge.getText().toString()} :: "
        }
    }
}