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

class AudioDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_details)

        val profileBtn = findViewById<ImageButton>(R.id.dsUserAccount3)
        val toDashboardBtn = findViewById<Button>(R.id.adDashboardBtn)

        profileBtn.setOnClickListener{
            // switch to the name of the Analyze Audio activity when that activity is created
            Intent(this, UserProfileActivity::class.java).also{
                // starts the new activity (next screen, in this case)
                startActivity(it)
            }
        }

        toDashboardBtn.setOnClickListener {
            Intent(this, DashboardActivity::class.java).also{
                startActivity(it) // travel back to the dashboard screen
            }
        }
        // Fetch recording data from Flask API
        fetchRecordingData(15)
    }

    private fun fetchRecordingData(recordingId: Int) {
        val client = OkHttpClient()
        val recordingTextView = findViewById<TextView>(R.id.adTitle)


        val request = Request.Builder()
            .url("http://ip-here/recording/$recordingId")
            .build()

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }
            val jsonData = response.body?.string()

            if (response.isSuccessful && !jsonData.isNullOrEmpty()) {
                // Parse JSON response
                val jsonObject = JSONObject(jsonData)
                val recordingArray = jsonObject.optJSONArray("recordings")

                if (recordingArray != null && recordingArray.length() > 0) {
                    val recording = recordingArray.getJSONObject(0)
                    val name = recording.optString("recording")

                    // Display recording data in the TextView
                    recordingTextView.text = name

                }
            } else {
                // Handle error case
                recordingTextView.text = "Error fetching recording data"
            }
        }
    }
}