package com.example.respalyzerproject.userprofile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application:Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<User>>
    val userProfile = MutableLiveData<User>()

    private val repository: UserRepository

    // called first when the user view model is called
    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        readAllData = repository.readAllData
    }

    // launching database related activities on a separate thread
    // bad practice to do database jobs front the main thread
    // run them in a background thread instead
    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }
    fun readUserProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieves the first user (and should be only user) of the app
            repository.readUser().collect{userProfile.postValue(it)}
        }
    }
}