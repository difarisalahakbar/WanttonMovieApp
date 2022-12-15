package com.difa.myapplication.core.data

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.difa.myapplication.core.data.source.local.LocalDataSource
import com.difa.myapplication.core.data.source.remote.RemoteDataSource
import com.difa.myapplication.core.data.source.remote.network.ApiResponse
import com.difa.myapplication.core.data.source.remote.response.CastItem
import com.difa.myapplication.core.data.source.remote.response.MovieItem
import com.difa.myapplication.core.data.source.remote.response.TvItem
import com.difa.myapplication.core.domain.model.CastModel
import com.difa.myapplication.core.domain.model.ShowModel
import com.difa.myapplication.core.domain.repository.IShowRepository
import com.difa.myapplication.core.utils.*
import com.difa.myapplication.core.utils.DataMapper.isNetworkConnected
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : IShowRepository {

    override fun getAllMovie(
        category: Int,
        page: Int,
        limit: Int,
        context: Context
    ): Flow<Resource<List<ShowModel>>> =
        object : NetworkBoundResource<List<ShowModel>, List<MovieItem>>() {
            override fun loadFromDB(): Flow<List<ShowModel>> {
                return localDataSource.getAllMovie(category, limit)
                    .map { DataMapper.mapShowEntitiesToDomain(it) }
            }

            override fun shouldFetch(data: List<ShowModel>?): Boolean {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (isNetworkConnected(context)) {
                        true
                    } else {
                        data == null || data.isEmpty() || data.size != page * limit
                    }
                } else {
                    data == null || data.isEmpty() || data.size != page * limit
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<MovieItem>>> {
                return when (category) {
                    NOW_PLAYING -> remoteDataSource.getMoviesNowPlaying(page)
                    POPULAR -> remoteDataSource.getMoviesPopular(page)
                    else -> remoteDataSource.getMoviesTopRated(page)
                }
            }

            override suspend fun saveCallResult(data: List<MovieItem>) {
                val dataEntity = DataMapper.mapMovieResponseToEntities(data, category)
                localDataSource.insertAllShow(dataEntity)
            }
        }.asFlow()

    override fun getAllTv(category: Int, page: Int, limit: Int, context: Context): Flow<Resource<List<ShowModel>>> =
        object : NetworkBoundResource<List<ShowModel>, List<TvItem>>() {
            override fun loadFromDB(): Flow<List<ShowModel>> {
                return localDataSource.getAllTv(category, limit)
                    .map { DataMapper.mapShowEntitiesToDomain(it) }
            }

            override fun shouldFetch(data: List<ShowModel>?): Boolean {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (isNetworkConnected(context)) {
                        true
                    } else {
                        data == null || data.isEmpty() || data.size != page * limit
                    }
                } else {
                    data == null || data.isEmpty() || data.size != page * limit
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<TvItem>>> {
                return when (category) {
                    NOW_PLAYING -> remoteDataSource.getTvNowPlaying(page)
                    POPULAR -> remoteDataSource.getTvPopular(page)
                    else -> remoteDataSource.getTvTopRated(page)
                }
            }

            override suspend fun saveCallResult(data: List<TvItem>) {
                val dataEntity = DataMapper.mapTvResponseToEntities(data, category)
                localDataSource.insertAllShow(dataEntity)
            }
        }.asFlow()

    override fun getMovieDetail(movieId: String, category: Int): Flow<Resource<ShowModel>> {
        return object : NetworkBoundResource<ShowModel, MovieItem>() {
            public override fun loadFromDB(): Flow<ShowModel> {
                return localDataSource.getShowById(movieId).map {
                    if (it != null) {
                        DataMapper.mapDetailEntitiesToDomain(it)
                    } else {
                        DataMapper.mapDetailEntitiesToDomain(DataMapper.dummyDataEntity())
                    }
                }
            }

            override fun shouldFetch(data: ShowModel?): Boolean {
                return data?.genres1 == ""
            }

            override suspend fun createCall(): Flow<ApiResponse<MovieItem>> {
                return remoteDataSource.getMovieDetail(movieId)
            }

            override suspend fun saveCallResult(data: MovieItem) {
                val movie = DataMapper.mapMovieDetailResponseToEntities(data, category)
                localDataSource.insertAllShow(listOf(movie))
                localDataSource.updateShow(movie)
            }
        }.asFlow()
    }

    override fun getTvDetail(tvId: String, category: Int): Flow<Resource<ShowModel>> {
        return object : NetworkBoundResource<ShowModel, TvItem>() {
            public override fun loadFromDB(): Flow<ShowModel> {
                return localDataSource.getShowById(tvId).map {
                    if (it != null) {
                        DataMapper.mapDetailEntitiesToDomain(it)
                    } else {
                        DataMapper.mapDetailEntitiesToDomain(DataMapper.dummyDataEntity())
                    }
                }
            }

            override fun shouldFetch(data: ShowModel?): Boolean {
                return data?.genres1 == ""
            }

            override suspend fun createCall(): Flow<ApiResponse<TvItem>> {
                return remoteDataSource.getTvDetail(tvId)
            }

            override suspend fun saveCallResult(data: TvItem) {
                val tv = DataMapper.mapTvDetailResponseToEntities(data, category)
                localDataSource.insertAllShow(listOf(tv))
                localDataSource.updateShow(tv)
            }
        }.asFlow()
    }

    override fun getMovieCast(showId: String): Flow<Resource<List<CastModel>>> {
        return object : NetworkBoundResource<List<CastModel>, List<CastItem>>() {
            override fun loadFromDB(): Flow<List<CastModel>> {
                return localDataSource.getCastShowById(showId).map {
                    DataMapper.mapCastEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<CastModel>?): Boolean {
                return data == null || data.isEmpty()
            }

            override suspend fun createCall(): Flow<ApiResponse<List<CastItem>>> {
                return remoteDataSource.getMovieCast(showId)
            }

            override suspend fun saveCallResult(data: List<CastItem>) {
                val casts = DataMapper.mapCastResponseToEntities(data, showId)
                localDataSource.insertAllCastShow(casts)
                localDataSource.updateListCast(casts)
            }

        }.asFlow()
    }

    override fun getTvCast(showId: String): Flow<Resource<List<CastModel>>> {
        return object : NetworkBoundResource<List<CastModel>, List<CastItem>>() {
            override fun loadFromDB(): Flow<List<CastModel>> {
                return localDataSource.getCastShowById(showId).map {
                    DataMapper.mapCastEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<CastModel>?): Boolean {
                return data == null || data.isEmpty()
            }

            override suspend fun createCall(): Flow<ApiResponse<List<CastItem>>> {
                return remoteDataSource.getTvCast(showId)
            }

            override suspend fun saveCallResult(data: List<CastItem>) {
                val casts = DataMapper.mapCastResponseToEntities(data, showId)
                localDataSource.insertAllCastShow(casts)
            }

        }.asFlow()
    }

    override fun getAllFavorite(): Flow<List<ShowModel>> {
        return localDataSource.getAllFavorites().map {
            DataMapper.mapShowEntitiesToDomain(it)
        }
    }

    override suspend fun setFavorite(showModel: ShowModel, state: Boolean) {
        val showEntity = DataMapper.mapDomainToEntity(showModel)
        localDataSource.setFavorite(showEntity, state)
    }

    override fun searchShow(keyword: String): Flow<Resource<List<ShowModel>>> {
        return object :
            NetworkBoundDoubleResource<List<ShowModel>, List<MovieItem>, List<TvItem>>() {
            override fun loadFromDB(): Flow<List<ShowModel>> {
                return localDataSource.getMovieSearch("%$keyword%").map {
                    DataMapper.mapShowEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<ShowModel>?): Boolean {
                return true
            }

            override suspend fun createCall(): Flow<ApiResponse<List<MovieItem>>> {
                return remoteDataSource.searchMovie(keyword)
            }

            override suspend fun saveCallResult(data: List<MovieItem>) {
                val dataEntity = DataMapper.mapMovieResponseToEntities(data, (0..2).random())
                localDataSource.insertAllShow(dataEntity)
            }

            override suspend fun createCallSecond(): Flow<ApiResponse<List<TvItem>>> {
                return remoteDataSource.searchTv(keyword)
            }

            override suspend fun saveCallResultSecond(data: List<TvItem>) {
                val dataEntity = DataMapper.mapTvResponseToEntities(data, (0..2).random())
                localDataSource.insertAllShow(dataEntity)
            }

        }.asFlow()
    }

    override fun getAllSimilarShow(id: String, showType: Int): Flow<Resource<List<ShowModel>>> {
        return object : RemoteResource<List<ShowModel>, List<MovieItem>, List<TvItem>>(showType) {
            override fun createCall(): Flow<ApiResponse<List<MovieItem>>> {
                return remoteDataSource.getAllSimilarMovie(id)
            }

            override fun mappingCallResult(data: List<MovieItem>): Flow<List<ShowModel>> {
                return flow {
                    emit(
                        DataMapper.mapMovieResponseToDomain(data)
                    )
                }
            }

            override fun createCallSecond(): Flow<ApiResponse<List<TvItem>>> {
                return remoteDataSource.getAllSimilarTv(id)

            }

            override fun mappingCallResultSecond(data: List<TvItem>): Flow<List<ShowModel>> {
                return flow {
                    emit(
                        DataMapper.mapTvResponseToDomain(data)
                    )
                }
            }

            override fun emptyResult(): Flow<List<ShowModel>> {
                return flow { emit(emptyList()) }
            }


        }.asFlow()
    }

    override fun getDetailCastById(
        castId: String,
        showId: String,
        character: String
    ): Flow<Resource<CastModel>> {
        return object : NetworkBoundResource<CastModel, CastItem>() {
            override fun loadFromDB(): Flow<CastModel> {
                return localDataSource.getDetailCastById(castId).map {
                    DataMapper.mapDetailCastEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: CastModel?): Boolean {
                return true
            }

            override suspend fun createCall(): Flow<ApiResponse<CastItem>> {
                return remoteDataSource.getDetailCast(castId)
            }

            override suspend fun saveCallResult(data: CastItem) {
                val dataEntity = DataMapper.mapDetailCastResponseToEntities(data, showId, character)
                localDataSource.updateCast(dataEntity)
            }

        }.asFlow()
    }

    override fun getCastMovieOrTv(castId: String, showType: Int): Flow<Resource<List<ShowModel>>> {
        return object : RemoteResource<List<ShowModel>, List<MovieItem>, List<TvItem>>(showType) {
            override fun createCall(): Flow<ApiResponse<List<MovieItem>>> {
                return remoteDataSource.getFilmography(castId)
            }

            override fun mappingCallResult(data: List<MovieItem>): Flow<List<ShowModel>> {
                return flow {
                    emit(
                        DataMapper.mapMovieResponseToDomain(data)
                    )
                }
            }

            override fun createCallSecond(): Flow<ApiResponse<List<TvItem>>> {
                return remoteDataSource.getTvography(castId)

            }

            override fun mappingCallResultSecond(data: List<TvItem>): Flow<List<ShowModel>> {
                return flow {
                    emit(
                        DataMapper.mapTvResponseToDomain(data)
                    )
                }
            }

            override fun emptyResult(): Flow<List<ShowModel>> {
                return flow { emit(emptyList()) }
            }


        }.asFlow()
    }
}