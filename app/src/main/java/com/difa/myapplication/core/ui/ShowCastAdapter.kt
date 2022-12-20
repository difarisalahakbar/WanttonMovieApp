package com.difa.myapplication.core.ui

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.difa.myapplication.R
import com.difa.myapplication.core.domain.model.ShowModel
import com.difa.myapplication.core.utils.*
import com.difa.myapplication.databinding.ItemListBinding
import com.difa.myapplication.showall.AllMoviesActivity
import com.difa.myapplication.showall.AllTvSeriesActivity

class ShowCastAdapter(private val onClick: (ShowModel) -> Unit ) :
    RecyclerView.Adapter<ShowCastAdapter.MovieViewHolder>() {

    private val list = ArrayList<ShowModel>()

    private val fullList = ArrayList<ShowModel>()

    fun setFullList(arrayList: List<ShowModel>?) {
        if (arrayList == null) {
            return
        }
        fullList.clear()
        fullList.addAll(arrayList)

        if(fullList.size > 7){
            list.add(DataMapper.dummyData())
        }

    }

    fun setList(arrayList: List<ShowModel>?) {
        if (arrayList == null) {
            return
        }
        list.clear()
        list.addAll(arrayList)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class MovieViewHolder(private val binding: ItemListBinding) : ViewHolder(binding.root) {
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

                if (item.id == "view_all") {
                    imgMovie.setImageDrawable(itemView.context.getDrawable(R.drawable.view_all))
                    dot.visibility = View.GONE
                    star.visibility = View.GONE
                    tvYear.visibility = View.GONE

                }

                if (item.posterPath != "" && item.posterPath != null) {
                    Glide.with(itemView.context)
                        .load(URL_IMAGE + item.posterPath)
                        .into(imgMovie)
                }


                itemView.setOnClickListener {

                    when(item.id){
                        "view_all" ->{
                            if (list[0].showType == MOVIE) {
                                val intent = Intent(itemView.context, AllMoviesActivity::class.java)
                                 intent.putExtra(EXTRA_CAST_MOVIE, true)
                                intent.putExtra(EXTRA_MOVIE, fullList)
                                itemView.context.startActivity(intent)
                            }else{
                                val intent = Intent(itemView.context, AllTvSeriesActivity::class.java)
                                intent.putExtra(EXTRA_CAST_MOVIE, true)
                                intent.putExtra(EXTRA_MOVIE, fullList)
                                itemView.context.startActivity(intent)
                            }
                        }
                        else -> onClick.invoke(item)
                    }

                }
            }
        }
    }

}