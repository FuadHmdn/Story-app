package com.fuad.story_app.data.repository

import com.fuad.story_app.data.api.ApiService
import com.fuad.story_app.data.local.preferences.UsersPreferences

class UserRepository(
    private val apiService: ApiService,
    private val usersPreferences: UsersPreferences
) {

    suspend fun register(name: String, email: String, password: String){
        apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String) {
        apiService.login(email, password)
    }
}