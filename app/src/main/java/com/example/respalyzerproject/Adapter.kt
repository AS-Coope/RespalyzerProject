package com.example.respalyzerproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.respalyzerproject.audiodatabase.AudioEntity

class Adapter(var audioRecords: ArrayList<AudioEntity>): RecyclerView.Adapter<Adapter.ViewHolder>() {

    inner class ViewHolder(theView: View): RecyclerView.ViewHolder(theView){
        var fileName: TextView = theView.findViewById(R.id.tvFileName)
        var fileSize: TextView = theView.findViewById(R.id.tvFileSize)
        var fileLength: TextView = theView.findViewById(R.id.tvFileLength)
        var check: CheckBox = theView.findViewById(R.id.checkbox)

    }

    // creates the audio layout but does not fill in the various text fields
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val startView = LayoutInflater.from(parent.context).inflate(R.layout.audio_layout, parent, false)
        return ViewHolder(startView)
    }

    override fun getItemCount(): Int {
        return audioRecords.size
    }

    // here is where the data is now decided when to be shown on screen/ filled in its relevant text fields, respectively
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position!= RecyclerView.NO_POSITION){
            var record = audioRecords[position]
            holder.fileName.text = record.date
            holder.fileSize.text = record.fileSize
            holder.fileLength.text = record.duration
        }
    }
}