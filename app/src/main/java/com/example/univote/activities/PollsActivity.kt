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
import com.example.univote.adapters.PollsAdapter
import com.example.univote.databinding.ActivityPollsBinding
import com.example.univote.databinding.ActivityRegisterBinding
import com.example.univote.models.Poll
import com.example.univote.network.ApiService
import com.example.univote.network.RetrofitClient
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
        loadActivePolls()
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
}