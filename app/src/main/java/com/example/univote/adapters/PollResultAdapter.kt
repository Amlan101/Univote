package com.example.univote.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.univote.databinding.ItemPollResultBinding
import com.example.univote.models.Option

class PollResultAdapter(
    private val options: List<Option>
): RecyclerView.Adapter<PollResultAdapter.PollResultViewHolder>() {
    inner class PollResultViewHolder(
        private val binding: ItemPollResultBinding
    ): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(option: Option) {
            binding.optionTextView.text = option.text
            binding.voteCountTextView.text = "Votes: ${option.votes}"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PollResultAdapter.PollResultViewHolder {
        val binding = ItemPollResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PollResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PollResultAdapter.PollResultViewHolder, position: Int) {
        holder.bind(options[position])
    }

    override fun getItemCount(): Int = options.size

}