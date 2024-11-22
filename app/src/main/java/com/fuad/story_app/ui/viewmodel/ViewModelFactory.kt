package com.fuad.story_app.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fuad.story_app.data.repository.UserRepository
import com.fuad.story_app.di.Injection

class ViewModelFactory private constructor(private val userRepository: UserRepository)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(userRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object{

        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory{
            return INSTANCE?: synchronized(this){
                INSTANCE?: ViewModelFactory(Injection.provideUserRepository(context))
            }.also { INSTANCE = it }
        }
    }

}