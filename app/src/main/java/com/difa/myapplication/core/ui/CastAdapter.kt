package com.difa.myapplication.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.difa.myapplication.core.domain.model.CastModel
import com.difa.myapplication.core.utils.URL_IMAGE
import com.difa.myapplication.databinding.ItemListCastBinding

class CastAdapter : RecyclerView.Adapter<CastAdapter.MovieViewHolder>() {

    private val list = ArrayList<CastModel>()

    fun setList(arrayList: List<CastModel>?) {
        if (arrayList == null) {
            return
        }
        list.clear()
        list.addAll(arrayList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemListCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(list.get(position))
    }

    override fun getItemCount(): Int = list.size

    inner class MovieViewHolder(private val binding: ItemListCastBinding) : ViewHolder(binding.root) {
        fun bind(item: CastModel) {
            with(binding) {
                if(item.profilePath != null){
                    Glide.with(itemView.context)
                        .load(URL_IMAGE + item.profilePath)
                        .into(imgCast)
                }
                tvName.text = item.name
                tvCharacter.text = item.character
            }
        }
     }

}