package com.example.respalyzerproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.respalyzerproject.audiodatabase.AudioDatabase
import com.example.respalyzerproject.audiodatabase.AudioEntity
import com.example.respalyzerproject.audioplayback.AndroidAudioPlayer
import com.example.respalyzerproject.audiorecord.AndroidAudioRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class AudioHistoryActivity : AppCompatActivity(), onItemClickListener {

    private lateinit var audioRecords: ArrayList<AudioEntity>
    private lateinit var audioAdapter: Adapter
    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_history)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)
        // database related initializations
        val audioDao = AudioDatabase.getDatabase(application).audioEntityDao()
        audioRecords = ArrayList()
        audioAdapter = Adapter(audioRecords, this)

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

    override fun onItemClickListener(position: Int) {

        // determining which audio to play in the recycler view
        var audio = audioRecords[position]
        var audioFileName = "audio_rec_101_${audio.date}.mp3"

        // getting the audio file from the app's cache
        var theAudioFile = File(cacheDir, audioFileName)

        //println(theAudioFile.absoluteFile)
        player.playFile(theAudioFile)
    }
}