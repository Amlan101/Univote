package com.example.univote.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.univote.R
import com.example.univote.databinding.ActivityMainBinding
import com.example.univote.databinding.ActivityRegisterBinding
import com.example.univote.utils.TokenManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if token exists to manage user session
        if(TokenManager.getToken() == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            // Clear the token
            TokenManager.clearToken()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}