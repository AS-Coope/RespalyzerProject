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
        // Fetch disease data from Flask API
        fetchDiseaseData(0)
    }

    private fun fetchDiseaseData(diseaseId: Int) {
        val client = OkHttpClient()
        val diseaseTextView = findViewById<TextView>(R.id.diName)
        val diseaseTextView2 = findViewById<TextView>(R.id.diDesc)
        val diseaseTextView3 = findViewById<TextView>(R.id.diSymptoms)
        val diseaseTextView4 = findViewById<TextView>(R.id.diTreatment)


        val request = Request.Builder()
            .url("http://192.168.100.81:8080/diseases/$diseaseId")
            .build()

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }
            val jsonData = response.body?.string()

            if (response.isSuccessful && !jsonData.isNullOrEmpty()) {
                // Parse JSON response
                val jsonObject = JSONObject(jsonData)
                val diseaseArray = jsonObject.optJSONArray("disease")

                if (diseaseArray != null && diseaseArray.length() > 0) {
                    val disease = diseaseArray.getJSONObject(0)
                    val name = disease.optString("name")
                    val description = disease.optString("description")
                    val symptoms = disease.optString("symptoms")
                    val treatment = disease.optString("treatment")

                    // Display disease data in the TextView
                    diseaseTextView.text = name
                    diseaseTextView2.text = description
                    diseaseTextView3.text = symptoms
                    diseaseTextView4.text = treatment

                }
            } else {
                // Handle error case
                diseaseTextView.text = "Error fetching disease data"
            }
        }
    }
}