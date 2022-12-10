package com.difa.myapplication.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.difa.myapplication.core.data.source.local.entity.CastEntity
import com.difa.myapplication.core.data.source.local.entity.ShowEntity

@Database(entities = [ShowEntity::class, CastEntity::class], version = 1, exportSchema = false)
abstract class ShowDatabase : RoomDatabase() {

    abstract fun getShowDao(): ShowDao

    abstract fun getCastDao(): CastDao

}