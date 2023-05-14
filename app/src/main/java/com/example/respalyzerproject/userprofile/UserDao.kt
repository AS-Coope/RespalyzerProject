package com.example.respalyzerproject.userprofile

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user:User)

    @Query("SELECT * FROM user_profile")
    fun readAllData(): LiveData<List<User>>

    // reading the first user from the database
    @Query("SELECT * FROM user_profile LIMIT 1")
    fun readUser(): Flow<User?>

    // counting to know when one user in database
    @Query("SELECT COUNT(*) FROM user_profile")
    fun getUserCount(): Int


}