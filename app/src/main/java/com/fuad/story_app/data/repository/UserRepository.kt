package com.fuad.story_app.data.repository

import com.fuad.story_app.data.api.ApiService
import com.fuad.story_app.data.local.preferences.UsersPreferences
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository(
    private val apiService: ApiService,
    private val usersPreferences: UsersPreferences
) {

    suspend fun saveToken(token: String){
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

    suspend fun removeSession(){
        usersPreferences.removeLoginSession()
    }

    suspend fun register(name: String, email: String, password: String){
        apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String) {
        apiService.login(email, password)
    }

    suspend fun addStories(multipart: MultipartBody.Part, desc: RequestBody){
        apiService.addStory(multipart, desc)
    }

    suspend fun getAllStories(size: Int) {
        apiService.getAllStories(size)
    }

    suspend fun getDetailStories(id: String){
        apiService.getDetailStories(id)
    }
}