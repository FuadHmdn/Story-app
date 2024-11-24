package com.fuad.story_app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fuad.story_app.data.local.preferences.UsersPreferences
import com.fuad.story_app.data.remote.ApiService
import com.fuad.story_app.data.remote.response.DetailStoryResponse
import com.fuad.story_app.data.remote.response.GetAllStoryResponse
import com.fuad.story_app.data.remote.response.ListStoryItem
import com.fuad.story_app.data.remote.response.LoginResponse
import com.fuad.story_app.data.remote.response.LoginResult
import com.fuad.story_app.data.remote.response.Response
import com.fuad.story_app.data.remote.response.Story
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.net.SocketTimeoutException

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

    private val _isGetAllSuccess = MutableLiveData<Boolean?>()
    private val _getAllMessage = MutableLiveData<String?>()
    val getAllMessage: LiveData<String?> get() = _getAllMessage
    private val _getALlResult = MutableLiveData<List<ListStoryItem>>()
    val getALlResult: LiveData<List<ListStoryItem>> get() = _getALlResult

    private val _getDetailMessage = MutableLiveData<String?>()
    val getDetailMessage: LiveData<String?> get() = _getDetailMessage
    private val _getDetailResult = MutableLiveData<Story>()
    val getDetailResult: LiveData<Story> get() = _getDetailResult

    suspend fun saveSession(session: LoginResult) {
        usersPreferences.saveLoginSession(session)
    }

    suspend fun saveLoginStatus(isLogin: Boolean) {
        usersPreferences.saveLoginStatus(isLogin)
    }

    fun getLoginStatus(): Flow<Boolean> {
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
        } catch (e: Exception) {
            _loginMessage.postValue(e.message)
            _isLoading.value = false
            _isLoginSuccess.value = true
        } catch (e: HttpException) {
            _isLoading.value = false
            _isLoginSuccess.value = true
            val json = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(json, LoginResponse::class.java)
            _loginMessage.value = errorBody.message
        } catch (e: SocketTimeoutException) {
            _isLoading.value = false
            _isLoginSuccess.value = true
            _loginMessage.value = e.message.toString()
        }
    }

    suspend fun addStories(multipart: MultipartBody.Part, desc: RequestBody) {
        apiService.addStory(multipart, desc)
    }

    suspend fun getAllStories(size: Int) {
        _isLoading.value = true
        try {
            val result = apiService.getAllStories(size)
            result.let {
                _isLoading.value = false
                if (!result.error) {
                    _isGetAllSuccess.value = true
                    _getAllMessage.value = result.message
                    _getALlResult.value = result.listStory
                } else {
                    _isGetAllSuccess.value = false
                    _getAllMessage.value = result.message
                    _getALlResult.value = emptyList()
                }
            }

        } catch (e: Exception) {
            _getAllMessage.value = e.message
            _isLoading.value = false
            _isGetAllSuccess.value = false
        } catch (e: HttpException) {
            _isLoading.value = false
            val json = e.response()?.errorBody()?.string()
            val response = Gson().fromJson(json, GetAllStoryResponse::class.java)
            _isGetAllSuccess.value = false
            _getAllMessage.value = response.message
            _getALlResult.value = response.listStory
        } catch (e: SocketTimeoutException) {
            _isLoading.value = false
            _isGetAllSuccess.value = false
            _getAllMessage.value = e.message.toString()
        }
    }

    suspend fun getDetailStories(id: String) {
        _isLoading.value = true
        try {
            val result = apiService.getDetailStories(id)
            _getDetailMessage.value = result.message
            _getDetailResult.value = result.story
        } catch (e: HttpException) {
            val json = e.response()?.errorBody()?.string()
            val result = Gson().fromJson(json, DetailStoryResponse::class.java)
            _loginMessage.value = result.message
        } catch (e: Exception) {
            _getAllMessage.value = e.message
            _isLoading.value = false
            _isGetAllSuccess.value = false
        }
    }

    fun clearMessage() {
        _loginMessage.value = null
        _registerMessage.value = null
        _getAllMessage.value = null
    }
}