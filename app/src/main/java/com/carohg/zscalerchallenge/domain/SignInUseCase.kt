package com.carohg.zscalerchallenge.domain

import com.carohg.zscalerchallenge.presentation.SignInUiState
import kotlinx.coroutines.flow.Flow

interface SignInUseCase {
    suspend fun invoke (username: String, password: String): Flow<SignInUiState>
}