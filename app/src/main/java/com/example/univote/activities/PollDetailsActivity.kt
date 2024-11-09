package com.example.univote.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.univote.R
import com.example.univote.databinding.ActivityPollDetailsBinding
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
        apiService.
    }
}