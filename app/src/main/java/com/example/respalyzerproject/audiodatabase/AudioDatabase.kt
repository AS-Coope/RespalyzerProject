package com.example.respalyzerproject.audiodatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [AudioEntity::class], version = 1, exportSchema = false)
abstract class AudioDatabase: RoomDatabase() {

    abstract fun audioEntityDao(): AudioEntityDao

    companion object{
        @Volatile // writes to this (INSTANCE) field are immediately seen by other threads
        private var INSTANCE: AudioDatabase? = null

        // creating the audio database once it does not already exist
        // database operations are resource demanding/intensive so it
        // making sure only one instance of the specific database is created
        // is paramount

        fun getDatabase(context: Context): AudioDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AudioDatabase::class.java,
                    "audio_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }


}