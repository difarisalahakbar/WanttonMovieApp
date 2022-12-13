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
import com.difa.myapplication.core.data.Resource
import com.difa.myapplication.core.ui.ShowAdapter
import com.difa.myapplication.core.utils.*
import com.difa.myapplication.databinding.FragmentMoviesBinding
import com.difa.myapplication.detail.DetailActivity
import com.difa.myapplication.showall.AllMoviesActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private val moviesViewModel: MoviesViewModel by viewModels()

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

        setupRecyclerView()
        setupMovies()

        binding.viewAllPopular.setOnClickListener {
            val intent = Intent(requireActivity(), AllMoviesActivity::class.java)
            intent.putExtra(EXTRA_MOVIE, POPULAR)
            startActivity(intent)
        }
        binding.viewAllNowPlaying.setOnClickListener {
            val intent = Intent(requireActivity(), AllMoviesActivity::class.java)
            intent.putExtra(EXTRA_MOVIE, NOW_PLAYING)
            startActivity(intent)
        }
        binding.viewAllTopRated.setOnClickListener {
            val intent = Intent(requireActivity(), AllMoviesActivity::class.java)
            intent.putExtra(EXTRA_MOVIE, TOP_RATED)
            startActivity(intent)
        }
        binding.swipeRefresh.setOnRefreshListener {
            setupMovies()
            binding.swipeRefresh.isRefreshing = false
        }

    }

    private fun setupRecyclerView() {
        adapterNowPlaying = ShowAdapter {
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, it)
            startActivity(intent)
        }
        adapterPopular = ShowAdapter {
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, it)
            startActivity(intent)
        }
        adapterTopRated = ShowAdapter {
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

    private fun setupMovies() {
        moviesViewModel.setPage(currentPage)

        moviesViewModel.getAllMovie(NOW_PLAYING, limit).observe(viewLifecycleOwner) { showModel ->
            if (showModel != null) {
                when (showModel) {
                    is Resource.Loading -> {
                        binding.shimmerNowPlaying.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.shimmerNowPlaying.visibility = View.GONE
                        adapterNowPlaying.setList(showModel.data, true)
                    }
                    is Resource.Error -> {
                        binding.shimmerNowPlaying.visibility = View.GONE
                        binding.viewError.root.visibility = View.VISIBLE
                        val errorMessage = showModel.message
                        if (errorMessage!!.contains(getString(R.string.unable_to_resolve))){
                            binding.viewError.tvError.text = "$errorMessage \n\nPlease, check your Internet connection \nand try again!"
                        }else{
                            binding.viewError.tvError.text = "$errorMessage"
                        }
                    }

                }
            }
        }



        moviesViewModel.getAllMovie(POPULAR, limit).observe(viewLifecycleOwner) { showModel ->
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
        moviesViewModel.getAllMovie(TOP_RATED, limit).observe(viewLifecycleOwner) { showModel ->
            if (showModel != null) {
                when (showModel) {
                    is Resource.Loading -> {
                        binding.shimmerTopRated.visibility = View.VISIBLE
                     }
                    is Resource.Success -> {
                        binding.shimmerTopRated.visibility = View.GONE

                        adapterTopRated.setList(showModel.data,true)
                    }
                    is Resource.Error -> {
                        binding.shimmerTopRated.visibility = View.GONE

                    }

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}