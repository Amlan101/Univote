package com.example.univote.activities

    import android.os.Bundle
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AppCompatActivity
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.example.univote.R
    import com.example.univote.adapters.PollResultAdapter
    import com.example.univote.databinding.ActivityResultsBinding
    import com.example.univote.models.ProtectedDataResponse
    import com.example.univote.network.ApiService
    import com.example.univote.network.RetrofitClient

    class ResultsActivity : AppCompatActivity() {

        private lateinit var binding: ActivityResultsBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityResultsBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.resultsRecyclerView.layoutManager = LinearLayoutManager(this)

            val pollResults = intent.getSerializableExtra("pollResults") as? ProtectedDataResponse
            pollResults?.let {
                binding.pollTitleTextView.text = it.title
                binding.resultsRecyclerView.adapter = PollResultAdapter(it.options)
            }
        }
    }