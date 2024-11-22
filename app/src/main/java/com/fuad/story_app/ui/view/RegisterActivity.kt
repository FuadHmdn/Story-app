package com.fuad.story_app.ui.view

import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.fuad.story_app.R
import com.fuad.story_app.databinding.ActivityRegisterBinding
import com.fuad.story_app.ui.viewmodel.UserViewModel
import com.fuad.story_app.ui.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(this) }
    private val viewModel: UserViewModel by viewModels { factory }
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setListener()
    }

    private fun setListener() {
        binding.edRegisterEmail.addTextChangedListener { editable ->
            val email = editable.toString().trim()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edRegisterEmail.error = "Masukan Email yang valid!"
            } else {
                binding.edRegisterEmail.error = null
            }
        }

        binding.btnRegister.setOnClickListener {
            val nama = binding.edRegisterName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString().trim()

            var valid = true

            if (nama.isEmpty()) {
                binding.edRegisterName.error = "Masukan nama terlebih dahulu!"
                valid = false
            }

            if (email.isEmpty()) {
                binding.edRegisterEmail.error = "Masukan email terlebih dahulu!"
                valid = false
            }

            if (password.isEmpty()) {
                binding.edRegisterPassword.error = "Masukan password terlebih dahulu!"
                valid = false
            }

            if (valid) {
                viewModel.register(nama, email, password)
            }
        }
    }
}