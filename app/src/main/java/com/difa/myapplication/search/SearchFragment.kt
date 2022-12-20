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
import com.difa.myapplication.R
import com.difa.myapplication.core.base.BaseFragment
import com.difa.myapplication.core.data.Resource
import com.difa.myapplication.core.ui.ShowSearchAdapter
import com.difa.myapplication.core.utils.EXTRA_DETAIL
import com.difa.myapplication.core.utils.gone
import com.difa.myapplication.core.utils.observes
import com.difa.myapplication.core.utils.visible
import com.difa.myapplication.databinding.FragmentSearchBinding
import com.difa.myapplication.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var adapter: ShowSearchAdapter

    private lateinit var keyword: String

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding =
        FragmentSearchBinding.inflate(inflater, container, false)

    override fun setupIntent() {
    }

    override fun setupUI() {
        setupRecyclerView()
    }

    override fun setupAction() {
        binding.textInput.apply {
            editText?.let { editText ->

                keyword = editText.text.toString().trim()

                setEndIconOnClickListener {
                    editText.setText("")
                }

                editText.setOnKeyListener { _, keyId, _ ->
                    if (keyId == KeyEvent.KEYCODE_ENTER) {
                        keyword = editText.text.toString().trim()

                        setupSearchData()

                        return@setOnKeyListener true
                    }
                    return@setOnKeyListener false

                }

                editText.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        keyword = editText.text.toString().trim()

                        setupSearchData()

                        return@setOnEditorActionListener true
                    }
                    return@setOnEditorActionListener false
                }

            }
            requestFocus()
        }
    }

    override fun setupProcess() {
    }

    override fun setupObserver() {
    }

    private fun setupSearchData(){
        with(binding) {
            viewHow.root.gone()
            searchViewModel.searchShow(keyword).observes(viewLifecycleOwner,
                onLoading = {
                    progressBar.visible()
                    viewError.root.gone()
                    viewEmpty.root.gone()
                },
                onSuccess = {
                    progressBar.gone()
                    viewError.root.gone()
                    viewEmpty.root.gone()
                    adapter.setList(it)
                    if (it.isEmpty()) {
                        binding.viewEmpty.root.visible()
                        binding.viewEmpty.tvError.text = getString(R.string.not_found)
                    }
                },
                onError = {
                    progressBar.gone()
                    viewError.root.visible()
                    viewEmpty.root.gone()
                    val errorMessage = it
                    if (errorMessage.contains(getString(R.string.unable_to_resolve))) {
                        viewError.tvError.text =
                            "$errorMessage \n\nPlease, check your Internet connection \nand try again!"
                    } else {
                        viewError.tvError.text = "$errorMessage"
                    }
                })
        }
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
}