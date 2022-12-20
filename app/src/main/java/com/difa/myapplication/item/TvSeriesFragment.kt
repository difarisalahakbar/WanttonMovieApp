package com.difa.myapplication.item

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.difa.myapplication.R
import com.difa.myapplication.core.base.BaseFragment
import com.difa.myapplication.core.data.Resource
import com.difa.myapplication.core.domain.model.ShowModel
import com.difa.myapplication.core.ui.ShowAdapter
import com.difa.myapplication.core.utils.*
import com.difa.myapplication.databinding.FragmentMoviesBinding
import com.difa.myapplication.detail.DetailActivity
import com.difa.myapplication.showall.AllMoviesActivity
import com.difa.myapplication.showall.AllTvSeriesActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvSeriesFragment : BaseFragment<FragmentMoviesBinding>() {

    private val tvSeriesViewModel: TvSeriesViewModel by viewModels()

    private lateinit var adapterNowPlaying: ShowAdapter
    private lateinit var adapterPopular: ShowAdapter
    private lateinit var adapterTopRated: ShowAdapter

    private val currentPage = 1
    private val limit = 7

    private fun setupRecyclerView() {
        adapterNowPlaying = ShowAdapter {
            intentToDetailActivity(it)
        }
        adapterPopular = ShowAdapter {
            intentToDetailActivity(it)
        }
        adapterTopRated = ShowAdapter {
            intentToDetailActivity(it)
        }

        with(binding) {
            rvNowPlaying.adapter = adapterNowPlaying
            rvNowPlaying.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            rvPopular.adapter = adapterPopular
            rvPopular.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            rvTopRated.adapter = adapterTopRated
            rvTopRated.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMoviesBinding =
        FragmentMoviesBinding.inflate(inflater, container, false)

    override fun setupIntent() {
    }

    override fun setupUI() {
        binding.tvNowPlaying.text = getString(R.string.on_the_air)

        setupRecyclerView()
    }

    override fun setupAction() {
        binding.viewAllPopular.setOnClickListener {
            intentToAllMoviesActivity(POPULAR)
        }
        binding.viewAllNowPlayings.setOnClickListener {
            intentToAllMoviesActivity(NOW_PLAYING)
        }
        binding.viewAllTopRated.setOnClickListener {
            intentToAllMoviesActivity(TOP_RATED)
        }
        binding.swipeRefresh.setOnRefreshListener {
            setupObserver()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun setupProcess() {
        tvSeriesViewModel.setPage(currentPage)
    }

    override fun setupObserver() {
        tvSeriesViewModel.getAllTv(NOW_PLAYING, limit, requireContext()).observes(viewLifecycleOwner,
                onLoading = {
                    binding.shimmerNowPlaying.visible()
                },
                onSuccess = {
                    binding.shimmerNowPlaying.gone()
                    adapterNowPlaying.setList(it, true)
                },
                onError = {
                    binding.shimmerNowPlaying.gone()
                    binding.viewError.root.visible()
                    val errorMessage = it
                    if (errorMessage.contains("Unable to resolve host")) {
                        binding.viewError.tvError.text =
                            "$errorMessage \n\nPlease, check your Internet connection \nand try again!"
                    } else {
                        binding.viewError.tvError.text = "$errorMessage"
                    }
                }
            )

        tvSeriesViewModel.getAllTv(POPULAR, limit, requireContext()).observes(viewLifecycleOwner,
            onLoading = {
                binding.shimmerPopular.visible()
            },
            onSuccess = {
                binding.shimmerPopular.gone()
                adapterPopular.setList(it, true)
            },
            onError = {
                binding.shimmerPopular.gone()
                binding.viewError.root.visible()
                val errorMessage = it
                if (errorMessage.contains("Unable to resolve host")) {
                    binding.viewError.tvError.text =
                        "$errorMessage \n\nPlease, check your Internet connection \nand try again!"
                } else {
                    binding.viewError.tvError.text = "$errorMessage"
                }
            }
        )

        tvSeriesViewModel.getAllTv(TOP_RATED, limit, requireContext()).observes(viewLifecycleOwner,
            onLoading = {
                binding.shimmerTopRated.visible()
            },
            onSuccess = {
                binding.shimmerTopRated.gone()
                adapterTopRated.setList(it, true)
            },
            onError = {
                binding.shimmerTopRated.gone()
                binding.viewError.root.visible()
                val errorMessage = it
                if (errorMessage.contains("Unable to resolve host")) {
                    binding.viewError.tvError.text =
                        "$errorMessage \n\nPlease, check your Internet connection \nand try again!"
                } else {
                    binding.viewError.tvError.text = "$errorMessage"
                }
            }
        )
    }

    private fun intentToAllMoviesActivity(category: Int) {
        val intent = Intent(requireActivity(), AllTvSeriesActivity::class.java)
        intent.putExtra(EXTRA_TV, category)
        startActivity(intent)
    }

    private fun intentToDetailActivity(showModel: ShowModel) {
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra(EXTRA_DETAIL, showModel)
        startActivity(intent)
    }
}