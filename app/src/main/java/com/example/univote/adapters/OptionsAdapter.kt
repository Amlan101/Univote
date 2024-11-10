package com.example.univote.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.univote.databinding.ItemOptionBinding
import com.example.univote.models.Option

class OptionsAdapter(
    private val options: List<Option>,
    private val onOptionClick: (Option) -> Unit
): RecyclerView.Adapter<OptionsAdapter.OptionsViewHolder>() {

    inner class OptionsViewHolder(
        private val binding: ItemOptionBinding
    ): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(option: Option){
            binding.optionTextView.text = option.text
            binding.voteButton.setOnClickListener { onOptionClick(option) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OptionsAdapter.OptionsViewHolder {
        val binding = ItemOptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OptionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OptionsAdapter.OptionsViewHolder, position: Int) {
        holder.bind(options[position])
    }

    override fun getItemCount(): Int = options.size
}