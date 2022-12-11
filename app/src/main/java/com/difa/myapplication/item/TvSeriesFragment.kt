package com.difa.myapplication.item

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.difa.myapplication.R
import com.difa.myapplication.core.data.Resource
import com.difa.myapplication.core.ui.ShowAdapter
import com.difa.myapplication.core.utils.*
import com.difa.myapplication.databinding.FragmentMoviesBinding
import com.difa.myapplication.detail.DetailActivity
import com.difa.myapplication.showall.AllTvSeriesActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvSeriesFragment : Fragment() {
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private val tvSeriesViewModel: TvSeriesViewModel by viewModels()

    private lateinit var adapterNowPlaying: ShowAdapter
    private lateinit var adapterPopular: ShowAdapter
    private lateinit var adapterTopRated: ShowAdapter

    private val currentPage = 1
    private val limit = 7

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvNowPlaying.text = getString(R.string.on_the_air)

        setupRecyclerView()
        setupTvSeries()

        binding.viewAllPopular.setOnClickListener {
            val intent = Intent(requireActivity(), AllTvSeriesActivity::class.java)
            intent.putExtra(EXTRA_TV, POPULAR)
            startActivity(intent)
        }
        binding.viewAllNowPlaying.setOnClickListener {
            val intent = Intent(requireActivity(), AllTvSeriesActivity::class.java)
            intent.putExtra(EXTRA_TV, NOW_PLAYING)
            startActivity(intent)
        }
        binding.viewAllTopRated.setOnClickListener {
            val intent = Intent(requireActivity(), AllTvSeriesActivity::class.java)
            intent.putExtra(EXTRA_TV, TOP_RATED)
            startActivity(intent)
        }
        binding.swipeRefresh.setOnRefreshListener {
            setupTvSeries()
            binding.swipeRefresh.isRefreshing = false
        }

    }

    private fun setupTvSeries() {
        tvSeriesViewModel.setPage(currentPage)

        tvSeriesViewModel.getAllTv(NOW_PLAYING, limit)
            .observe(viewLifecycleOwner) { showModel ->
                if (showModel != null) {
                    when (showModel) {
                        is Resource.Loading -> {
                            binding.shimmerNowPlaying.visibility = View.VISIBLE
                            binding.viewError.root.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            binding.shimmerNowPlaying.visibility = View.GONE
                            binding.viewError.root.visibility = View.GONE
                            adapterNowPlaying.setList(showModel.data, true)
                        }
                        is Resource.Error -> {
                            binding.shimmerNowPlaying.visibility = View.GONE
                            binding.viewError.root.visibility = View.VISIBLE
                            val errorMessage = showModel.message
                            if (errorMessage!!.contains("Unable to resolve host")){
                                binding.viewError.tvError.text = "$errorMessage \n\nPlease, check your Internet connection \nand try again!"
                            }else{
                                binding.viewError.tvError.text = "$errorMessage"
                            }
                        }

                    }
                }
            }

        tvSeriesViewModel.getAllTv(POPULAR, limit)
            .observe(viewLifecycleOwner) { showModel ->
                if (showModel != null) {
                    when (showModel) {
                        is Resource.Loading -> {
                            binding.shimmerPopular.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.shimmerPopular.visibility = View.GONE
                            adapterPopular.setList(showModel.data, true)
                        }
                        is Resource.Error -> {
                            binding.shimmerPopular.visibility = View.GONE
                        }

                    }
                }
            }

        tvSeriesViewModel.getAllTv(TOP_RATED, limit)
            .observe(viewLifecycleOwner) { showModel ->
                if (showModel != null) {
                    when (showModel) {
                        is Resource.Loading -> {
                            binding.shimmerTopRated.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.shimmerTopRated.visibility = View.GONE
                            adapterTopRated.setList(showModel.data, true)
                        }
                        is Resource.Error -> {
                            binding.shimmerTopRated.visibility = View.GONE
                        }
                    }
                }
            }
    }

    private fun setupRecyclerView() {
        adapterNowPlaying = ShowAdapter{
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, it)
            startActivity(intent)
        }
        adapterPopular = ShowAdapter{
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, it)
            startActivity(intent)
        }
        adapterTopRated = ShowAdapter{
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, it)
            startActivity(intent)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}