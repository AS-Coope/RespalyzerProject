package com.example.respalyzerproject.userprofile

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {

    val readAllData: LiveData<List<User>> = userDao.readAllData()

    fun addUser(user:User){
        userDao.addUser(user)
    }
}