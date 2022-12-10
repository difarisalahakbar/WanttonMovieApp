package com.difa.myapplication.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.difa.myapplication.core.domain.usecase.ShowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val showUseCase: ShowUseCase) : ViewModel() {

    fun getAllFavorite() = showUseCase.getAllFavorite().asLiveData()

}