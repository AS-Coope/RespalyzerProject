package com.example.respalyzerproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class IllnessActivity : AppCompatActivity() {
    private lateinit var diseaseId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_illness)

        val profileBtn = findViewById<ImageButton>(R.id.dsUserAccount3)
        val learnMoreBtn = findViewById<Button>(R.id.iLearnMore)
        val medCentreBtn = findViewById<Button>(R.id.iMedicalCentreBtn)
        val emerContactsBtn = findViewById<Button>(R.id.iEmergencyContact)


        // Fetch disease data from Flask API
        fetchDiseaseData()

        profileBtn.setOnClickListener{
            // switch to the name of the Analyze Audio activity when that activity is created
            Intent(this, UserProfileActivity::class.java).also{
                // starts the new activity (next screen, in this case)
                startActivity(it)
            }
        }

        learnMoreBtn.setOnClickListener {
            Intent(this, DiseaseInfoActivity::class.java).also{
                it.putExtra("diseaseId", diseaseId)
                startActivity(it) // travel back to the dashboard screen
            }
        }

        emerContactsBtn.setOnClickListener {
            Intent(this, EmergencyContactsActivity::class.java).also{
                startActivity(it) // travel back to the dashboard screen
            }
        }

        medCentreBtn.setOnClickListener {
            Intent(this, MedicalMapsActivity::class.java).also{
                startActivity(it) // travel back to the dashboard screen
            }
        }
    }

    private fun fetchDiseaseData() {
        val client = OkHttpClient()
        val diseaseTextView = findViewById<TextView>(R.id.arIllness)

        val request = Request.Builder()
            .url("http://your-ip-here/latestrecording")
            .build()

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }
            val jsonData = response.body?.string()

            if (response.isSuccessful && !jsonData.isNullOrEmpty()) {
                // Parse JSON response
                val jsonObject = JSONObject(jsonData)
                val diseaseArray = jsonObject.optJSONArray("recordings")

                if (diseaseArray != null && diseaseArray.length() > 0) {
                    val disease = diseaseArray.getJSONObject(0)
                    val name = disease.optString("reading")
                    val likelihood = disease.optString("likelihood")
                    diseaseId = disease.optString("disease_id")

                    // Display disease data in the TextView
                    val diseaseData = "Unfortunately, you have a $likelihood% similarity to someone who has $name"
                    diseaseTextView.text = diseaseData
                }
            } else {
                // Handle error case
                diseaseTextView.text = "Error fetching disease data"
            }
        }
    }
}