package com.difa.myapplication.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.difa.myapplication.R
import com.difa.myapplication.core.ui.ShowAllAdapter
import com.difa.myapplication.core.utils.EXTRA_DETAIL
import com.difa.myapplication.databinding.FragmentFavoriteBinding
import com.difa.myapplication.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null

    private val favoriteViewModel: FavoriteViewModel by viewModels()

    private lateinit var adapter: ShowAllAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupFavorite()

    }

    private fun setupFavorite() {
        favoriteViewModel.getAllFavorite().observe(viewLifecycleOwner){
            adapter.setList(it)
            binding.viewEmpty.tvError.text = getString(R.string.empty_favorite)
            binding.viewEmpty.root.visibility =
                if (it.isNotEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        adapter = ShowAllAdapter {
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, it)
            startActivity(intent)
        }

        with(binding){
            rvFavorite.adapter = adapter
            rvFavorite.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}