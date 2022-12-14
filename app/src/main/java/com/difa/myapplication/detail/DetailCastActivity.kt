package com.difa.myapplication.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.difa.myapplication.core.data.Resource
import com.difa.myapplication.core.domain.model.CastModel
import com.difa.myapplication.core.ui.ShowAdapter
import com.difa.myapplication.core.utils.*
import com.difa.myapplication.databinding.ActivityDetailCastBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailCastActivity : AppCompatActivity() {

    private val detailViewModel: DetailViewModel by viewModels()

    private lateinit var binding: ActivityDetailCastBinding

    private lateinit var movieAdapter: ShowAdapter

    private lateinit var tvAdapter: ShowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCastBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.title = ""

        val data = intent.getParcelableExtra<CastModel>(EXTRA_DETAIL_CAST)

        binding.tvTitleApp.text = data?.name

        binding.btnBack.setOnClickListener {
            finish()
        }


        setupRecyclerView()
        setupDetailCast(data)

    }

    private fun setupDetailCast(data: CastModel?) {
        if (data != null) {
            with(data) {
                with(binding) {
                    tvName.text = name
                    tvKnownAs.text = knownAs
                    Glide.with(this@DetailCastActivity)
                        .load(URL_IMAGE_ORIGINAL + profilePath)
                        .into(imgMovie)

                    detailViewModel.getDetailCast(castId, id, character).observe(this@DetailCastActivity) { castModel ->
                        when (castModel) {
                            is Resource.Loading -> {
                                viewLoading.root.visibility = View.VISIBLE
                            }
                            is Resource.Success -> {
                                viewLoading.root.visibility = View.GONE
                                if(castModel.data?.biography != ""){
                                    tvBiography.text = castModel.data?.biography
                                    binding.tvBiography.setOnClickListener {
                                        val intent = Intent(this@DetailCastActivity, BiographyActivity::class.java)
                                        intent.putExtra(EXTRA_BIOGRAPHY, castModel.data?.biography)
                                        startActivity(intent)
                                    }
                                }
                                if(castModel.data?.birthDay != ""){
                                    tvBorn.text = castModel.data?.birthDay
                                }
                                if(castModel.data?.deathDay != ""){
                                    tvTitleDead.visibility = View.VISIBLE
                                    tvDead.visibility = View.VISIBLE
                                    tvDead.text = castModel.data?.deathDay
                                }
                            }
                            is Resource.Error -> {
                                viewLoading.root.visibility = View.GONE
                                Toast.makeText(this@DetailCastActivity, "${castModel.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    detailViewModel.getCastMovieOrTv(castId, MOVIE).observe(this@DetailCastActivity){ showModel ->
                        when(showModel){
                            is Resource.Loading -> {

                            }
                            is Resource.Success -> {
                                movieAdapter.setList(showModel.data, false)
                            }
                            is Resource.Error -> {
                              Toast.makeText(this@DetailCastActivity, "${showModel.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    detailViewModel.getCastMovieOrTv(castId, TV_SERIES).observe(this@DetailCastActivity){ showModel ->
                        when(showModel){
                            is Resource.Loading -> {

                            }
                            is Resource.Success -> {
                                tvAdapter.setList(showModel.data, false)
                            }
                            is Resource.Error -> {
                                Toast.makeText(this@DetailCastActivity, "${showModel.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        movieAdapter = ShowAdapter {
            val intent = Intent(this@DetailCastActivity, DetailActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, it)
            startActivity(intent)
        }

        tvAdapter = ShowAdapter {
            val intent = Intent(this@DetailCastActivity, DetailActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, it)
            startActivity(intent)
        }

        with(binding){
            rvFilmography.adapter = movieAdapter
            rvFilmography.layoutManager = LinearLayoutManager(this@DetailCastActivity, LinearLayoutManager.HORIZONTAL, false)
            rvTv.adapter = tvAdapter
            rvTv.layoutManager = LinearLayoutManager(this@DetailCastActivity, LinearLayoutManager.HORIZONTAL, false)

        }
    }
}