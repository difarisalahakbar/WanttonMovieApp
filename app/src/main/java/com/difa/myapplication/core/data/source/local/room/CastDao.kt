package com.difa.myapplication.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.difa.myapplication.core.data.source.local.entity.CastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CastDao {

    @Query("SELECT * FROM table_cast WHERE id = :showId")
    fun getCastShowById(showId: String): Flow<List<CastEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllCastShow(casts: List<CastEntity>)
}