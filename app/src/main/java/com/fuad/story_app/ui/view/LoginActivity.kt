package com.fuad.story_app.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.fuad.story_app.R
import com.fuad.story_app.data.remote.response.LoginResult
import com.fuad.story_app.databinding.ActivityLoginBinding
import com.fuad.story_app.ui.viewmodel.UserViewModel
import com.fuad.story_app.ui.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(this) }
    private val viewModel: UserViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.loginResult.observe(this) { result ->
            result?.let {
                val session = LoginResult(result.name, result.userId, result.token)
                viewModel.saveSession(session)
            }
        }

        viewModel.isLoginSuccess.observe(this){ isError ->
            if (isError == false) {
                viewModel.saveLoginStatus(true)
                moveToHomeActivity()
            }
        }

        viewModel.loginMessage.observe(this){ message ->
            message?.let {
                showToast(it)
                viewModel.clearMessage()
            }
        }

        viewModel.isLoading.observe(this){ isLoading ->
            showLoading(isLoading)
        }

        setListener()
    }

    private fun moveToHomeActivity(){
        val intent = Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        viewModel.clearRegisterLogin()
        startActivity(intent)
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setListener() {
        binding.buatAkun.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.edLoginEmail.addTextChangedListener { editable ->
            val email = editable.toString().trim()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edLoginEmail.error = getString(R.string.email_tidak_valid)
            } else {
                binding.edLoginEmail.error = null
            }
        }

        binding.btnLogin.setOnClickListener {

            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            var valid = true
            if (email.isEmpty()) {
                valid = false
                binding.edLoginEmail.error = getString(R.string.masukan_email_terlebih_dahulu)
            }

            if (password.isEmpty()) {
                valid = false
                binding.edLoginPassword.error = getString(R.string.masukan_password_terlebih_dahulu)
            }

            if (valid) {
                viewModel.login(email, password)
            }
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}