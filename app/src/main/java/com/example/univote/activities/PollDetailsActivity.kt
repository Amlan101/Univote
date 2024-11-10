package com.example.univote.activities


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Callback
import retrofit2.Response
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.univote.adapters.OptionsAdapter
import retrofit2.Call
import com.example.univote.databinding.ActivityPollDetailsBinding
import com.example.univote.models.PollDetailsResponse
import com.example.univote.network.ApiService
import com.example.univote.network.RetrofitClient

class PollDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPollDetailsBinding
    private val apiService = RetrofitClient.createService(ApiService::class.java)
    private lateinit var pollId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPollDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pollId = intent.getStringExtra("poll_id") ?: return
        binding.optionsRecyclerView.layoutManager = LinearLayoutManager(this)
        loadPollDetails()

    }

    private fun loadPollDetails() {
        apiService.getPollDetails(pollId).enqueue(object: Callback<PollDetailsResponse>{
            override fun onResponse(
                call: Call<PollDetailsResponse>,
                response: Response<PollDetailsResponse>
            ) {
                if(response.isSuccessful){
                    val pollDetails = response.body()
                    pollDetails?.let {
                        binding.pollTitleTextView.text = it.title
                        binding.optionsRecyclerView.adapter =
                            OptionsAdapter(it.options){ selectedOption ->
                                submitVote(selectedOption.id)
                            }
                    }
                } else {
                    Toast.makeText(this@PollDetailsActivity, "Failed to load poll details", Toast.LENGTH_SHORT).show()
                    Log.d("Poll Details Error", "Failed to load poll details: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PollDetailsResponse>, t: Throwable) {
                Toast.makeText(this@PollDetailsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("Poll Details Error", "Failed to load poll details: ${t.message}")
            }

        })
    }

    private fun submitVote(optionId: String) {
        apiService.castVote(pollId, optionId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PollDetailsActivity, "Vote cast successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@PollDetailsActivity, "Failed to cast vote", Toast.LENGTH_SHORT).show()
                    Log.d("Vote Error", "Failed to cast vote: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@PollDetailsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("Vote Error", "Failed to cast vote: ${t.message}")
            }
        })
    }
}