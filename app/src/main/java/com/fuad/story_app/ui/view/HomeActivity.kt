package com.fuad.story_app.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fuad.story_app.R
import com.fuad.story_app.databinding.ActivityMainBinding
import com.fuad.story_app.ui.adapter.StoryAdapter
import com.fuad.story_app.ui.viewmodel.LoginViewModel
import com.fuad.story_app.ui.viewmodel.StoryViewModel
import com.fuad.story_app.ui.viewmodel.UserViewModel
import com.fuad.story_app.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(this) }
    private val storyViewModel: StoryViewModel by viewModels { factory }
    private val userViewModel: UserViewModel by viewModels { factory }
    private val loginViewModel: LoginViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()

        userViewModel.getLoginSession.observe(this) { session ->
            if (session.token.isNotEmpty()) {
                showUi()
            } else {
                moveToLogin()
            }
            session.userId
        }

        storyViewModel.isStoryLoading.observe(this) {
            if (it != null) {
                showLoading(it)
            }
        }

        storyViewModel.getAllStoryMessage.observe(this) {
            if (it != null) {
                showToast(it)
            }
        }
    }


    private fun setListener() {
        binding.rvItem.layoutManager = LinearLayoutManager(this)
        binding.btnFloating.setOnClickListener {
            startActivity(Intent(this, SubmitStoryActivity::class.java))
        }
    }

    private fun showUi() {
        userViewModel.getTokenUser.observe(this) { token ->
            if (token.isNotEmpty()) {
                storyViewModel.getStory(token).observe(this) { list ->
                    val adapter = StoryAdapter {
                        val id = it.id
                        val intent = Intent(this, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.ID_STORY, id)
                        startActivity(intent)
                    }
                    adapter.submitData(lifecycle, list)
                    binding.rvItem.adapter = adapter
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        userViewModel.getTokenUser.observe(this) { token ->
            if (token.isNotEmpty()) {
                storyViewModel.getStory(token).observe(this) { list ->
                    val adapter = StoryAdapter {
                        val id = it.id
                        val intent = Intent(this, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.ID_STORY, id)
                        startActivity(intent)
                    }
                    adapter.submitData(lifecycle, list)
                    binding.rvItem.adapter = adapter
                }
            }
        }
    }

    private fun moveToLogin() {
        Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
        }
        lifecycleScope.launch {
            userViewModel.removeLoginSession()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                userViewModel.removeLoginSession()
                loginViewModel.clearLoginResult()
                true
            }

            R.id.action_settings -> {
                startActivity(Intent(this, SettingActivity::class.java))
                true
            }

            R.id.action_map -> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}