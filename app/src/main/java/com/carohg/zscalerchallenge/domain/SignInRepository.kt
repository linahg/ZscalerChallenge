package com.carohg.zscalerchallenge.domain

import com.carohg.zscalerchallenge.presentation.SignInUiState
import kotlinx.coroutines.flow.Flow

interface SignInRepository {
    suspend fun signInToNetworking(username: String, password: String): Flow<SignInUiState>
}