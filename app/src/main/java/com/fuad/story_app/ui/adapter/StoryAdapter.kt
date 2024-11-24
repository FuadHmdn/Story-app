package com.fuad.story_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fuad.story_app.data.remote.response.ListStoryItem
import com.fuad.story_app.databinding.ListStoryBinding

class StoryAdapter(private val onClick: (ListStoryItem) -> Unit): ListAdapter<ListStoryItem, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(private val binding: ListStoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem, onClick: (ListStoryItem) -> Unit) {
            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)

            binding.tvItemName.text = story.name
            binding.tvItemDecription.text = story.description

            binding.root.setOnClickListener {
                onClick(story)
            }
        }

    }

    companion object{
        val DIFF_CALLBACK = object :  DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story, onClick)
    }
}