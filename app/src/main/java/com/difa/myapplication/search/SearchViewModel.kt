package com.difa.myapplication.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.difa.myapplication.core.data.Resource
import com.difa.myapplication.core.domain.model.ShowModel
import com.difa.myapplication.core.domain.usecase.ShowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val showUseCase: ShowUseCase) : ViewModel() {

    fun searchShow(keyword: String): LiveData<Resource<List<ShowModel>>> = showUseCase.searchShow(keyword).asLiveData()

}