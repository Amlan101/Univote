package com.example.univote.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.univote.adapters.PollsAdapter
import com.example.univote.databinding.ActivityPollsBinding
import com.example.univote.models.Poll
import com.example.univote.models.ProtectedDataResponse
import com.example.univote.network.ApiService
import com.example.univote.network.RetrofitClient
import com.example.univote.utils.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PollsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPollsBinding
    private val apiService = RetrofitClient.createService(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPollsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pollRecyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        loadAndAccessProtectedResource()
        loadActivePolls()
        binding.btnLogout.setOnClickListener {
            navigateToLoginScreen()
        }
    }

    private fun loadAndAccessProtectedResource() {
        apiService.getActivePolls().enqueue(object : Callback<List<Poll>> {
            override fun onResponse(call: Call<List<Poll>>, response: Response<List<Poll>>) {
                if (response.isSuccessful) {
                    val polls = response.body() ?: emptyList()
                    binding.pollRecyclerView.adapter = PollsAdapter(polls) { poll ->
                        val intent = Intent(this@PollsActivity, PollDetailsActivity::class.java)
                        intent.putExtra("poll_id", poll.id)
                        startActivity(intent)
                    }
                    if (polls.isNotEmpty()) {
                        accessProtectedResource(polls[0].id) // Access the first poll's resource as an example
                    }
                } else {
                    Toast.makeText(this@PollsActivity, "Failed to load polls", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Poll>>, t: Throwable) {
                Toast.makeText(this@PollsActivity, "Failed to load polls: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("PollsActivity", "Error loading polls", t)
            }
        })
    }

    // Function to access a protected resource using a poll ID
    private fun accessProtectedResource(pollId: String) {
        apiService.getPollResults(pollId).enqueue(object : Callback<ProtectedDataResponse> {
            override fun onResponse(call: Call<ProtectedDataResponse>, response: Response<ProtectedDataResponse>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    val data = response.body()
                    Log.d("PollsActivity", "Protected resource accessed: $data")
                } else if (response.code() == 401) {
                    // Handle unauthorized (expired/invalid token)
                    TokenManager.clearToken()
                    navigateToLoginScreen()
                }
            }

            override fun onFailure(call: Call<ProtectedDataResponse>, t: Throwable) {
                Toast.makeText(this@PollsActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                Log.e("PollsActivity", "Error accessing protected resource", t)
            }
        })
    }

    private fun loadActivePolls(){
        apiService.getActivePolls().enqueue(object : Callback<List<Poll>>{
            override fun onResponse(call: Call<List<Poll>>, response: Response<List<Poll>>) {
                if(response.isSuccessful) {
                    val polls = response.body() ?: emptyList()
                    binding.pollRecyclerView.adapter = PollsAdapter(polls) { poll ->
                        // Navigate to PollDetailsActivity
                        val intent = Intent(this@PollsActivity, PollDetailsActivity::class.java)
                        intent.putExtra("poll_id", poll.id)
                        startActivity(intent)
                    }
                }else {
                    Toast.makeText(this@PollsActivity, "Failed to load polls", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Poll>>, t: Throwable) {
                Toast.makeText(this@PollsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("PollsActivity", "Error loading polls: ${t.message}")
            }

        })
    }

    private fun navigateToLoginScreen() {
        TokenManager.logoutAndNavigateToLogin(this)
    }
}