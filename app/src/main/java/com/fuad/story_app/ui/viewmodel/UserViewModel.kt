package com.fuad.story_app.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fuad.story_app.data.remote.response.ListStoryItem
import com.fuad.story_app.data.remote.response.LoginResult
import com.fuad.story_app.data.remote.response.Story
import com.fuad.story_app.data.repository.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

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

    val getDetailResult: LiveData<Story> get() = userRepository.getDetailResult
    val getDetailMessage: LiveData<String?> get() = userRepository.getDetailMessage

    val isSuccessAddStory: LiveData<Boolean> get() = userRepository.isSuccessAddStory
    val getAddStoryMessage: LiveData<String?> get() = userRepository.getSubmitResult

    var currentUri: Uri? = null

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

    fun getDetailStory(id: String){
        viewModelScope.launch {
            userRepository.getDetailStories(id)
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

    fun addStory(multipart: MultipartBody.Part, desc: RequestBody){
        viewModelScope.launch {
            userRepository.addStories(multipart, desc)
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

    fun clearAddStoryStatus(){
        userRepository.clearSubmitStatus()
    }
}