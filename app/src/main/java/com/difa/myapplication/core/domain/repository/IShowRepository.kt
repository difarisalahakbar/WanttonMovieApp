package com.difa.myapplication.core.domain.repository

import android.content.Context
import com.difa.myapplication.core.data.Resource
import com.difa.myapplication.core.domain.model.CastModel
import com.difa.myapplication.core.domain.model.ShowModel
import kotlinx.coroutines.flow.Flow

interface IShowRepository {

    fun getAllMovie(category: Int, page: Int, limit: Int, context: Context): Flow<Resource<List<ShowModel>>>

    fun getAllTv(category: Int, page: Int, limit: Int, context: Context): Flow<Resource<List<ShowModel>>>

    fun getMovieDetail(movieId: String, category: Int): Flow<Resource<ShowModel>>

    fun getTvDetail(tvId: String, category: Int): Flow<Resource<ShowModel>>

    fun getMovieCast(showId: String): Flow<Resource<List<CastModel>>>

    fun getTvCast(showId: String): Flow<Resource<List<CastModel>>>

    fun getAllFavorite(): Flow<List<ShowModel>>

    fun searchShow(keyword: String): Flow<Resource<List<ShowModel>>>

    fun getAllSimilarShow(id: String, showType: Int): Flow<Resource<List<ShowModel>>>

    fun getDetailCastById(castId: String, showId: String, character: String): Flow<Resource<CastModel>>

    fun getCastMovieOrTv(castId: String, showType: Int): Flow<Resource<List<ShowModel>>>

    suspend fun setFavorite(showModel: ShowModel, state: Boolean)

}