package com.example.respalyzerproject.audiodatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audio")
data class AudioEntity (
    @PrimaryKey(autoGenerate = true)
    var audioId: Int = 0,
    var fileName: String,
    var filePath: String,
    var timeStamp: Long,
    var duration: String
    )