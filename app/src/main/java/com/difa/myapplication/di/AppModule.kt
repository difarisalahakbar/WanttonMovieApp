package com.difa.myapplication.di

import com.difa.myapplication.core.domain.usecase.ShowUseCase
import com.difa.myapplication.core.domain.usecase.ShowsInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideUseCase(showsInteractor: ShowsInteractor): ShowUseCase
}