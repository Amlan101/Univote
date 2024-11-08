package com.example.univote.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.univote.R
import com.example.univote.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                // TODO: Add API call to authenticate and handle JWT
                startActivity(Intent(this, MainActivity::class.java))
                Snackbar.make(binding.btnLogin, "Login functionality to be implemented", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding.btnLogin, "Please fill all the fields", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.registerBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}