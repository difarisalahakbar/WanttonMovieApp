package com.difa.myapplication.core.ui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.difa.myapplication.core.domain.model.ShowModel
import com.difa.myapplication.core.utils.*
import com.difa.myapplication.databinding.ItemListSearchBinding


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
        holder.bind(list[position])
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

