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
import com.difa.myapplication.core.data.Resource
import com.difa.myapplication.core.domain.model.ShowModel
import com.difa.myapplication.core.ui.ShowAllAdapter
import com.difa.myapplication.core.utils.*
import com.difa.myapplication.databinding.ActivityAllMoviesBinding
import com.difa.myapplication.detail.DetailActivity
import com.difa.myapplication.item.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllMoviesActivity : AppCompatActivity() {

    private val moviesViewModel: MoviesViewModel by viewModels()

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

        binding.btnBack.setOnClickListener {
            finish()
        }

        setupRecyclerView()
        setupAllMovies()

    }

    private fun setupRecyclerView() {
        adapter = ShowAllAdapter{
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, it)
            startActivity(intent)
        }

        with(binding) {
            rvAll.adapter = adapter
            rvAll.layoutManager = GridLayoutManager(this@AllMoviesActivity, 2)
        }
    }

    private fun setupAllMovies() {
        moviesViewModel.setPage(currentPage)

        with(binding) {
            nestedScrollMovie.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
                    val height = (v.getChildAt(0)?.measuredHeight ?: 0) - (v.measuredHeight ?: 0)
                    if (scrollY == height) {
                        if (currentPage < maxPage) {
                            moviesViewModel.setPage(++currentPage)
                        } else {
                            Toast.makeText(this@AllMoviesActivity, "Max Page", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                })
        }

        when (intent.getIntExtra(EXTRA_MOVIE, 0)) {
            POPULAR -> {
                binding.tvNowPlaying.text = "Popular"
                moviesViewModel.getAllMovie(POPULAR, limit).observe(this) { movieList ->
                    when (movieList) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE

                            val data = movieList.data as ArrayList<ShowModel>
                            if (!data.isNullOrEmpty()) {
                                adapter.setList(data)
                            }
                        }

                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@AllMoviesActivity, "${movieList.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            TOP_RATED -> {
                binding.tvNowPlaying.text = "Top Rated"
                moviesViewModel.getAllMovie(TOP_RATED, limit).observe(this) { movieList ->
                    when (movieList) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val data = movieList.data as ArrayList<ShowModel>
                            if (!data.isNullOrEmpty()) {
                                adapter.setList(data)
                            }
                        }

                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@AllMoviesActivity, "${movieList.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            else -> {
                moviesViewModel.getAllMovie(NOW_PLAYING, limit).observe(this) { movieList ->
                    when (movieList) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE

                            val data = movieList.data as ArrayList<ShowModel>
                            if (!data.isNullOrEmpty()) {
                                adapter.setList(data)
                            }
                        }

                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@AllMoviesActivity, "${movieList.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }


}