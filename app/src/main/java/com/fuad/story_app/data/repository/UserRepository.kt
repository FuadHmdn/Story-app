package com.fuad.story_app.data.repository

import com.fuad.story_app.data.local.model.DataPreferences
import com.fuad.story_app.data.local.preferences.UsersPreferences
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val usersPreferences: UsersPreferences
) {

    fun getLoginSession(): Flow<DataPreferences> {
        return usersPreferences.getLoginSession()
    }

    fun getTokenUser(): Flow<String>{
        return usersPreferences.getTokenUser()
    }

    suspend fun saveLoginSession(token: String, userId: String, name: String) {
        usersPreferences.saveLoginSession(token, userId, name)
    }

    suspend fun removeLoginSession() {
        usersPreferences.removeLoginSession()
    }

    companion object{

        @Volatile
        private var instance: UserRepository? = null

        fun getInstance( usersPreferences: UsersPreferences): UserRepository {
            return instance?: synchronized(this){
                instance?: UserRepository(usersPreferences)
            }.also { instance = it }
        }

    }
}