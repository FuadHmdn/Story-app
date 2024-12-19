package com.fuad.story_app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.fuad.story_app.data.local.StoryPagingSource
import com.fuad.story_app.data.remote.ApiService
import com.fuad.story_app.data.remote.response.ErrorResponse
import com.fuad.story_app.data.remote.response.ListStoryItem
import com.fuad.story_app.data.remote.response.Story
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class StoryRepository(
    private val apiService: ApiService
) {

    private var _isStoryLoading = MutableLiveData<Boolean?>()
    val isStoryLoading: LiveData<Boolean?> get() = _isStoryLoading

    private var _listStoryItem = MutableLiveData<List<ListStoryItem?>?>()
    val listStoryItem: LiveData<List<ListStoryItem?>?> get() = _listStoryItem
    private var _getStoryMessage = MutableLiveData<String?>()
    val getStoryMessage: LiveData<String?> get() = _getStoryMessage

    private var _storyItem = MutableLiveData<Story?>()
    val storyItem: LiveData<Story?> get() = _storyItem
    private var _getDetailStoryMessage = MutableLiveData<String?>()
    val getDetailStoryMessage: LiveData<String?> get() = _getStoryMessage

    private var _addStoryMessage = MutableLiveData<String?>()
    val addStoryMessage: LiveData<String?> get() = _addStoryMessage
    private var _addStorySuccess = MutableLiveData<Boolean?>()
    val addStorySuccess: LiveData<Boolean?> get() = _addStorySuccess

    fun getStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 7
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, "Bearer $token", _getStoryMessage, _isStoryLoading)
            }

        ).liveData
    }

    suspend fun getAllStory(token: String) {
        try {
            val result = apiService.getStoriesLocation("Bearer $token")
            _listStoryItem.value = result.listStory
        } catch (e: HttpException) {
            val json = e.response()?.errorBody()?.string()
            val result = Gson().fromJson(json, ErrorResponse::class.java)
            _getStoryMessage.value = result.message
        } catch (e: SocketTimeoutException) {
            _getStoryMessage.value = e.message
        } catch (e: IOException) {
            _getStoryMessage.value = e.message
        }
    }

    suspend fun getDetailStory(id: String, token: String) {
        _isStoryLoading.value = true
        try {
            val result = apiService.getDetailStories(id, "Bearer $token")
            _storyItem.value = result.story
            _getDetailStoryMessage.value = result.message
            _isStoryLoading.value = false
        } catch (e: HttpException) {
            val json = e.response()?.errorBody()?.string()
            val result = Gson().fromJson(json, ErrorResponse::class.java)
            _getDetailStoryMessage.value = result.message
            _isStoryLoading.value = false
        } catch (e: SocketTimeoutException) {
            _getDetailStoryMessage.value = e.message
            _isStoryLoading.value = false
        } catch (e: IOException) {
            _getDetailStoryMessage.value = "Tidak dapat memuat: ${e.message}"
            _isStoryLoading.value = false
        }
    }

    suspend fun addStory(
        file: MultipartBody.Part,
        desc: RequestBody,
        token: String,
        lat: RequestBody?,
        lon: RequestBody?
    ) {
        _isStoryLoading.value = true
        try {
            val result = apiService.addStory(file, desc, lat, lon, "Bearer $token")
            _addStorySuccess.value = true
            _addStoryMessage.value = result.message
            _isStoryLoading.value = false
        } catch (e: IOException) {
            _addStoryMessage.value = e.message
            _isStoryLoading.value = false
        } catch (e: HttpException) {
            val json = e.response()?.errorBody()?.string()
            val result = Gson().fromJson(json, ErrorResponse::class.java)
            _addStoryMessage.value = result.message
            _isStoryLoading.value = false
        } catch (e: SocketTimeoutException) {
            _addStorySuccess.value = true
            _addStoryMessage.value = e.message
            _isStoryLoading.value = false
        }
    }

    fun clearStoryMessage() {
        _getStoryMessage.value = null
        _getDetailStoryMessage.value = null
        _addStoryMessage.value = null
    }

    fun clearAddStatus() {
        _addStorySuccess.value = null
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