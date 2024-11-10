package com.example.univote.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.univote.databinding.ItemPollBinding
import com.example.univote.models.Poll

class PollsAdapter(
    private val polls: List<Poll>,
    private val onPollClick: (Poll) -> Unit
): RecyclerView.Adapter<PollsAdapter.PollsViewHolder>() {

    inner class PollsViewHolder(
        private val binding: ItemPollBinding
    ): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(poll: Poll){
            binding.pollTitleTextView.text = poll.title
            binding.pollStatusTextView.text = if (poll.isActive) "Ongoing" else "Inactive"
            binding.root.setOnClickListener{
                onPollClick(poll)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PollsAdapter.PollsViewHolder {
        val binding = ItemPollBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PollsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PollsAdapter.PollsViewHolder, position: Int) {
        holder.bind(polls[position])
    }

    override fun getItemCount(): Int {
        return polls.size
    }

}