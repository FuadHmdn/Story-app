package com.fuad.story_app.di

import android.content.Context
import com.fuad.story_app.data.remote.ApiConfig
import com.fuad.story_app.data.local.preferences.UsersPreferences
import com.fuad.story_app.data.local.preferences.dataStore
import com.fuad.story_app.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideUserRepository(context: Context): UserRepository  {
        val pref = UsersPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getLoginSession().first()}
        val apiService = ApiConfig.getApiService(user)

        return UserRepository(apiService, pref)
    }
}