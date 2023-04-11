package com.example.respalyzerproject.audiorecord

import java.io.File

// abstraction of what the recorder should do
interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}