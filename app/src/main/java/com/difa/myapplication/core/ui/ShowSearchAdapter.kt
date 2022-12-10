package com.difa.myapplication.core.ui

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.difa.myapplication.R
import com.difa.myapplication.core.domain.model.ShowModel
import com.difa.myapplication.core.utils.*
import com.difa.myapplication.databinding.ItemListBinding
import com.difa.myapplication.databinding.ItemListSearchBinding
import com.difa.myapplication.showall.AllMoviesActivity
import com.difa.myapplication.showall.AllTvSeriesActivity
import com.squareup.picasso.Picasso

class ShowSearchAdapter(private val onClick: (ShowModel) -> Unit) :
    RecyclerView.Adapter<ShowSearchAdapter.MovieViewHolder>() {

    private val list = ArrayList<ShowModel>()

    fun setList(arrayList: List<ShowModel>?) {
        if (arrayList == null) {
            return
        }
        list.clear()
        list.addAll(arrayList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            ItemListSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(list.get(position))
    }

    override fun getItemCount(): Int = list.size

    inner class MovieViewHolder(private val binding: ItemListSearchBinding) :
        ViewHolder(binding.root) {
        fun bind(item: ShowModel) {
            with(binding) {
                tvTitle.text = item.title
                tvRate.text = item.voteAverage
                val year = item.releaseDate?.split("-")

                if (year != null) {
                    if (year[0] != "") {
                        tvYear.text = year[0]
                    } else {
                        tvYear.text = "-"
                    }
                }

                if (item.posterPath != "" && item.posterPath != null) {
                    Glide.with(itemView.context)
                        .load(URL_IMAGE + item.posterPath)
                        .into(imgMovie)
                }

                itemView.setOnClickListener {
                    onClick.invoke(item)
                }
            }
        }
    }
}

