package com.fuad.story_app.ui.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.fuad.story_app.R
import com.fuad.story_app.databinding.ActivityDetailBinding
import com.fuad.story_app.ui.viewmodel.UserViewModel
import com.fuad.story_app.ui.viewmodel.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(this) }
    private val viewModel: UserViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getStringExtra(ID_STORY)
        id?.let {
            Log.d("DETAIL", "$id")
            viewModel.getDetailStory(id)

            viewModel.getDetailResult.observe(this) { results ->
                Log.d("DETAIL", results.photoUrl)
                Glide.with(this)
                    .load(results.photoUrl)
                    .placeholder(R.drawable.ic_baseline_image_search_24)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(binding.ivDetailPhoto)

                binding.tvDetailName.text = results.name
                binding.tvDetailDescription.text = results.description
            }
        } ?: run {
            viewModel.getDetailMessage.observe(this) {
                showToast(it)
            }
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val ID_STORY = "id_story"
    }
}