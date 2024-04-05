package com.carohg.zscalerchallenge.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carohg.zscalerchallenge.domain.SignInUseCase
import com.carohg.zscalerchallenge.utils.SignInErrorCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(val signInUseCase : SignInUseCase): ViewModel() {

    private var _result = MutableStateFlow<SignInUiState>(SignInUiState.Initial)
    val result =  _result.asStateFlow()

    fun signIn(username: String, password: String){
        viewModelScope.launch{
            if(username.isNullOrBlank() && password.isNullOrBlank())
                _result.value = SignInUiState.Error(SignInErrorCode.EMPTY_FIELDS)
            else if(password.isNullOrBlank())
                _result.value = SignInUiState.Error(SignInErrorCode.EMPTY_PASSWORD)
            else if(username.isNullOrBlank())
                _result.value = SignInUiState.Error(SignInErrorCode.EMPTY_USER)
            else {
                signInUseCase.invoke(username, password).collect{
                    when(it){
                        is SignInUiState.Error -> _result.value = SignInUiState.Error(it.code, it.message)
                        SignInUiState.Loading -> _result.value = SignInUiState.Loading
                        is SignInUiState.Success -> _result.value = SignInUiState.Success(it.message)
                        SignInUiState.Initial -> _result.value = SignInUiState.Initial
                    }
                }

            }
        }
    }

    fun setInitial(){
        viewModelScope.launch {
            _result.value = SignInUiState.Initial
        }
    }
}