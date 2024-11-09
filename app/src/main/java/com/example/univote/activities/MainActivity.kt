package com.example.univote.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.univote.R
import com.example.univote.databinding.ActivityMainBinding
import com.example.univote.models.Poll
import com.example.univote.models.ProtectedDataResponse
import retrofit2.Call
import retrofit2.Callback
import com.example.univote.network.ApiService
import com.example.univote.network.RetrofitClient
import com.example.univote.utils.TokenManager
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if token exists to manage user session
        val token = TokenManager.getToken()
        if(token == null){
            navigateToLoginScreen()
        } else {
            setupApiService(token)
            loadAndAccessProtectedResource()
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun accessProtectedResource(pollId: String) {
        apiService.getPollResults(pollId).enqueue(object : Callback<ProtectedDataResponse>{
            override fun onResponse(
                call: Call<ProtectedDataResponse>,
                response: Response<ProtectedDataResponse>
            ) {
                if (response.isSuccessful) {
                    // Handle successful response
                    val data = response.body()
                } else if (response.code() == 401) {
                    // Handle unauthorized (expired/invalid token)
                    TokenManager.clearToken()
                    navigateToLoginScreen()
                }
            }

            override fun onFailure(call: Call<ProtectedDataResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadAndAccessProtectedResource() {
        apiService.getActivePolls().enqueue(object : Callback<List<Poll>> {
            override fun onResponse(call: Call<List<Poll>>, response: Response<List<Poll>>) {
                if (response.isSuccessful && response.body() != null) {
                    val polls = response.body()!!
                    if (polls.isNotEmpty()) {
                        val pollId = polls[0].id
                        accessProtectedResource(pollId)
                    }
                }
            }

            override fun onFailure(call: Call<List<Poll>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to load polls", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun setupApiService(token: String) {
        apiService = RetrofitClient.createService(ApiService::class.java)
        RetrofitClient.addTokenInterceptor(token)
    }

    private fun navigateToLoginScreen(){
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun logout() {
        TokenManager.clearToken()
        navigateToLoginScreen()
    }

}