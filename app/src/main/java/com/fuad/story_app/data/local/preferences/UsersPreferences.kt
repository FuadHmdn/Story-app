package com.fuad.story_app.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.log

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class UsersPreferences private constructor(val dataStore: DataStore<Preferences>){

    private val LOGIN_SESSION = stringPreferencesKey("login_session")

    fun getLoginSession(): Flow<String> {
        return dataStore.data.map {
            it[LOGIN_SESSION]?: ""
        }
    }

    suspend fun saveLoginSession(token: String){
        dataStore.edit { login ->
            login[LOGIN_SESSION] = token
        }
    }

    suspend fun removeLoginSession(){
        dataStore.edit { preferences ->
            preferences.remove(LOGIN_SESSION)
        }
    }

    companion object{

        @Volatile
        private var INSTANCE: UsersPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UsersPreferences{
            return INSTANCE?: synchronized(this){
                val instance = UsersPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}