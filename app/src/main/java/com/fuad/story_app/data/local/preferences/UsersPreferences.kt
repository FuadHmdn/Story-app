package com.fuad.story_app.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.fuad.story_app.data.remote.response.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class UsersPreferences private constructor(private val dataStore: DataStore<Preferences>){

    private val TOKEN = stringPreferencesKey("token")
    private val USER_ID = stringPreferencesKey("user_id")
    private val USER_NAME = stringPreferencesKey("user_name")
    private val IS_LOGIN = booleanPreferencesKey("is_login")

    fun getLoginSession(): Flow<LoginResult> {
        return dataStore.data.map {
            LoginResult(
                it[USER_NAME]?: "",
                it[USER_ID]?: "",
                it[TOKEN]?: ""
            )
        }
    }

    fun getLoginStatus(): Flow<Boolean> {
        return dataStore.data.map {
            it[IS_LOGIN]?: false
        }
    }

    suspend fun saveLoginSession(session: LoginResult){
        dataStore.edit { preferences ->
            preferences[TOKEN] = session.token
            preferences[USER_ID] = session.userId
            preferences[USER_NAME] = session.name
        }
    }

    suspend fun saveLoginStatus(isLogin: Boolean){
        dataStore.edit { preferences ->
            preferences[IS_LOGIN] = isLogin
        }
    }

    suspend fun removeLoginSession(){
        dataStore.edit { preferences ->
            preferences.clear()
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