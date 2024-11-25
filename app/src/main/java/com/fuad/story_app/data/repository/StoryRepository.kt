package com.fuad.story_app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fuad.story_app.data.remote.ApiService
import com.fuad.story_app.data.remote.response.ListStoryItem
import com.fuad.story_app.data.remote.response.Response
import com.google.gson.Gson
import retrofit2.HttpException
import java.net.SocketTimeoutException

class StoryRepository(
    private val apiService: ApiService
) {

    private var _listStoryItem = MutableLiveData<List<ListStoryItem?>?>()
    val listStoryItem: LiveData<List<ListStoryItem?>?> get() = _listStoryItem
    private var _getStoryMessage: String? = null
    val getStoryMessage get() = _getStoryMessage

    suspend fun getAllStory(token: String) {
        try {
            val result = apiService.getAllStories("Bearer $token")
            _listStoryItem.value = result.listStory
            _getStoryMessage = result.message
        } catch (e: HttpException) {
            val json = e.response()?.errorBody()?.string()
            val result = Gson().fromJson(json, Response::class.java)
            _getStoryMessage = result.message
        } catch (e: SocketTimeoutException) {
            _getStoryMessage = e.message
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(apiService: ApiService): StoryRepository {
            return instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
        }
    }

}