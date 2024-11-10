package com.example.univote.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.univote.R
import com.example.univote.databinding.ActivityCreatePollBinding
import com.example.univote.models.CreatePollRequest
import com.example.univote.network.ApiService
import com.example.univote.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreatePollActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePollBinding
    private val apiService = RetrofitClient.createService(ApiService::class.java)
    private val options = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePollBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addOptionButton.setOnClickListener {
            val optionText = binding.optionEditText.text.toString().trim()
            if(optionText.isNotEmpty()){
                options.add(optionText)
                binding.optionEditText.text?.clear()
                updateOptionsList()
            }
        }

        binding.createPollButton.setOnClickListener {
            val title = binding.titleEditText.text.toString().trim()
            if(title.isEmpty() || options.isEmpty()){
                Toast.makeText(this, "Please enter a title and at least one option", Toast.LENGTH_SHORT).show()
            } else {
                createPoll(title, options)
            }
        }
    }

    private fun createPoll(title: String, options: MutableList<String>) {
        val pollRequest = CreatePollRequest(title, options)

        apiService.createPoll(pollRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    TODO("Check whether this is actually creating a poll in DB using apiService")
                    Toast.makeText(this@CreatePollActivity, "Poll created successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@CreatePollActivity, "Failed to create poll", Toast.LENGTH_SHORT).show()
                    Log.d("CreatePollActivity", "Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@CreatePollActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("CreatePollActivity", "Error: ${t.message}")
            }

        })
    }

    private fun updateOptionsList() {
        binding.optionEditText.setText(options.joinToString(separator = "\n") { "- $it" })
    }
}