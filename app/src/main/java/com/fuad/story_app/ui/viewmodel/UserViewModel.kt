package com.fuad.story_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fuad.story_app.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository): ViewModel() {

    fun register(name: String, email: String, password: String){
        viewModelScope.launch {
            userRepository.register(name, email, password)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            userRepository.login(email, password)
        }
    }

    fun removeSession(){
        viewModelScope.launch {
            userRepository.removeSession()
        }
    }
}