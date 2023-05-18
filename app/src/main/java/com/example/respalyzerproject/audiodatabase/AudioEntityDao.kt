package com.example.respalyzerproject.audiodatabase

// import android.media.AudioRecord
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AudioEntityDao {

    @Query("SELECT * FROM audio")
    fun getAllRecords(): List<AudioEntity>

    @Insert
    suspend fun insert(vararg audioRecord: AudioEntity)

    @Delete
    fun delete(audioRecord: AudioEntity)

    // deleting all entries in a database
    @Query("DELETE FROM audio")
    suspend fun deleteAllAudioRecords()
}