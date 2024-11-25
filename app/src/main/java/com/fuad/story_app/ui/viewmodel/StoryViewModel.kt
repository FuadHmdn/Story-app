package com.fuad.story_app.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fuad.story_app.data.repository.StoryRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    var currentUri: Uri? = null
    val listStoryItem get() = storyRepository.listStoryItem
    val getAllStoryMessage get() = storyRepository.getStoryMessage

    val storyItem get() = storyRepository.storyItem
    val getDetailStoryMessage get() = storyRepository.getDetailStoryMessage

    val addStoryMessage get() = storyRepository.addStoryMessage
    val addStorySuccess get() = storyRepository.addStorySuccess

    val isStoryLoading get() = storyRepository.isStoryLoading

    fun getAllStory(token: String){
        viewModelScope.launch {
            storyRepository.getAllStory(token)
        }
    }

    fun getDetailStory(id: String, token: String) {
        viewModelScope.launch {
            storyRepository.getDetailStory(id, token)
        }
    }

    fun addStory(file: MultipartBody.Part, desc: RequestBody, token: String){
        viewModelScope.launch {
            storyRepository.addStory(file, desc, token)
        }
    }

    fun clearMessageStory(){
        storyRepository.clearStoryMessage()
    }

    fun clearAddStatus(){
        storyRepository.clearAddStatus()
    }
}