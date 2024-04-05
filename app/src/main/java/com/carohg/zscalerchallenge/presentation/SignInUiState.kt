package com.carohg.zscalerchallenge.presentation

import com.carohg.zscalerchallenge.utils.SignInErrorCode

sealed class SignInUiState {
    data object Initial: SignInUiState()
    data object Loading: SignInUiState()
    data class Success(val message: String): SignInUiState()
    data class Error(val code: SignInErrorCode, val message: String? = null): SignInUiState()

}