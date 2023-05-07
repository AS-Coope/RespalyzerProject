package com.example.respalyzerproject.audioplayback

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
}