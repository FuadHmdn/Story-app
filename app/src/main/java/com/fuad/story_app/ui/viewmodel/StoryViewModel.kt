package com.fuad.story_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fuad.story_app.data.repository.StoryRepository
import kotlinx.coroutines.launch

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    val listStoryItem get() = storyRepository.listStoryItem
    val getAllStoryMessage get() = storyRepository.getStoryMessage

    fun getAllStory(token: String){
        viewModelScope.launch {
            storyRepository.getAllStory(token)
        }
    }
}