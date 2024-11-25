package com.fuad.story_app.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fuad.story_app.data.remote.response.LoginResult
import com.fuad.story_app.data.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    val isRegisterSuccess get() = loginRepository.isRegisterSuccess
    val loginMessage: LiveData<String?> get() = loginRepository.loginMessage
    val loginResult: LiveData<LoginResult?> get() = loginRepository.loginResult
    val registerMessage get() = loginRepository.registerMessage
    val loginLoading: LiveData<Boolean> get() = loginRepository.loginLoading

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            loginRepository.register(name, email, password)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginRepository.login(email, password)
        }
    }

    fun clearMessage(){
        loginRepository.clearMessage()
    }

    fun clearLoginResult(){
        loginRepository.clearLoginResult()
    }
}