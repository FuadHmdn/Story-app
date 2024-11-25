package com.fuad.story_app.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fuad.story_app.data.repository.LoginRepository
import com.fuad.story_app.data.repository.StoryRepository
import com.fuad.story_app.data.repository.UserRepository
import com.fuad.story_app.di.Injection

class ViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository,
    private val loginRepository: LoginRepository,
    )
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(loginRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object{

        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory{
            return INSTANCE?: synchronized(this){
                INSTANCE?: ViewModelFactory(
                    Injection.provideUserRepository(context),
                    Injection.provideStoryRepository(),
                    Injection.provideLoginRepository()
                )
            }.also { INSTANCE = it }
        }
    }

}