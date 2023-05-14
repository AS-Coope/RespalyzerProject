package com.example.respalyzerproject.userprofile

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/*class UserViewModel(application:Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<User>>
    val userProfile = MutableLiveData<User>()

    private val repository: UserRepository

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


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
    fun getUserCount(): LiveData<Int> {
        return liveData {
            emit(repository.getUserCount())
        }
    }
}*/

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    val readAllData: LiveData<List<User>>
    val userProfile = MutableLiveData<User>()

    //private val repository: UserRepository

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    // called first when the user view model is called
    init {
        //val userDao = UserDatabase.getDatabase(application).userDao()
        //repository = UserRepository(userDao)
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
    fun getUserCount(): LiveData<Int> {
        return liveData {
            emit(repository.getUserCount())
        }
    }
}