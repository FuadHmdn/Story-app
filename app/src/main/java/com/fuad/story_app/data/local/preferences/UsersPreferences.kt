package com.fuad.story_app.data.local.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.fuad.story_app.data.local.model.DataPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class UsersPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getLoginSession(): Flow<DataPreferences> {
        return dataStore.data.map {
            DataPreferences(
                it[USER_NAME] ?: "",
                it[USER_ID] ?: "",
                it[TOKEN] ?: ""
            )
        }
    }

    fun getTokenUser(): Flow<String> {
        return dataStore.data.map {
            it[TOKEN] ?: ""
        }
    }

    suspend fun saveLoginSession(token: String, userId: String, name: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
            preferences[USER_ID] = userId
            preferences[USER_NAME] = name
        }
        Log.d("PREFERENCES", "Menyimpan Token: $token")

    }

    suspend fun removeLoginSession() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN)
            preferences.remove(USER_ID)
            preferences.remove(USER_NAME)
        }
        Log.d("PREFERENCES", "Login session removed successfully")
    }

    companion object {

        private val TOKEN = stringPreferencesKey("token")
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")

        @Volatile
        private var INSTANCE: UsersPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UsersPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UsersPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}