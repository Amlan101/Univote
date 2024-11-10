package com.example.univote.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.univote.databinding.ItemPollManagementBinding
import com.example.univote.models.Poll

class PollManagementAdapter(
    private val polls: List<Poll>,
    private val onPollAction: (action: String, pollId: String) -> Unit
): RecyclerView.Adapter<PollManagementAdapter.PollManagementViewHolder>() {
    inner class PollManagementViewHolder(
        private val binding: ItemPollManagementBinding
    ): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(poll: Poll) {
            binding.pollTitleTextView.text = poll.title
            binding.deactivateButton.setOnClickListener { onPollAction("deactivate", poll.id) }
            binding.resultsButton.setOnClickListener { onPollAction("results", poll.id) }
            binding.deleteButton.setOnClickListener { onPollAction("delete", poll.id) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PollManagementAdapter.PollManagementViewHolder {
        val binding = ItemPollManagementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PollManagementViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PollManagementAdapter.PollManagementViewHolder,
        position: Int
    ) {
        holder.bind(polls[position])
    }

    override fun getItemCount(): Int {
        return polls.size
    }
}