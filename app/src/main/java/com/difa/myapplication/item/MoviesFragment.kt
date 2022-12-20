package com.difa.myapplication.item

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.difa.myapplication.R
import com.difa.myapplication.core.base.BaseFragment
import com.difa.myapplication.core.domain.model.ShowModel
import com.difa.myapplication.core.ui.ShowAdapter
import com.difa.myapplication.core.utils.*
import com.difa.myapplication.databinding.FragmentMoviesBinding
import com.difa.myapplication.detail.DetailActivity
import com.difa.myapplication.showall.AllMoviesActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : BaseFragment<FragmentMoviesBinding>() {

    private val moviesViewModel: MoviesViewModel by viewModels()

    private lateinit var adapterNowPlaying: ShowAdapter
    private lateinit var adapterPopular: ShowAdapter
    private lateinit var adapterTopRated: ShowAdapter

    private val currentPage = 1
    private val limit = 7

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMoviesBinding =
        FragmentMoviesBinding.inflate(inflater, container, false)


    override fun setupIntent() {

    }

    override fun setupUI() {
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
        moviesViewModel.setPage(currentPage)
    }

    override fun setupObserver() {
        moviesViewModel.getAllMovie(NOW_PLAYING, limit, requireContext()).observes(viewLifecycleOwner,
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
                    if (errorMessage.contains(getString(R.string.unable_to_resolve))) {
                        binding.viewError.tvError.text =
                            "$errorMessage \n\nPlease, check your Internet connection \nand try again!"
                    } else {
                        binding.viewError.tvError.text = "$errorMessage"
                    }
                }
            )

        moviesViewModel.getAllMovie(POPULAR, limit, requireContext()).observes(viewLifecycleOwner,
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
                if (errorMessage.contains(getString(R.string.unable_to_resolve))) {
                    binding.viewError.tvError.text =
                        "$errorMessage \n\nPlease, check your Internet connection \nand try again!"
                } else {
                    binding.viewError.tvError.text = "$errorMessage"
                }
            }
        )

        moviesViewModel.getAllMovie(TOP_RATED, limit, requireContext()).observes(viewLifecycleOwner,
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
                if (errorMessage.contains(getString(R.string.unable_to_resolve))) {
                    binding.viewError.tvError.text =
                        "$errorMessage \n\nPlease, check your Internet connection \nand try again!"
                } else {
                    binding.viewError.tvError.text = "$errorMessage"
                }
            }
        )
    }

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

    private fun intentToAllMoviesActivity(category: Int) {
        val intent = Intent(requireActivity(), AllMoviesActivity::class.java)
        intent.putExtra(EXTRA_MOVIE, category)
        startActivity(intent)
    }

    private fun intentToDetailActivity(showModel: ShowModel) {
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra(EXTRA_DETAIL, showModel)
        startActivity(intent)
    }

}