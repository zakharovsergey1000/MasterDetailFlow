package com.example.sergey.myapplication

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {
    private val users: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>().also {
            loadUsers()
        }
    }

    fun getUsers(): LiveData<List<User>> {
        return users
    }

    private val repository: Repository = Repository()
    fun loadUsers() {
        // Do an asynchronous operation to fetch users.
        viewModelScope.launch {
            // Coroutine that will be canceled when the ViewModel is cleared.
            val retrievedData = repository.getUsers()
            if (retrievedData.status == Status.SUCCESS) {
                users.postValue(retrievedData.data)
            }
//            emit(retrievedData)
            _usersResource.postValue(retrievedData)
        }
    }

    private val _usersResource: MutableLiveData<Resource<List<User>>> by lazy {
        MutableLiveData<Resource<List<User>>>()
    }
    val usersResource: LiveData<Resource<List<User>>> = _usersResource
}