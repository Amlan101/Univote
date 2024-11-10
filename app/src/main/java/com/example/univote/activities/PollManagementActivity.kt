package com.example.univote.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.univote.R
import com.example.univote.adapters.PollManagementAdapter
import com.example.univote.databinding.ActivityPollManagementBinding
import com.example.univote.models.Poll
import com.example.univote.models.ProtectedDataResponse
import com.example.univote.network.ApiService
import com.example.univote.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PollManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPollManagementBinding
    private val apiService = RetrofitClient.createService(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPollManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pollsRecyclerView.layoutManager = LinearLayoutManager(this)
        loadPolls()
    }

    private fun loadPolls() {
        apiService.getActivePolls().enqueue(object : Callback<List<Poll>> {
            override fun onResponse(call: Call<List<Poll>>, response: Response<List<Poll>>) {
                if (response.isSuccessful) {
                    val polls = response.body() ?: emptyList()
                    binding.pollsRecyclerView.adapter = PollManagementAdapter(polls, ::onPollAction)
                } else {
                    Toast.makeText(this@PollManagementActivity, "Failed to load polls", Toast.LENGTH_SHORT).show()
                    Log.d("PollManagementActivity", "Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Poll>>, t: Throwable) {
                Toast.makeText(this@PollManagementActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun onPollAction(action: String, pollId: String) {
        when (action) {
            "deactivate" -> deactivatePoll(pollId)
            "delete" -> deletePoll(pollId)
            "results" -> viewResults(pollId)
        }
    }

    private fun deletePoll(pollId: String) {
        apiService.deletePoll(pollId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PollManagementActivity, "Poll deleted", Toast.LENGTH_SHORT).show()
                    loadPolls() // Refresh the list
                } else {
                    Toast.makeText(this@PollManagementActivity, "Failed to delete poll", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@PollManagementActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deactivatePoll(pollId: String) {
        apiService.deactivatePoll(pollId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PollManagementActivity, "Poll deactivated", Toast.LENGTH_SHORT).show()
                    loadPolls() // Refresh the list
                } else {
                    Toast.makeText(this@PollManagementActivity, "Failed to deactivate poll", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@PollManagementActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun viewResults(pollId: String) {
        apiService.getPollResults(pollId).enqueue(object : Callback<ProtectedDataResponse> {
            override fun onResponse(call: Call<ProtectedDataResponse>, response: Response<ProtectedDataResponse>) {
                if (response.isSuccessful) {
                    val results = response.body()
                    results?.let {
                        val intent = Intent(this@PollManagementActivity, ResultsActivity::class.java)
                        intent.putExtra("pollResults", results)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@PollManagementActivity, "Failed to load results", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProtectedDataResponse>, t: Throwable) {
                Toast.makeText(this@PollManagementActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}