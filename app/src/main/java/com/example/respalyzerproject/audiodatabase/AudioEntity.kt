package com.example.respalyzerproject.audiodatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "audio")
data class AudioEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recording_id") val audioId: Int,
    @ColumnInfo(name = "user_id") val userId: Int = 1,
    @ColumnInfo(name = "audio_file_path") val filePath: String,
    @ColumnInfo(name = "recording_length")val duration: String,
    @ColumnInfo(name = "file_size") val fileSize: String,
    @ColumnInfo(name = "diagnosis_reading") val reading:String,
    @ColumnInfo(name = "date_recorded") val date:String
)