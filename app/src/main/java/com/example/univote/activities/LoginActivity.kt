package com.example.univote.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.jwt.JWT
import com.example.univote.databinding.ActivityLoginBinding
import com.example.univote.network.ApiService
import com.example.univote.network.LoginRequest
import com.example.univote.network.RetrofitClient
import com.example.univote.utils.TokenManager
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
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if(email.isNotEmpty() && password.isNotEmpty()){
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
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
                    // Redirect based on user role
                    if (isAdmin(response.body()!!.token)) {
                        startActivity(Intent(this@LoginActivity, AdminDashboardActivity::class.java))
                    } else {
                        startActivity(Intent(this@LoginActivity, PollsActivity::class.java))
                    }
                    finish()
                } else {
                    // Show error message
                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun isAdmin(token: String): Boolean {
        try {
            // Decode the JWT token
            val jwt = JWT(token)
            // Check for the "is_admin" claim
            val isAdminClaim = jwt.getClaim("is_admin").asBoolean()
            return isAdminClaim == true
        } catch (e: Exception){
            e.printStackTrace()
            return false
        }
    }
}