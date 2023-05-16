package com.example.respalyzerproject.userprofile

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id") val id:Int,
    @ColumnInfo(name = "user_name") val name:String,
    @ColumnInfo(name = "user_age") val age:Int,
    @ColumnInfo(name = "user_gender") val gender:String,
    @ColumnInfo(name = "user_weight") val weight:Float,
    @ColumnInfo(name = "user_height") val height:Float,
    @ColumnInfo(name = "user_illness") val illnesses:String,
    @ColumnInfo(name = "user_ec_name") val emergContactName:String,
    @ColumnInfo(name = "user_ec_number") val emergContactNum:String
)