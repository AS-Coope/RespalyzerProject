package com.example.respalyzerproject

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String,
    val age:String,
    val gender:String,
    val weight:String,
    val height:String,
    val illnesses:String,
    val emergContactName:String,
    val emergContactNum:String
)