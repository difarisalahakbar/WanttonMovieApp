package com.difa.myapplication.core.di

import com.difa.myapplication.core.data.ShowRepository
import com.difa.myapplication.core.domain.repository.IShowRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module(includes = [DatabaseModule::class, RemoteModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(showRepository: ShowRepository): IShowRepository

}