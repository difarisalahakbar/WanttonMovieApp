package com.difa.myapplication.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.difa.myapplication.core.domain.model.ShowModel
import com.difa.myapplication.core.domain.usecase.ShowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val showUseCase: ShowUseCase): ViewModel() {
    fun getMovieDetail(movieId: String, category: Int) = showUseCase.getMovieDetail(movieId, category).asLiveData()

    fun getTvDetail(tvId: String, category: Int) = showUseCase.getTvDetail(tvId, category).asLiveData()

    fun getMovieCast(showId: String) = showUseCase.getMovieCast(showId).asLiveData()

    fun getTvCast(showId: String) = showUseCase.getTvCast(showId).asLiveData()

    fun setFavorite(showModel: ShowModel, state: Boolean){
        viewModelScope.launch(Dispatchers.IO){
            showUseCase.setFavorite(showModel, state)
        }
    }

    fun getAllSimilarShow(id: String, showType: Int) = showUseCase.getAllSimilarShow(id, showType).asLiveData()

    fun getCastMovieOrTv(castId: String, showType: Int) = showUseCase.getCastMovieOrTv(castId, showType).asLiveData()

    fun getDetailCast(castId: String, showId: String, character: String) = showUseCase.getDetailCastById(castId, showId, character).asLiveData()
}