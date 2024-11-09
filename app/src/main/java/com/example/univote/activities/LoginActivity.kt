package com.example.univote.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.univote.R
import com.example.univote.databinding.ActivityLoginBinding
import com.example.univote.network.ApiService
import com.example.univote.network.LoginRequest
import com.example.univote.network.RetrofitClient
import com.example.univote.utils.TokenManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val apiService by lazy { RetrofitClient.createService(ApiService::class.java) }
    private val tokenManager = TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                startActivity(Intent(this, MainActivity::class.java))
                loginUser(email, password)
            } else {
                Snackbar.make(binding.btnLogin, "Please fill all the fields", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.registerBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Function to handle login functionality
     */
    private fun loginUser(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.login(LoginRequest(email, password))
            withContext(Dispatchers.Main){
                if(response.isSuccessful && response.body()!= null){
                    // Store token using Token Manager
                    tokenManager.saveToken(response.body()!!.token)
                    // Navigate to main activity
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    // Show error message
                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}