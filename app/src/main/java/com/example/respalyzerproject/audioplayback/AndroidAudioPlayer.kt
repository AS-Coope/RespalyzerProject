package com.example.respalyzerproject.audioplayback

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File

class AndroidAudioPlayer(
    private val context: Context
):AudioPlayer {

    private var player: MediaPlayer? = null
    override fun playFile(file: File) {
        MediaPlayer.create(context, file.toUri()).apply{
            player = this
            start()
        }
    }

    override fun stop() {
        player?.stop() // stop it playing
        player?.release() // release the resources of the player
        player = null
    }
}