package com.example.respalyzerproject

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.respalyzerproject.userprofile.User
import com.example.respalyzerproject.userprofile.UserViewModel

class UserProfileActivity : AppCompatActivity() {

    private lateinit var mUserViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        var displayName = findViewById<TextView>(R.id.tvUserName)
        var displayAge = findViewById<TextView>(R.id.tvUserAge)
        var displayWeight = findViewById<TextView>(R.id.tvUserWeight)
        var displayHeight= findViewById<TextView>(R.id.tvUserHeight)

        // necessary for the update user that takes place down in the dialog box

        // show user profile
        val emerContactsBtn = findViewById<Button>(R.id.dbScreenEmergencyBtn)
        val ecDashboardScreen = findViewById<Button>(R.id.ecScreenDashboardBtn)
        val userProfileBtn = findViewById<ImageButton>(R.id.ecUserAccount)

        mUserViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        mUserViewModel.readUserProfile()

        // shows relevant user data on user profile screen
        mUserViewModel.userProfile.observe(this){
            displayName.text = it.name
            displayAge.text = it.age.toString()
            displayWeight.text = it.weight.toString()
            displayHeight.text = it.height.toString()
        }

        userProfileBtn.setOnClickListener {
            Intent(this, UserProfile::class.java).also{

                // starts the new activity (next screen, in this case)
                startActivity(it)
            }
        }

        emerContactsBtn.setOnClickListener{
            Intent(this, EmergencyContactsActivity::class.java).also{
                // starts the new activity (next screen, in this case)
                startActivity(it)
            }
        }

        ecDashboardScreen.setOnClickListener{
            Intent(this, DashboardActivity::class.java).also{
                // starts the new activity (next screen, in this case)
                startActivity(it)
            }
        }

        // dialog box operations
        val dialogShowBtn: ImageButton = findViewById(R.id.editName)

        dialogShowBtn.setOnClickListener {

            val dialogBinding = layoutInflater.inflate(R.layout.change_user_name_dialog, null)

            var dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(dialogBinding)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            // dialog box buttons
            val addBtn: Button = dialogBinding.findViewById(R.id.btnAdd)
            val cancelBtn: Button = dialogBinding.findViewById(R.id.btnCancel)

            // dialog text view
            var nameEt: EditText = dialogBinding.findViewById(R.id.etChangeName)

            dialog.setCancelable(true)

            cancelBtn.setOnClickListener{
                dialog.dismiss()
            }

            addBtn.setOnClickListener {

                // as of 4:25PM May 14, 2023, this code does not accomplish the task
                // of updating the user's name in the database and then displaying that update onto the screen

                // getting the user from the database
                var newUser = mUserViewModel.getUser(1)
                // storing the user's name now as this new name
                newUser.name = nameEt.text.toString()
                // storing the user back in the database
                mUserViewModel.updateTheUserName(newUser)

                dialog.dismiss()
            }
        }
    }
}