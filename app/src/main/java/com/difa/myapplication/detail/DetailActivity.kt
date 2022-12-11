package com.difa.myapplication.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.difa.myapplication.R
import com.difa.myapplication.core.data.Resource
import com.difa.myapplication.core.domain.model.ShowModel
import com.difa.myapplication.core.ui.CastAdapter
import com.difa.myapplication.core.ui.ShowAdapter
import com.difa.myapplication.core.utils.*
import com.difa.myapplication.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()

    private lateinit var adapter: CastAdapter
    private lateinit var similarAdapter: ShowAdapter

    private lateinit var currentShowModel: ShowModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.title = ""

        setupRecyclerView()

        val data = intent.getParcelableExtra<ShowModel>(EXTRA_DETAIL)

        binding.btnBack.setOnClickListener {
            finish()
        }
        //setDetail from data local
        if (data != null) {
            with(data) {
                with(binding) {
                    tvTitle.text = title
                    tvRate.text = voteAverage
                    tvOverview.text = overview
                    if(releaseDate != null && releaseDate != "" && releaseDate != "-"){
                        tvYear.text = releaseDate?.substring(0, 4)
                    }
                    if (data.posterPath != "" && data.posterPath != null){
                        Glide.with(this@DetailActivity)
                            .load(URL_IMAGE + posterPath)
                            .into(imgMovie)
                    }else{
                        imgMovie.setImageDrawable(ContextCompat.getDrawable(this@DetailActivity, R.drawable.empty))
                    }
                }
                setupSimilarShow(id, showType)
            }
        }

        when (data?.showType){
            MOVIE -> setupDetailMovie(data)
            TV_SERIES -> setupDetailTv(data)
        }
    }

    private fun setupSimilarShow(id: String, showType: Int) {
        detailViewModel.getAllSimilarShow(id, showType).observe(this){ showModel ->
            when(showModel){
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    similarAdapter.setList(showModel.data, false)
                }
                is Resource.Error -> {

                }
            }
        }

    }

    private fun setupDetailTv(data: ShowModel?) {
        if (data != null) {
            with(data) {
                with(binding) {
                    //setDetail from new api response
                    detailViewModel.getTvDetail(id, category).observe(this@DetailActivity) { showModel ->
                            if (showModel != null) {
                                when (showModel) {
                                    is Resource.Loading -> {

                                    }
                                    is Resource.Success -> {
                                        currentShowModel = showModel.data!!
                                        setFavorite(currentShowModel)

                                        Glide.with(this@DetailActivity)
                                            .load(URL_IMAGE_ORIGINAL + showModel.data.backdropPath)
                                            .into(imgTimeline)

                                        tvGenre1.text = showModel.data.genres1

                                        if (showModel.data.genres2 != null) {
                                            dot6.visibility = View.VISIBLE
                                            tvGenre2.visibility = View.VISIBLE
                                            tvGenre2.text = showModel.data.genres2
                                        }

                                        if (showModel.data.genres3 != null) {
                                            dot7.visibility = View.VISIBLE
                                            tvGenre3.visibility = View.VISIBLE
                                            tvGenre3.text = showModel.data.genres3
                                        }
                                        tvDuration.text = "N/A"
                                    }
                                    is Resource.Error -> {
                                        Toast.makeText(this@DetailActivity, "${showModel.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }

                    //setDetailCast from new api response
                    detailViewModel.getTvCast(id).observe(this@DetailActivity) { castModel ->
                        if (castModel != null) {
                            when (castModel) {
                                is Resource.Loading -> {

                                }
                                is Resource.Success -> {
                                    adapter.setList(castModel.data)
                                }
                                is Resource.Error -> {
                                    Toast.makeText(this@DetailActivity, "${castModel.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupDetailMovie(data: ShowModel?) {
        if (data != null) {
            with(data) {
                with(binding) {
                    //setDetail from new api response
                    detailViewModel.getMovieDetail(id, category)
                        .observe(this@DetailActivity) { showModel ->
                            if (showModel != null) {
                                when (showModel) {
                                    is Resource.Loading -> {

                                    }
                                    is Resource.Success -> {
                                        currentShowModel = showModel.data!!
                                        setFavorite(currentShowModel)

                                        Glide.with(this@DetailActivity)
                                            .load(URL_IMAGE_ORIGINAL + showModel.data.backdropPath)
                                            .into(imgTimeline)

                                        tvGenre1.text = showModel.data.genres1

                                        if (showModel.data.genres2 != null) {
                                            dot6.visibility = View.VISIBLE
                                            tvGenre2.visibility = View.VISIBLE
                                            tvGenre2.text = showModel.data.genres2
                                        }

                                        if (showModel.data.genres3 != null) {
                                            dot7.visibility = View.VISIBLE
                                            tvGenre3.visibility = View.VISIBLE
                                            tvGenre3.text = showModel.data.genres3
                                        }
                                        val runtime = showModel.data.runtime
                                        val duration = if (runtime?.div(60)!! > 0) {
                                            "${runtime.div(60)}h ${runtime.mod(60)}m"
                                        } else {
                                            "${runtime.mod(60)}m"
                                        }
                                        tvDuration.text = duration
                                    }
                                    is Resource.Error -> {
                                        Toast.makeText(this@DetailActivity, "${showModel.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }

                    //setDetailCast from new api response
                    detailViewModel.getMovieCast(id).observe(this@DetailActivity) { castModel ->
                        if (castModel != null) {
                            when (castModel) {
                                is Resource.Loading -> {

                                }
                                is Resource.Success -> {
                                    adapter.setList(castModel.data)
                                }
                                is Resource.Error -> {
                                    Toast.makeText(this@DetailActivity, "${castModel.message}", Toast.LENGTH_LONG).show()

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

        with(binding){
            rvSimilar.adapter = similarAdapter
            rvSimilar.layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        adapter = CastAdapter()

        with(binding) {
            rvCast.adapter = adapter
            rvCast.layoutManager =
                LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setFavorite(showModel: ShowModel){
        var state = showModel.isFavorite
        setDesign(state)
        binding.btnFavorite.setOnClickListener {
            state = !state
            detailViewModel.setFavorite(showModel, state)
            setDesign(state)
        }
    }

    private fun setDesign(state: Boolean){
        if(state){
            binding.btnFavorite.text = getString(R.string.remove_favorite)
            binding.btnFavorite.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey))
            binding.btnFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_remove_24, 0, 0, 0)
        }else{
            binding.btnFavorite.text =getString(R.string.add_favorite)
            binding.btnFavorite.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            binding.btnFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_add_24, 0, 0, 0)
        }
    }
}