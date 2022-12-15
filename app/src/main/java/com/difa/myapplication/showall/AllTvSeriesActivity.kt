package com.difa.myapplication.showall

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.difa.myapplication.R
import com.difa.myapplication.core.data.Resource
import com.difa.myapplication.core.domain.model.ShowModel
import com.difa.myapplication.core.ui.ShowAllAdapter
import com.difa.myapplication.core.utils.*
import com.difa.myapplication.databinding.ActivityAllMoviesBinding
import com.difa.myapplication.detail.DetailActivity
import com.difa.myapplication.item.TvSeriesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllTvSeriesActivity : AppCompatActivity() {

    private val tvSeriesViewModel: TvSeriesViewModel by viewModels()

    private lateinit var binding: ActivityAllMoviesBinding

    private lateinit var adapter: ShowAllAdapter

    private var currentPage = 1
    private var maxPage = 6
    private val limit = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllMoviesBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.title = ""

        binding.tvTitle.text = getString(R.string.tv_series)

        binding.btnBack.setOnClickListener {
            finish()
        }

        setupRecyclerView()
        setupAllTvSeries()

    }

    private fun setupRecyclerView() {
        adapter = ShowAllAdapter{
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, it)
            startActivity(intent)
        }

        with(binding){
            rvAll.adapter = adapter
            rvAll.layoutManager = GridLayoutManager(this@AllTvSeriesActivity, 2)
        }
    }

    private fun setupAllTvSeries() {
        tvSeriesViewModel.setPage(currentPage)

        with(binding){
            nestedScrollMovie.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
                    val height = (v.getChildAt(0)?.measuredHeight ?: 0) - v.measuredHeight
                    if (scrollY == height) {
                        if (currentPage < maxPage) {
                            tvSeriesViewModel.setPage(++currentPage)
                         } else {
                            Toast.makeText(this@AllTvSeriesActivity, "Max Page", Toast.LENGTH_LONG).show()
                        }
                    }
                })
        }

        when(intent.getIntExtra(EXTRA_TV, 0)){
            POPULAR -> {
                binding.tvNowPlaying.text = getString(R.string.popular)
                tvSeriesViewModel.getAllTv(POPULAR, limit, this@AllTvSeriesActivity).observe(this) { tvList ->
                    when (tvList) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE

                            val data = tvList.data as ArrayList<ShowModel>
                            if (data.isNotEmpty()) {
                                adapter.setList(data)
                            }
                        }

                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@AllTvSeriesActivity, "${tvList.message}", Toast.LENGTH_LONG).show()

                        }
                    }
                }
            }
            TOP_RATED -> {
                binding.tvNowPlaying.text = getString(R.string.top_rated)
                tvSeriesViewModel.getAllTv(TOP_RATED, limit, this@AllTvSeriesActivity).observe(this) { tvList ->
                    when (tvList) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val data = tvList.data as ArrayList<ShowModel>
                            if (data.isNotEmpty()) {
                                adapter.setList(data)
                            }
                        }

                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@AllTvSeriesActivity, "${tvList.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            else ->{
                tvSeriesViewModel.getAllTv(NOW_PLAYING, limit, this@AllTvSeriesActivity).observe(this) { tvList ->
                    binding.tvNowPlaying.text = getString(R.string.on_the_air)
                    when (tvList) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            adapter.setList(tvList.data)
                        }

                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@AllTvSeriesActivity, "${tvList.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}