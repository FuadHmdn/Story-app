package com.fuad.story_app.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fuad.story_app.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository): ViewModel() {

    val isRegisterSuccess: LiveData<Boolean?> get() = userRepository.isRegisterSuccess
    val registerMessage: LiveData<String?> get() = userRepository.registerMessage
    val isLoading: LiveData<Boolean> get() = userRepository.isLoading

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

    fun clearRegister(){
        userRepository.clearRegister()
    }

    fun removeSession(){
        viewModelScope.launch {
            userRepository.removeSession()
        }
    }

    fun getSession(): String? {
        var token: String? = null
        viewModelScope.launch {
            token = userRepository.getToken()
        }
        return token
    }
}