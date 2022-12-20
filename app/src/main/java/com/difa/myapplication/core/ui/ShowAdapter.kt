package com.difa.myapplication.core.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.difa.myapplication.R
import com.difa.myapplication.core.domain.model.ShowModel
import com.difa.myapplication.core.utils.*
import com.difa.myapplication.databinding.ItemListBinding
import com.difa.myapplication.showall.AllMoviesActivity
import com.difa.myapplication.showall.AllTvSeriesActivity

class ShowAdapter(private val onClick: (ShowModel) -> Unit) :
    RecyclerView.Adapter<ShowAdapter.MovieViewHolder>() {

    private val list = ArrayList<ShowModel>()

    fun setList(arrayList: List<ShowModel>?, addDummy: Boolean) {
        if (arrayList == null) {
            return
        }
        list.clear()
        list.addAll(arrayList)
        if(addDummy && list.size == 7){
            list.add(DataMapper.dummyData())
        }
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
                    val category = list[0].category

                    when(item.id){
                        "view_all" ->{
                            if (list[0].showType == MOVIE) {
                                val intent = Intent(itemView.context, AllMoviesActivity::class.java)
                                intent.putExtra(EXTRA_MOVIE, category)
                                itemView.context.startActivity(intent)
                            }else{
                                val intent = Intent(itemView.context, AllTvSeriesActivity::class.java)
                                intent.putExtra(EXTRA_TV, category)
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