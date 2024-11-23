package com.fuad.story_app.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fuad.story_app.data.remote.response.ListStoryItem
import com.fuad.story_app.data.remote.response.LoginResult
import com.fuad.story_app.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    val isRegisterSuccess: LiveData<Boolean?> get() = userRepository.isRegisterSuccess
    val registerMessage: LiveData<String?> get() = userRepository.registerMessage
    val isLoading: LiveData<Boolean> get() = userRepository.isLoading

    val isLoginSuccess: LiveData<Boolean?> get() = userRepository.isLoginSuccess
    val loginMessage: LiveData<String?> get() = userRepository.loginMessage
    val loginResult: LiveData<LoginResult?> get() = userRepository.loginResult

    val getAllResult: LiveData<List<ListStoryItem>> get() = userRepository.getALlResult
    val getAllMessage: LiveData<String?> get() = userRepository.getAllMessage
    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            userRepository.register(name, email, password)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            userRepository.login(email, password)
        }
    }

    fun clearRegisterLogin() {
        userRepository.clearRegisterLogin()
    }

    fun getAllStory() {
        viewModelScope.launch {
            userRepository.getAllStories(50)
        }
    }

    fun removeSession() {
        viewModelScope.launch {
            userRepository.removeSession()
        }
    }

    fun saveLoginStatus(isLogin: Boolean) {
        viewModelScope.launch {
            userRepository.saveLoginStatus(isLogin)
        }
    }

    fun getLoginStatus() {
        viewModelScope.launch {
            userRepository.getLoginStatus().collect {
                _loginStatus.value = it
            }
        }
    }

    fun saveSession(session: LoginResult) {
        viewModelScope.launch {
            userRepository.saveSession(session)
        }
    }

    fun clearMessage() {
        userRepository.clearMessage()
    }
}