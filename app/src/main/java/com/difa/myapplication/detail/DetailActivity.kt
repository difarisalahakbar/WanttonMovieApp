package com.difa.myapplication.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.difa.myapplication.MainActivity
import com.difa.myapplication.R
import com.difa.myapplication.core.base.BaseActivity
import com.difa.myapplication.core.data.Resource
import com.difa.myapplication.core.domain.model.ShowModel
import com.difa.myapplication.core.ui.CastAdapter
import com.difa.myapplication.core.ui.ShowAdapter
import com.difa.myapplication.core.utils.*
import com.difa.myapplication.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : BaseActivity<ActivityDetailBinding>() {

    private val detailViewModel: DetailViewModel by viewModels()

    private lateinit var adapter: CastAdapter
    private lateinit var similarAdapter: ShowAdapter

    private lateinit var currentShowModel: ShowModel

    private var data: ShowModel? = null

    override fun getViewBinding(): ActivityDetailBinding =
        ActivityDetailBinding.inflate(layoutInflater)

    override fun setupIntent() {
        data = intent.getParcelableExtra(EXTRA_DETAIL)
    }

    override fun setupUI() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setupToolbar(binding.toolbar, "", false)

        setupRecyclerView()
    }

    override fun setupAction() {
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this@DetailActivity, MainActivity::class.java))
        }
    }

    override fun setupProcess() {
        //setDetail from data local
        if (data != null) {
            with(binding) {
                with(data) {
                    tvTitle.text = this!!.title
                    tvRate.text = voteAverage
                    if(overview.isNotEmpty()){
                        tvOverview.text = overview
                    }else{
                        tvOverview.text = "-"
                    }

                    tvOverview.post {
                        val l = tvOverview.layout
                        if (l != null) {
                            val lines = l.lineCount
                            if (lines > 0)
                                if (l.getEllipsisCount(lines - 1) > 0) {
                                    btnExpandShrink.visibility = View.VISIBLE

                                }
                        }
                    }

                    btnExpandShrink.setOnClickListener {
                        if (btnExpandShrink.text == getString(R.string.show_more)) {
                            btnExpandShrink.text = getString(R.string.show_less)
                            tvOverview.ellipsize = null
                            tvOverview.maxLines = Integer.MAX_VALUE
                        } else {
                            btnExpandShrink.text = getString(R.string.show_more)
                            tvOverview.ellipsize = TextUtils.TruncateAt.END
                            tvOverview.maxLines = 4
                        }
                    }
                    if (releaseDate != null && releaseDate != "" && releaseDate != "-") {
                        tvYear.text = releaseDate?.substring(0, 4)
                    }
                    if (data!!.posterPath != "" && data!!.posterPath != null) {
                        Glide.with(this@DetailActivity)
                            .load(URL_IMAGE + posterPath)
                            .into(imgMovie)
                    } else {
                        imgMovie.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@DetailActivity,
                                R.drawable.empty
                            )
                        )
                    }
                    setupSimilarShow(id, showType)
                }
            }
        }


    }

    override fun setupObserver() {
        when (data?.showType) {
            MOVIE -> {
                if (data != null) {
                    with(binding) {
                        with(data) {
                            //setDetail from new api response
                            detailViewModel.getMovieDetail(this!!.id, category)
                                .observes(this@DetailActivity,
                                onLoading = {
                                    viewLoading.root.visible()
                                    cardMovie.gone()
                                },
                                onSuccess = {
                                    viewLoading.root.gone()
                                    cardMovie.visible()
                                    currentShowModel = it
                                    setFavorite(currentShowModel)

                                    Glide.with(this@DetailActivity)
                                        .load(URL_IMAGE_ORIGINAL + it.backdropPath)
                                        .into(imgTimeline)

                                    if (it.genres1 != null) {
                                        tvGenre1.text = it.genres1
                                    }

                                    if (it.genres2 != null) {
                                        dot6.visible()
                                        tvGenre2.visible()
                                        tvGenre2.text = it.genres2
                                    }

                                    if (it.genres3 != null) {
                                        dot7.visible()
                                        tvGenre3.visible()
                                        tvGenre3.text = it.genres3
                                    }
                                    val runtime = it.runtime
                                    val duration =
                                        if (runtime?.div(60)!! > 0 && runtime.mod(60) > 0) {
                                            "${runtime.div(60)}h ${runtime.mod(60)}m"
                                        } else if (runtime.div(60) == 0 && runtime.mod(
                                                60
                                            ) > 0
                                        ) {
                                            "${runtime.mod(60)}m"
                                        } else if ((runtime.div(60) > 0 && runtime.mod(
                                                60
                                            ) == 0)
                                        ) {
                                            "${runtime.div(60)}h"
                                        } else {
                                            ""
                                        }
                                    tvDuration.text = duration
                                },
                                onError = {
                                    viewLoading.root.gone()
                                    cardMovie.visible()
                                    Toast.makeText(
                                        this@DetailActivity,
                                        "$it",
                                        Toast.LENGTH_LONG
                                    ).show()
                                })

                            //setDetailCast from new api response
                            detailViewModel.getMovieCast(id)
                                .observe(this@DetailActivity) { castModel ->
                                    if (castModel != null) {
                                        when (castModel) {
                                            is Resource.Loading -> {

                                            }
                                            is Resource.Success -> {
                                                if(castModel.data!!.isNotEmpty()){
                                                    adapter.setList(castModel.data)
                                                }else{
                                                    binding.actor.gone()
                                                }
                                            }
                                            is Resource.Error -> {
                                                Toast.makeText(
                                                    this@DetailActivity,
                                                    "${castModel.message}",
                                                    Toast.LENGTH_LONG
                                                ).show()

                                            }
                                        }
                                    }
                                }
                        }
                    }
                }
            }
            TV_SERIES -> {
                if (data != null) {
                    with(binding) {
                        with(data) {
                            //setDetail from new api response
                            detailViewModel.getTvDetail(this!!.id, category)
                                .observes(this@DetailActivity,
                                    onLoading = {
                                        viewLoading.root.visible()
                                        cardMovie.gone()
                                    },
                                    onSuccess = {
                                        viewLoading.root.gone()
                                        cardMovie.visible()
                                        currentShowModel = it
                                        setFavorite(currentShowModel)

                                        Glide.with(this@DetailActivity)
                                            .load(URL_IMAGE_ORIGINAL + it.backdropPath)
                                            .into(imgTimeline)

                                        if (it.genres1 != null) {
                                            tvGenre1.text = it.genres1
                                        }

                                        if (it.genres2 != null) {
                                            dot6.visible()
                                            tvGenre2.visible()
                                            tvGenre2.text = it.genres2
                                        }

                                        if (it.genres3 != null) {
                                            dot7.visible()
                                            tvGenre3.visible()
                                            tvGenre3.text = it.genres3
                                        }
                                        tvDuration.text = "N/A"
                                    },
                                    onError = {
                                        cardMovie.visible()
                                        viewLoading.root.gone()
                                        Toast.makeText(
                                            this@DetailActivity,
                                            "$it",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    })

                            //setDetailCast from new api response
                            detailViewModel.getTvCast(id)
                                .observe(this@DetailActivity) { castModel ->
                                    if (castModel != null) {
                                        when (castModel) {
                                            is Resource.Loading -> {

                                            }
                                            is Resource.Success -> {
                                                if(castModel.data!!.isNotEmpty()){
                                                    adapter.setList(castModel.data)
                                                }else{
                                                    binding.actor.gone()
                                                }
                                            }
                                            is Resource.Error -> {
                                                Toast.makeText(
                                                    this@DetailActivity,
                                                    "${castModel.message}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                    }
                                }
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        similarAdapter = ShowAdapter {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, it)
            startActivity(intent)
        }

        with(binding) {
            rvSimilar.adapter = similarAdapter
            rvSimilar.layoutManager =
                LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        adapter = CastAdapter {
            val intent = Intent(this@DetailActivity, DetailCastActivity::class.java)
            intent.putExtra(EXTRA_DETAIL_CAST, it)
            startActivity(intent)
        }

        with(binding) {
            rvCast.adapter = adapter
            rvCast.layoutManager =
                LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setFavorite(showModel: ShowModel) {
        var state = showModel.isFavorite
        setDesign(state)
        binding.btnFavorite.setOnClickListener {
            state = !state
            detailViewModel.setFavorite(showModel, state)
            setDesign(state)
        }
    }

    private fun setDesign(state: Boolean) {
        if (state) {
            binding.btnFavorite.text = getString(R.string.remove_favorite)
            binding.btnFavorite.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey))
            binding.btnFavorite.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_baseline_remove_24,
                0,
                0,
                0
            )
        } else {
            binding.btnFavorite.text = getString(R.string.add_favorite)
            binding.btnFavorite.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            binding.btnFavorite.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_baseline_add_24,
                0,
                0,
                0
            )
        }
    }

    private fun setupSimilarShow(id: String, showType: Int) {
        detailViewModel.getAllSimilarShow(id, showType).observe(this) { showModel ->
            when (showModel) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if(showModel.data!!.isNotEmpty()){
                        similarAdapter.setList(showModel.data, false)
                    }else{
                        binding.tvSimilar.gone()
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(this@DetailActivity, "${showModel.message}", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }


}