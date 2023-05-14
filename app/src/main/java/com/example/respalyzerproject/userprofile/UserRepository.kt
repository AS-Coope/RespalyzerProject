package com.example.respalyzerproject.userprofile

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    val readAllData: LiveData<List<User>> = userDao.readAllData()
    //val userProfile = MutableLiveData<User>()

    suspend fun addUser(user:User){
        userDao.addUser(user)
    }

    // getting the user (profile) from the database
    @WorkerThread
    fun readUser(): Flow<User?>{
        return userDao.readUser()
    }

    suspend fun getUserCount(): Int {
        return withContext(Dispatchers.IO) {
            userDao.getUserCount()
        }
    }
}