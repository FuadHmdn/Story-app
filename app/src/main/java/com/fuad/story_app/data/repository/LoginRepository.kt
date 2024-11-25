package com.fuad.story_app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fuad.story_app.data.remote.ApiService
import com.fuad.story_app.data.remote.response.ErrorResponse
import com.fuad.story_app.data.remote.response.LoginResult
import com.google.gson.Gson
import retrofit2.HttpException
import java.net.SocketTimeoutException

class LoginRepository(
    private val apiService: ApiService
) {

    private var _loginLoading = MutableLiveData<Boolean>()
    val loginLoading: LiveData<Boolean> get() = _loginLoading

    private var _isLoginSuccess = MutableLiveData<Boolean?>()
    private var _loginResult = MutableLiveData<LoginResult?>()
    val loginResult: LiveData<LoginResult?> get() = _loginResult
    private var _loginMessage = MutableLiveData<String?>()
    val loginMessage: LiveData<String?> get() = _loginMessage


    private var _isRegisterSucces = MutableLiveData<Boolean?>()
    val isRegisterSuccess: LiveData<Boolean?> get() = _isRegisterSucces
    private var _registerMessage = MutableLiveData<String?>()
    val registerMessage: LiveData<String?> get() = _registerMessage

    suspend fun register(name: String, email: String, password: String) {
        _loginLoading.value = true
        try {
            _loginLoading.value = false
            val result = apiService.register(name, email, password)
            _registerMessage.value = result.message
            _isRegisterSucces.value = true

        } catch (e: HttpException) {
            _loginLoading.value = false
            _isRegisterSucces.value = false
            val json = e.response()?.errorBody()?.string()
            val result = Gson().fromJson(json, ErrorResponse::class.java)
            _registerMessage.value = result.message
        } catch (e: Exception) {
            _loginLoading.value = false
            _isRegisterSucces.value = false
            _registerMessage.value = e.message
        } catch (e: SocketTimeoutException) {
            _loginLoading.value = false
            _isRegisterSucces.value = false
            _registerMessage.value = e.message
        }
    }

    suspend fun login(email: String, password: String) {
        _loginLoading.value = true
        try {
            _loginLoading.value = false
            val result = apiService.login( email, password)
            _loginResult.value = result.loginResult
            _loginMessage.value = result.message
            _isLoginSuccess.value = true
        } catch (e: HttpException) {
            _loginLoading.value = false
            _isLoginSuccess.value = false
            val json = e.response()?.errorBody()?.string()
            val result = Gson().fromJson(json, ErrorResponse::class.java)
            _loginMessage.value = result.message
        } catch (e: Exception) {
            _loginLoading.value = false
            _isLoginSuccess.value = false
            _loginMessage.value = e.message
        } catch (e: SocketTimeoutException) {
            _loginLoading.value = false
            _isLoginSuccess.value = false
            _loginMessage.value = e.message
        }
    }

    fun clearMessage(){
        _loginMessage.value = null
        _registerMessage.value = null
    }

    fun clearLoginResult(){
        _loginResult.value = null
    }

    companion object {
        @Volatile
        private var instance: LoginRepository? = null

        fun getInstance(apiService: ApiService): LoginRepository {
            return instance ?: synchronized(this) {
                instance ?: LoginRepository(apiService)
            }.also { instance = it }
        }
    }
}