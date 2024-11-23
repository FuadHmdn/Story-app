package com.fuad.story_app.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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

        viewModel.registerMessage.observe(this) { message ->
            if (message != null) {
                showToast(message)
            }
        }
        viewModel.isRegisterSuccess.observe(this) { isError ->
            if (isError == false) {
                clearEditText()
                showDialog()
            }
        }
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        setListener()
    }

    override fun onStart() {
        super.onStart()
        viewModel.clearRegisterLogin()
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun clearEditText() {
        binding.edRegisterName.setText("")
        binding.edRegisterEmail.setText("")
        binding.edRegisterEmail.error = null
        binding.edRegisterPassword.setText("")
        binding.edRegisterPassword.error = null
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.berhasil_buat_akun))
            .setMessage(getString(R.string.silahkan_masuk_sekarang))
            .setPositiveButton(getString(R.string.login)) { _, _ ->
                val intent = Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }

                startActivity(intent)
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun setListener() {
        binding.edRegisterEmail.addTextChangedListener { editable ->
            val email = editable.toString().trim()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edRegisterEmail.error = getString(R.string.email_tidak_valid)
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
                binding.edRegisterName.error = getString(R.string.masukan_nama_terlebih_dahulu)
                valid = false
            }

            if (email.isEmpty()) {
                binding.edRegisterEmail.error = getString(R.string.masukan_email_terlebih_dahulu)
                valid = false
            }

            if (password.isEmpty()) {
                binding.edRegisterPassword.error =
                    getString(R.string.masukan_password_terlebih_dahulu)
                valid = false
            }

            if (valid) {
                viewModel.register(nama, email, password)
            }
        }
    }
}