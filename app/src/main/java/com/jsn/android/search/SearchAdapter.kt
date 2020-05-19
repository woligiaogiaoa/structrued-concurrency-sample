package com.jsn.android.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jsn.android.databinding.ItemSearchResultBinding

val callback=object :DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem===newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem.equals(newItem)
    }
}


class SearchAdapter : ListAdapter<String, SearchAdapter.SearchViewHolder>(callback) {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): SearchViewHolder {
        val inflate =
            ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class SearchViewHolder(val binding:ItemSearchResultBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(item:String){
            binding.tvItemResult.text=item
        }
    }
}