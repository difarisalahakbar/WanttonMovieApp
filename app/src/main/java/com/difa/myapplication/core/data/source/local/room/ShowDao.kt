package com.difa.myapplication.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.difa.myapplication.core.data.source.local.entity.ShowEntity
import com.difa.myapplication.core.utils.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShowDao {

    @Query("SELECT * FROM table_show WHERE showType = $MOVIE and category = :category LIMIT :limit")
    fun getAllMovie(category: Int, limit: Int): Flow<List<ShowEntity>>

    @Query("SELECT * FROM table_show WHERE showType = $TV_SERIES and category = :category LIMIT :limit")
    fun getAllTv(category: Int, limit: Int): Flow<List<ShowEntity>>

    @Query("SELECT * FROM table_show WHERE id = :showId")
    fun getShowById(showId: String): Flow<ShowEntity>

    @Query("SELECT * FROM table_show WHERE isFavorite = 1")
    fun getAllFavorite(): Flow<List<ShowEntity>>

    @Query("SELECT * FROM table_show WHERE title LIKE :keyword")
    fun getMovieSearch(keyword: String): Flow<List<ShowEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllShow(shows: List<ShowEntity>)

    @Update
    suspend fun updateShow(show: ShowEntity)

    @Query("DELETE FROM table_show WHERE showType = :showType and category = :category")
    suspend fun deleteAll(showType: Int, category: Int)
}