package com.difa.myapplication.search

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.difa.myapplication.core.data.Resource
import com.difa.myapplication.core.ui.ShowSearchAdapter
import com.difa.myapplication.core.utils.EXTRA_DETAIL
import com.difa.myapplication.databinding.FragmentSearchBinding
import com.difa.myapplication.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var adapter: ShowSearchAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()

    }

    private fun setupRecyclerView() {
        adapter = ShowSearchAdapter {
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, it)
            startActivity(intent)
        }

        with(binding) {
            rvSearch.adapter = adapter
            rvSearch.layoutManager = LinearLayoutManager(requireContext())
        }

    }

    private fun setupSearch() {
        binding.textInput.apply {
            editText?.let { editText ->
                setEndIconOnClickListener {
                    editText.setText("")
                }

                editText.setOnKeyListener { _, keyId, _ ->
                    if (keyId == KeyEvent.KEYCODE_ENTER) {

                        searchShow(editText.text.toString().trim())

                        return@setOnKeyListener true
                    }
                    return@setOnKeyListener false

                }

                editText.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                        searchShow(editText.text.toString().trim())

                        return@setOnEditorActionListener true
                    }
                    return@setOnEditorActionListener false
                }

            }
            requestFocus()
        }
    }

    private fun searchShow(keyword: String) {
        binding.viewHow.root.visibility = View.GONE
        searchViewModel.searchShow(keyword).observe(viewLifecycleOwner) { searchShow ->
            if (searchShow != null) {
                when (searchShow) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.viewError.root.visibility = View.GONE
                        binding.viewEmpty.root.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.viewError.root.visibility = View.GONE
                        binding.viewEmpty.root.visibility = View.GONE
                        adapter.setList(searchShow.data)
                        if (searchShow.data!!.isEmpty()) {
                            binding.viewEmpty.root.visibility = View.VISIBLE
                            binding.viewEmpty.tvError.text = "Show not found."
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.viewError.root.visibility = View.VISIBLE
                        binding.viewEmpty.root.visibility = View.GONE
                        val errorMessage = searchShow.message
                        if (errorMessage!!.contains("Unable to resolve host")) {
                            binding.viewError.tvError.text =
                                "$errorMessage \n\nPlease, check your Internet connection \nand try again!"
                        } else {
                            binding.viewError.tvError.text = "$errorMessage"
                        }
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