package com.difa.myapplication.showall

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.difa.myapplication.R
import com.difa.myapplication.core.base.BaseActivity
import com.difa.myapplication.core.domain.model.ShowModel
import com.difa.myapplication.core.ui.ShowAllAdapter
import com.difa.myapplication.core.utils.*
import com.difa.myapplication.databinding.ActivityAllMoviesBinding
import com.difa.myapplication.detail.DetailActivity
import com.difa.myapplication.item.TvSeriesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllTvSeriesActivity : BaseActivity<ActivityAllMoviesBinding>() {

    private val tvSeriesViewModel: TvSeriesViewModel by viewModels()

    private lateinit var adapter: ShowAllAdapter
    private var showModelFromCast: java.util.ArrayList<ShowModel>? = ArrayList()

    private var isShowCastAdapter = false

    private var currentPage = 1
    private var maxPage = 6
    private val limit = 20

    override fun getViewBinding(): ActivityAllMoviesBinding =
        ActivityAllMoviesBinding.inflate(layoutInflater)

    override fun setupIntent() {
        isShowCastAdapter = intent.getBooleanExtra(EXTRA_CAST_MOVIE, false)
        showModelFromCast = intent.getParcelableArrayListExtra(EXTRA_MOVIE)
    }

    override fun setupUI() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setupToolbar(binding.toolbar, "", false)
        binding.tvTitle.text = getString(R.string.tv_series)

        setupRecyclerView()
    }

    override fun setupAction() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun setupProcess() {
        tvSeriesViewModel.setPage(currentPage)

        with(binding) {
            nestedScrollMovie.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
                    val height = (v.getChildAt(0)?.measuredHeight ?: 0) - v.measuredHeight
                    if (scrollY == height) {
                        if (currentPage < maxPage) {
                            tvSeriesViewModel.setPage(++currentPage)
                        } else {
                            Toast.makeText(this@AllTvSeriesActivity, "Max Page", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                })
        }
    }

    override fun setupObserver() {
        with(binding) {
            if (isShowCastAdapter) {
                progressBar.visibility = View.GONE
                tvNowPlaying.text = ""
                adapter.setList(showModelFromCast)
            } else {
                when (intent.getIntExtra(EXTRA_TV, 0)) {
                    POPULAR -> {
                        tvNowPlaying.text = getString(R.string.popular)
                        tvSeriesViewModel.getAllTv(POPULAR, limit, this@AllTvSeriesActivity)
                            .observes(this@AllTvSeriesActivity,
                                onLoading = {
                                    progressBar.visible()
                                },
                                onSuccess = {
                                    progressBar.gone()

                                    val data = it as ArrayList<ShowModel>
                                    if (data.isNotEmpty()) {
                                        adapter.setList(data)
                                    }
                                },
                                onError = {
                                    progressBar.gone()
                                    Toast.makeText(
                                        this@AllTvSeriesActivity,
                                        "$it",
                                        Toast.LENGTH_LONG
                                    ).show()
                                })
                    }
                    TOP_RATED -> {
                        tvNowPlaying.text = getString(R.string.top_rated)
                        tvSeriesViewModel.getAllTv(TOP_RATED, limit, this@AllTvSeriesActivity)
                            .observes(this@AllTvSeriesActivity,
                                onLoading = {
                                    progressBar.visible()

                                },
                                onSuccess = {
                                    progressBar.gone()
                                    val data = it as ArrayList<ShowModel>
                                    if (data.isNotEmpty()) {
                                        adapter.setList(data)
                                    }
                                },
                                onError = {
                                    progressBar.gone()
                                    Toast.makeText(
                                        this@AllTvSeriesActivity,
                                        "$it",
                                        Toast.LENGTH_LONG
                                    ).show()
                                })

                    }
                    else -> {
                        tvSeriesViewModel.getAllTv(NOW_PLAYING, limit, this@AllTvSeriesActivity)
                            .observes(this@AllTvSeriesActivity,
                                onLoading = {
                                    progressBar.visible()

                                },
                                onSuccess = {
                                    progressBar.gone()
                                    val data = it as ArrayList<ShowModel>
                                    if (data.isNotEmpty()) {
                                        adapter.setList(data)
                                    }
                                },
                                onError = {
                                    progressBar.gone()
                                    Toast.makeText(
                                        this@AllTvSeriesActivity,
                                        "$it",
                                        Toast.LENGTH_LONG
                                    ).show()
                                })
                    }
                }

            }
        }

    }

    private fun setupRecyclerView() {
        adapter = ShowAllAdapter {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, it)
            startActivity(intent)
        }

        with(binding) {
            rvAll.adapter = adapter
            rvAll.layoutManager = GridLayoutManager(this@AllTvSeriesActivity, 2)
        }
    }


}