package com.carohg.zscalerchallenge.domain

import javax.inject.Inject

class SignInUseCaseImpl @Inject constructor (val signInRepository : SignInRepository): SignInUseCase {

    override suspend operator fun invoke(username: String, password: String) = signInRepository.signInToNetworking(username, password)
}