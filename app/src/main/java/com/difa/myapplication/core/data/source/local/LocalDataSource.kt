package com.difa.myapplication.core.data.source.local

import com.difa.myapplication.core.data.source.local.entity.CastEntity
import com.difa.myapplication.core.data.source.local.entity.ShowEntity
import com.difa.myapplication.core.data.source.local.room.CastDao
import com.difa.myapplication.core.data.source.local.room.ShowDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val showDao: ShowDao,
    private val castDao: CastDao
) {
    fun getAllMovie(category: Int, limit: Int): Flow<List<ShowEntity>> = showDao.getAllMovie(category, limit)

    fun getAllTv(category: Int, limit: Int): Flow<List<ShowEntity>> = showDao.getAllTv(category, limit)

    fun getShowById(showId: String): Flow<ShowEntity?> = showDao.getShowById(showId)

    fun getCastShowById(showId: String) : Flow<List<CastEntity>> = castDao.getCastShowById(showId)

    fun getAllFavorites(): Flow<List<ShowEntity>> = showDao.getAllFavorite()

    fun getMovieSearch(keyword: String): Flow<List<ShowEntity>> = showDao.getMovieSearch(keyword)

    suspend fun updateShow(showEntity: ShowEntity) = showDao.updateShow(showEntity)

    suspend fun insertAllShow(shows: List<ShowEntity>) = showDao.insertAllShow(shows)

    suspend fun insertAllCastShow(casts: List<CastEntity>) = castDao.insertAllCastShow(casts)

    suspend fun setFavorite(showEntity: ShowEntity, state: Boolean){
        showEntity.isFavorite = state
        showDao.updateShow(showEntity)
    }

}