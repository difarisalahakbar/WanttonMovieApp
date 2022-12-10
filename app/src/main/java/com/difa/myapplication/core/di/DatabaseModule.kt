package com.difa.myapplication.core.di

import android.content.Context
import androidx.room.Room
import com.difa.myapplication.core.data.source.local.room.CastDao
import com.difa.myapplication.core.data.source.local.room.ShowDao
import com.difa.myapplication.core.data.source.local.room.ShowDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): ShowDatabase =
        Room.databaseBuilder(context, ShowDatabase::class.java, "nonton.db")
            .fallbackToDestructiveMigration().build()

    @Provides
    fun providesShowDao(showDatabase: ShowDatabase): ShowDao = showDatabase.getShowDao()

    @Provides
    fun providesCastDao(showDatabase: ShowDatabase): CastDao = showDatabase.getCastDao()
}