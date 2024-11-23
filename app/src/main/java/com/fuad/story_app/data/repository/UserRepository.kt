package com.fuad.story_app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fuad.story_app.data.local.preferences.UsersPreferences
import com.fuad.story_app.data.remote.ApiService
import com.fuad.story_app.data.remote.response.LoginResponse
import com.fuad.story_app.data.remote.response.LoginResult
import com.fuad.story_app.data.remote.response.Response
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class UserRepository(
    private val apiService: ApiService,
    private val usersPreferences: UsersPreferences
) {

    private val _isRegisterSuccess = MutableLiveData<Boolean?>()
    val isRegisterSuccess: LiveData<Boolean?> get() = _isRegisterSuccess
    private val _registerMessage = MutableLiveData<String?>()
    val registerMessage: LiveData<String?> get() = _registerMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isLoginSuccess = MutableLiveData<Boolean?>()
    val isLoginSuccess: LiveData<Boolean?> get() = _isLoginSuccess
    private val _loginMessage = MutableLiveData<String?>()
    val loginMessage: LiveData<String?> get() = _loginMessage
    private val _loginResult = MutableLiveData<LoginResult?>()
    val loginResult: LiveData<LoginResult?> get() = _loginResult

    suspend fun saveSession(session: LoginResult ) {
        usersPreferences.saveLoginSession(session)
    }

    suspend fun saveLoginStatus(isLogin: Boolean ) {
        usersPreferences.saveLoginStatus(isLogin)
    }

    fun getSession(): Flow<LoginResult>{
        return usersPreferences.getLoginSession()
    }

    fun getLoginStatus(): Flow<Boolean>{
        return usersPreferences.getLoginStatus()
    }

    suspend fun removeSession() {
        usersPreferences.removeLoginSession()
    }

    suspend fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        try {
            val result = apiService.register(name, email, password)

            if (result.error) {
                _isLoading.value = false
                _isRegisterSuccess.value = true
                _registerMessage.value = result.message
            } else {
                _isLoading.value = false
                _isRegisterSuccess.value = false
                _registerMessage.value = result.message
            }

        } catch (e: HttpException) {
            _isLoading.value = false
            _isRegisterSuccess.value = true
            val json = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(json, Response::class.java)
            _registerMessage.value = errorBody.message

        }
    }

    fun clearRegisterLogin() {
        if (_isRegisterSuccess.value != null || _registerMessage.value != null) {
            _isRegisterSuccess.value = null
            _registerMessage.value = null
        }
        if (_isLoginSuccess.value != null || _loginMessage.value != null || _loginResult.value != null) {
            _isLoginSuccess.value = null
            _loginMessage.value = null
            _loginResult.value = null
        }
    }

    suspend fun login(email: String, password: String) {
        _isLoading.value = true
        try {
            val result = apiService.login(email, password)
            if (result.error) {
                _isLoading.value = false
                _isLoginSuccess.value = true
                _loginMessage.value = result.message
            } else {
                _isLoading.value = false
                _isLoginSuccess.value = false
                _loginMessage.value = result.message
                _loginResult.value = result.loginResult
            }
        } catch (e: HttpException) {
            _isLoading.value = false
            val json = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(json, LoginResponse::class.java)
            _registerMessage.value = errorBody.message
        }
    }

    suspend fun addStories(multipart: MultipartBody.Part, desc: RequestBody) {
        apiService.addStory(multipart, desc)
    }

    suspend fun getAllStories(size: Int) {
        apiService.getAllStories(size)
    }

    suspend fun getDetailStories(id: String) {
        apiService.getDetailStories(id)
    }

    fun clearMessage(){
        _loginMessage.value = null
        _registerMessage.value = null
    }
}