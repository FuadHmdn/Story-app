package com.fuad.story_app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fuad.story_app.data.remote.ApiService
import com.fuad.story_app.data.remote.response.Response
import com.fuad.story_app.data.local.preferences.UsersPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
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

    suspend fun saveToken(token: String) {
        usersPreferences.saveLoginSession(token)
    }

    suspend fun getToken(): String? {
        val token = usersPreferences.getLoginSession().first()
        return if (token.isEmpty()) {
            null
        } else {
            token
        }
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

    fun clearRegister() {
        if (isRegisterSuccess != null || registerMessage != null) {
            _isRegisterSuccess.value = null
            _registerMessage.value = null
        }
    }

    suspend fun login(email: String, password: String) {
        apiService.login(email, password)
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
}