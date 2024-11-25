package com.fuad.story_app.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fuad.story_app.data.local.model.DataPreferences
import com.fuad.story_app.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    var getLoginSession: LiveData<DataPreferences> = userRepository.getLoginSession().asLiveData()
    var getTokenUser: LiveData<String> = userRepository.getTokenUser().asLiveData()

    fun saveLoginSession(token: String, userId: String, name: String, onSuccess:()-> Unit) {
        viewModelScope.launch {
            userRepository.saveLoginSession(token, userId, name)
            onSuccess()
        }
    }

    fun removeLoginSession() {
        viewModelScope.launch {
            userRepository.removeLoginSession()
        }
    }
}
