package com.example.respalyzerproject.audioplayback

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File
import java.io.IOException

class AndroidAudioPlayer(
    private val context: Context
):AudioPlayer {

    private var player: MediaPlayer? = null
    /*
    override fun playFile(file: File?) {
        if (file != null) {
            MediaPlayer.create(context, file.toUri()).apply{
                player = this
                start()
            }
        }
    }
     */
    override fun playFile(file: File?) {
        player = MediaPlayer()
        try {
            if (file != null) {
                // deals with playing the file properly
                player?.setDataSource(file.path)
                player?.prepare()
                player?.start()
            }
        } catch (e: IOException) {
            print("File was not found/ File error occurred")
        } finally {
            player?.setOnCompletionListener {
                // releasing the player resources once playing is complete
                player?.stop()
            }
        }
    }

    override fun stop() {
        player?.stop() // stop it playing
        player?.release() // release the resources of the player
        player = null
    }
}