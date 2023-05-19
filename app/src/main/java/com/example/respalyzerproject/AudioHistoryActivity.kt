package com.example.respalyzerproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.respalyzerproject.audiodatabase.AudioDatabase
import com.example.respalyzerproject.audiodatabase.AudioEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AudioHistoryActivity : AppCompatActivity() {

    private lateinit var audioRecords: ArrayList<AudioEntity>
    private lateinit var audioAdapter: Adapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_history)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)
        // database related initializations
        val audioDao = AudioDatabase.getDatabase(application).audioEntityDao()
        audioRecords = ArrayList()
        audioAdapter = Adapter(audioRecords)

        recyclerview.apply{
            adapter = audioAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // getting the data from the database and storing it in the arraylist to display
        CoroutineScope(Dispatchers.IO).launch {
            audioRecords.addAll(audioDao.getAllRecords()) // database operations happen on a background thread

        }
        audioAdapter.notifyDataSetChanged() // but view/ui updates have to happen on the ui thread

        val toDashboardBtn = findViewById<Button>(R.id.ahDashboardBtn)
        val profileBtn = findViewById<ImageButton>(R.id.dsUserAccount3)

        toDashboardBtn.setOnClickListener {
            Intent(this, DashboardActivity::class.java).also{
                startActivity(it) // travel back to the dashboard screen
            }
        }

        profileBtn.setOnClickListener{
            // switch to the name of the Analyze Audio activity when that activity is created
            Intent(this, UserProfileActivity::class.java).also{
                // starts the new activity (next screen, in this case)
                startActivity(it)
            }
        }
    }
}