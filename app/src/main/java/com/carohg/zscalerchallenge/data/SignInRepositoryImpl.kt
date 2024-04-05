package com.carohg.zscalerchallenge.data

import com.carohg.zscalerchallenge.domain.SignInRepository
import com.carohg.zscalerchallenge.presentation.SignInUiState
import com.carohg.zscalerchallenge.utils.SignInErrorCode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class SignInRepositoryImpl: SignInRepository {

    override suspend fun signInToNetworking(username: String, password: String) = flow {
        emit(SignInUiState.Loading)
        delay(2000)
        val defaultUser = FakeUser() /** Credentials : username = Carolina, password = abc123**/
        if(defaultUser == FakeUser(username, password))
            emit(SignInUiState.Success(message = "Sign in successfully!"))
        else
            emit(SignInUiState.Error(code = SignInErrorCode.SIGNIN_ERROR  ,message = "Wrong credentials!"))
    }

}