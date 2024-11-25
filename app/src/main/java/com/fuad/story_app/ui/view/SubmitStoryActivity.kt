package com.fuad.story_app.ui.view

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fuad.story_app.R
import com.fuad.story_app.databinding.ActivitySubmitStoryBinding
import com.fuad.story_app.ui.viewmodel.StoryViewModel
import com.fuad.story_app.ui.viewmodel.UserViewModel
import com.fuad.story_app.ui.viewmodel.ViewModelFactory
import com.fuad.story_app.utils.getImageUri
import com.fuad.story_app.utils.reduceFileImage
import com.fuad.story_app.utils.uriToFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class SubmitStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubmitStoryBinding
    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(this) }
    private val viewModel: StoryViewModel by viewModels { factory }
    private val userViewModel: UserViewModel by viewModels { factory }
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySubmitStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.addStorySuccess.observe(this) { isSuccess ->
            CoroutineScope(Dispatchers.Main).launch {
                if (isSuccess == true) {
                    viewModel.clearAddStatus()
                    delay(1000)
                    finish()
                }
            }

        }

        viewModel.isStoryLoading.observe(this) {
            if (it != null) {
                showLoading(it)
            }
        }

        viewModel.addStoryMessage.observe(this) { message ->
            if (message != null) {
                showToast(message)
                viewModel.clearMessageStory()
            }
        }

        setListener()
        showImage()
    }

    private fun setListener() {
        binding.btnGaleri.setOnClickListener {
            launcherIntentGalery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnKamera.setOnClickListener {
            uri = getImageUri(this)
            launcherIntentCamera.launch(uri!!)
        }

        binding.buttonAdd.setOnClickListener {
            var valid = true
            val desc = binding.edAddDescription.text.toString().trim()

            if (desc.isEmpty()) {
                valid = false
                binding.edAddDescription.error =
                    getString(R.string.masukan_deskripsi_terlebih_dahulu)
            }

            if (viewModel.currentUri == null) {
                valid = false
                showToast(getString(R.string.tidak_ada_gambar_yang_dipilih))
            }

            if (valid) {
                viewModel.currentUri?.let { uri ->
                    val file = uriToFile(uri, this).reduceFileImage()
                    val description = binding.edAddDescription.text.toString()

                    val body = description.toRequestBody("text/plain".toMediaType())
                    val imageFile = file.asRequestBody("image/jpeg".toMediaType())

                    val multipartBody = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        imageFile
                    )

                    userViewModel.getTokenUser.observe(this) { token ->
                        if (token != null) {
                            viewModel.addStory(multipartBody, body, token)
                        }
                    }
                }
            }
        }
    }

    private fun showImage() {
        binding.pvPhoto.setImageURI(viewModel.currentUri)
    }

    private val launcherIntentGalery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.currentUri = uri
            showImage()
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            viewModel.currentUri = uri
            showImage()
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}