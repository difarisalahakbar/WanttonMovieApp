package com.difa.myapplication.core.data.source.remote

 import com.difa.myapplication.core.data.source.remote.network.ApiResponse
import com.difa.myapplication.core.data.source.remote.network.ApiService
import com.difa.myapplication.core.data.source.remote.response.CastItem
import com.difa.myapplication.core.data.source.remote.response.MovieItem
import com.difa.myapplication.core.data.source.remote.response.TvItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    fun getMoviesNowPlaying(page: Int): Flow<ApiResponse<List<MovieItem>>> =
        flow {
            try {
                val response = apiService.getMovieNowPlaying(page).results
                if (response.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    fun getMoviesPopular(page: Int): Flow<ApiResponse<List<MovieItem>>> =
        flow {
            try {
                val response = apiService.getMoviePopular(page).results
                if (response.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    fun getMoviesTopRated(page: Int): Flow<ApiResponse<List<MovieItem>>> =
        flow {
            try {
                val response = apiService.getMovieTopRated(page).results
                if (response.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    fun getTvPopular(page: Int): Flow<ApiResponse<List<TvItem>>> =
        flow {
            try {
                val response = apiService.getTvPopular(page).results
                if (response.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    fun getTvNowPlaying(page: Int): Flow<ApiResponse<List<TvItem>>> =
        flow {
            try {
                val response = apiService.getTvNowPlaying(page).results
                if (response.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    fun getTvTopRated(page: Int): Flow<ApiResponse<List<TvItem>>> =
        flow {
            try {
                val response = apiService.getTvTopRated(page).results
                if (response.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    fun getMovieDetail(movieId: String): Flow<ApiResponse<MovieItem>> =
        flow {
            try {
                val response = apiService.getMovieDetail(movieId)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    fun getTvDetail(tvId: String): Flow<ApiResponse<TvItem>> =
        flow {
            try {
                val response = apiService.getTvDetail(tvId)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    fun getMovieCast(movieId: String): Flow<ApiResponse<List<CastItem>>> =
        flow{
            try{
                val response = apiService.getMovieCast(movieId).cast
                val list = ArrayList<CastItem>()
                if(response.size > 10){
                   list.addAll(response.take(10))
                }else{
                    list.addAll(response)
                }

                if(response.isNotEmpty()){
                    emit(ApiResponse.Success(list))
                }else{
                    emit(ApiResponse.Empty)
                }
            }catch (e: Exception){
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    fun getTvCast(tvId: String): Flow<ApiResponse<List<CastItem>>> =
        flow{
            try{
                val response = apiService.getTvCast(tvId).cast
                val list = ArrayList<CastItem>()
                if(response.size > 10){
                    list.addAll(response.take(10))
                }else{
                    list.addAll(response)
                }
                if(response.isNotEmpty()){
                    emit(ApiResponse.Success(list))
                }else{
                    emit(ApiResponse.Empty)
                }
            }catch (e: Exception){
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    fun searchMovie(keyword: String): Flow<ApiResponse<List<MovieItem>>> =
        flow {
            try{
                val response = apiService.searchMovie(keyword).results
                if(response.isNotEmpty()){
                    emit(ApiResponse.Success(response))
                }else{
                    emit(ApiResponse.Empty)
                }
            }catch (e: Exception){
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    fun searchTv(keyword: String): Flow<ApiResponse<List<TvItem>>> =
        flow {
            try{
                val response = apiService.searchTv(keyword).results
                if(response.isNotEmpty()){
                    emit(ApiResponse.Success(response))
                }else{
                    emit(ApiResponse.Empty)
                }
            }catch (e: Exception){
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    fun getAllSimilarMovie(movieId: String): Flow<ApiResponse<List<MovieItem>>> {
        return flow {
            try {
                val response = apiService.getAllSimilarMovie(movieId).results
                if (response.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getAllSimilarTv(seriesId: String): Flow<ApiResponse<List<TvItem>>> {
        return flow {
            try {
                val response = apiService.getAllSimilarTv(seriesId).results
                if (response.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

}