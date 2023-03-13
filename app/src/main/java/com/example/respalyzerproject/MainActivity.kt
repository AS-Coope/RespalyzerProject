package com.example.respalyzerproject

import android.content.Intent
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
        val knownIllnesses = findViewById<EditText>(R.id.userKnownIllnesses)
        val userEmContactName = findViewById<EditText>(R.id.userEmergencyContactName)
        val userEmContactNumber = findViewById<EditText>(R.id.userEmergencyContactNumber)
        val profileScreenSubmitBtn = findViewById<Button>(R.id.profileScreen1NextBtn)
        val displayUser = findViewById<TextView>(R.id.displayUser)

        profileScreenSubmitBtn.setOnClickListener{

            /*Toast.makeText(this@MainActivity, "Name: ${userName.getText().toString()} :: Age: ${userAge.getText().toString()} :: " +
                    "Weight: ${userWeight.getText().toString()} :: Height: ${userHeight.getText().toString()} :: Known Illnesses: ${knownIllnesses.getText().toString()} :: " +
                    "Emergency Contact Name: ${userEmContactName.getText().toString()} :: Emergency Contact Number: ${userEmContactNumber.getText().toString()}", Toast.LENGTH_LONG).show()
             */

            // checking to ensure all the required fields are filled before submission
            displayUser.text = ""
            if (userName.getText().toString() == "" || userAge.getText().toString() == "" || userWeight.getText().toString() == "" || userHeight.getText().toString() == ""){
                if (userName.getText().toString() == ""){
                    displayUser.text = "Name is required to be filled out"
                }
                if (userAge.getText().toString() == "" ){
                    displayUser.text = "Age is required to be filled out"
                }
                if (userWeight.getText().toString() == "" ){
                    displayUser.text = "Weight is required to be filled out"
                }
                if (userHeight.getText().toString() == "" ){
                    displayUser.text = "Height is required to be filled out"
                }
            } else {

                /*displayUser.text = "Name: ${userName.getText().toString()} :: Age: ${userAge.getText().toString()} :: " +
                    "Weight: ${userWeight.getText().toString()} :: Height: ${userHeight.getText().toString()} :: Known Illnesses: ${knownIllnesses.getText().toString()} :: " +
                    "Emergency Contact Name: ${userEmContactName.getText().toString()} :: Emergency Contact Number: ${userEmContactNumber.getText().toString()}"
                */
                // goes to the welcome screen
                Intent(this, WelcomeScreenActivity::class.java).also{
                    // putExtra allows the passing of data from one activity to the next using an Intent
                    it.putExtra("EXTRA_USERNAME", userName.text.toString())
                    it.putExtra("EXTRA_USERAGE", userAge.text.toString().toInt())
                    it.putExtra("EXTRA_USERWEIGHT", userWeight.text.toString().toFloat())
                    it.putExtra("EXTRA_USERHEIGHT", userHeight.text.toString().toFloat())
                    it.putExtra("EXTRA_KNOWNILLNESSES", knownIllnesses.text.toString())
                    it.putExtra("EXTRA_EMERGNAME", userEmContactName.text.toString())
                    it.putExtra("EXTRA_EMERGNUMBER", userEmContactNumber.text.toString())

                    // starts the new activity (next screen, in this case)
                    startActivity(it)
                }
            }
        }
    }
}