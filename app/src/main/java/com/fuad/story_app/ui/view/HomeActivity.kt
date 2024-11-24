package com.fuad.story_app.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fuad.story_app.R
import com.fuad.story_app.databinding.ActivityMainBinding
import com.fuad.story_app.ui.adapter.StoryAdapter
import com.fuad.story_app.ui.viewmodel.UserViewModel
import com.fuad.story_app.ui.viewmodel.ViewModelFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(this) }
    private val viewModel: UserViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = StoryAdapter{ item ->
            val id: String = item.id
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.ID_STORY, id)
            startActivity(intent)
        }
        binding.rvItem.layoutManager = LinearLayoutManager(this@HomeActivity)

        viewModel.loginStatus.observe(this){ isLogin ->
            if (isLogin) {
                viewModel.getAllResult.observe(this@HomeActivity) { list ->
                    if (list.isNotEmpty()) {
                        adapter.submitList(list)
                        binding.rvItem.adapter = adapter
                    }
                }
                loadHomeData()
            } else {
                moveToLogin()
            }
        }
        viewModel.getLoginStatus()
    }

    override fun onResume() {
        super.onResume()
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModel.getAllStory()
        viewModel.isLoading.observe(this@HomeActivity) {
            showLoading(it)
        }
        viewModel.getAllMessage.observe(this@HomeActivity) { message ->
            if (message != null) {
                showToast(message)
                viewModel.clearMessage()
            }
        }
    }

    private fun moveToLogin() {
        val intent = Intent(this@HomeActivity, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                viewModel.removeSession()
                true
            }
            else -> {
                false
            }
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}