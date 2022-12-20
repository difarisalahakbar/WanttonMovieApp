package com.difa.myapplication.detail

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.difa.myapplication.core.base.BaseActivity
import com.difa.myapplication.core.domain.model.CastModel
import com.difa.myapplication.core.domain.model.ShowModel
import com.difa.myapplication.core.ui.ShowCastAdapter
import com.difa.myapplication.core.utils.*
import com.difa.myapplication.databinding.ActivityDetailCastBinding
import com.difa.myapplication.showall.AllMoviesActivity
import com.difa.myapplication.showall.AllTvSeriesActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailCastActivity : BaseActivity<ActivityDetailCastBinding>() {

    private val detailViewModel: DetailViewModel by viewModels()

    private lateinit var movieAdapter: ShowCastAdapter

    private lateinit var tvAdapter: ShowCastAdapter

    private lateinit var data: CastModel

    private lateinit var fullListMovie: List<ShowModel>

    private lateinit var fullListTv: List<ShowModel>

    override fun getViewBinding(): ActivityDetailCastBinding =
        ActivityDetailCastBinding.inflate(layoutInflater)

    override fun setupIntent() {
        data = intent.getParcelableExtra<CastModel>(EXTRA_DETAIL_CAST) as CastModel
    }

    override fun setupUI() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setupToolbar(binding.toolbar, "", false)

        binding.tvTitleApp.text = data.name

        setupRecyclerView()
    }

    override fun setupAction() {
        with(binding){
            btnBack.setOnClickListener {
                finish()
            }
            tvViewAllMovie.setOnClickListener {
                intentToAllMoviesActivity()
            }
            tvViewAllTv.setOnClickListener {
                intentToAllTvSeriesActivity()
            }
        }


    }

    override fun setupProcess() {
    }

    override fun setupObserver() {
        with(data) {
            with(binding) {
                tvName.text = name
                tvKnownAs.text = knownAs
                Glide.with(this@DetailCastActivity)
                    .load(URL_IMAGE_ORIGINAL + profilePath)
                    .into(imgMovie)

                detailViewModel.getDetailCast(castId, id, character)
                    .observes(this@DetailCastActivity,
                    onLoading = {
                        viewLoading.root.visible()
                    },
                    onSuccess = {
                        viewLoading.root.gone()
                        if (it.biography != "") {
                            tvBiography.text = it.biography
                            tvBiography.setOnClickListener { _ ->
                                val intent = Intent(
                                    this@DetailCastActivity,
                                    BiographyActivity::class.java
                                )
                                intent.putExtra(EXTRA_BIOGRAPHY, it.biography)
                                startActivity(intent)
                            }
                        }
                        if (it.birthDay != "") {
                            tvBorn.text = it.birthDay
                        }
                        if (it.deathDay != "") {
                            tvTitleDead.visible()
                            tvDead.visible()
                            tvDead.text = it.deathDay
                        }
                    },
                    onError = {
                        viewLoading.root.gone()
                        Toast.makeText(
                            this@DetailCastActivity,
                            "$it",
                            Toast.LENGTH_LONG
                        ).show()
                    })

                detailViewModel.getCastMovieOrTv(castId, MOVIE)
                    .observes(this@DetailCastActivity,
                    onLoading = {

                    },
                    onSuccess = {
                        fullListMovie = it
                        if(it.isNotEmpty()){
                            if (it.size > 7) {
                                movieAdapter.setList(fullListMovie.take(7))
                            } else {
                                tvViewAllMovie.gone()
                                movieAdapter.setList(fullListMovie)
                            }
                        }else{
                            tvViewAllMovie.gone()
                            textView12.gone()
                        }

                        movieAdapter.setFullList(fullListMovie)
                    },
                    onError = {
                        Toast.makeText(
                            this@DetailCastActivity,
                            "$it",
                            Toast.LENGTH_LONG
                        ).show()
                    })

                detailViewModel.getCastMovieOrTv(castId, TV_SERIES)
                    .observes(this@DetailCastActivity,
                    onLoading = {

                    },
                    onSuccess = {
                        fullListTv = it
                        if(it.isNotEmpty()){
                            if (it.size > 7) {
                                tvAdapter.setList(fullListTv.take(7))
                            } else {
                                tvViewAllTv.gone()
                                tvAdapter.setList(fullListTv)
                            }
                        }else{
                            tvViewAllTv.gone()
                            textView13.gone()
                        }

                        tvAdapter.setFullList(fullListTv)
                    },
                    onError   = {
                        Toast.makeText(
                            this@DetailCastActivity,
                            "$it",
                            Toast.LENGTH_LONG
                        ).show()
                    })
            }

        }
    }

    private fun setupRecyclerView() {
        movieAdapter = ShowCastAdapter {
            val intent = Intent(this@DetailCastActivity, DetailActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, it)
            startActivity(intent)
        }

        tvAdapter = ShowCastAdapter {
            val intent = Intent(this@DetailCastActivity, DetailActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, it)
            startActivity(intent)
        }

        with(binding) {
            rvFilmography.adapter = movieAdapter
            rvFilmography.layoutManager =
                LinearLayoutManager(this@DetailCastActivity, LinearLayoutManager.HORIZONTAL, false)
            rvTv.adapter = tvAdapter
            rvTv.layoutManager =
                LinearLayoutManager(this@DetailCastActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun intentToAllMoviesActivity(){
        val intent = Intent(this@DetailCastActivity, AllMoviesActivity::class.java)
        intent.putExtra(EXTRA_CAST_MOVIE, true)
        intent.putExtra(EXTRA_MOVIE, fullListMovie as ArrayList)
        startActivity(intent)
    }
    private fun intentToAllTvSeriesActivity(){
        val intent = Intent(this@DetailCastActivity, AllTvSeriesActivity::class.java)
        intent.putExtra(EXTRA_CAST_MOVIE, true)
        intent.putExtra(EXTRA_MOVIE, fullListTv as ArrayList)
        startActivity(intent)
    }
}