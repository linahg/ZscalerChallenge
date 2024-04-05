package com.carohg.zscalerchallenge.di

import com.carohg.zscalerchallenge.domain.SignInRepository
import com.carohg.zscalerchallenge.data.SignInRepositoryImpl
import com.carohg.zscalerchallenge.domain.SignInUseCase
import com.carohg.zscalerchallenge.domain.SignInUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DomainModule {

    @Provides
    fun provideSignInRepository(): SignInRepository{
        return SignInRepositoryImpl()
    }

    @Provides
    fun provideSignInUseCase(repository: SignInRepository): SignInUseCase{
        return SignInUseCaseImpl(repository)
    }
}