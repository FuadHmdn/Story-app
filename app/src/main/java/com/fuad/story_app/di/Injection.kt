package com.fuad.story_app.di

import android.content.Context
import android.util.Log
import com.fuad.story_app.data.remote.ApiConfig
import com.fuad.story_app.data.local.preferences.UsersPreferences
import com.fuad.story_app.data.local.preferences.dataStore
import com.fuad.story_app.data.repository.LoginRepository
import com.fuad.story_app.data.repository.StoryRepository
import com.fuad.story_app.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideUserRepository(context: Context): UserRepository  {
        val pref = UsersPreferences.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    fun provideStoryRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService)
    }

    fun provideLoginRepository(): LoginRepository {
        val apiService = ApiConfig.getApiService()
        return LoginRepository(apiService)
    }
}