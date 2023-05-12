package com.example.respalyzerproject

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.respalyzerproject.userprofile.UserViewModel

class EmergencyContactsActivity : AppCompatActivity() {
    private lateinit var ecUserViewModel: UserViewModel
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

        ecUserViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        // putExtra Variable Assignments
        /*
        val ecEmerContactName = intent.getStringExtra("EXTRA_EMERUSERNAME")
        val ecEmerContactNumber = intent.getStringExtra("EXTRA_EMERUSERNUMBER")

         */

        // call button variable
        val ecCallButtonib = findViewById<ImageButton>(R.id.ecCallButton)

        ecUserViewModel.readUserProfile()
        ecUserViewModel.userProfile.observe(this){
            ecEmerContNametv.text = it.emergContactName.toString()
            ecEmerContNumtv.text = it.emergContactNum.toString()
        }

        ecDashboardScreen.setOnClickListener{
            Intent(this, DashboardActivity::class.java).also{

                // starts the new activity (next screen, in this case)
                startActivity(it)
            }
        }
        ecViewMedCentres.setOnClickListener{
            Intent(this, DashboardActivity::class.java).also{

                // starts the new activity (next screen, in this case)
                startActivity(it)
            }
        }

        // Calling the User
        ecCallButtonib.setOnClickListener{
            // allows the phone app to be called and the contact's number is dialed
            // but not called
            // -CALL permission had to be added to AndroidManifest.xml file-
            val it = Intent(Intent.ACTION_DIAL)
            it.data = Uri.parse("tel:${ecEmerContNumtv.text}")
            startActivity(it)
            // it works!!!!!!!!!!!!!!! finally!!!
        }

    }
}