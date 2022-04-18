package org.hyperskill.app.auth.domain.interactor

import org.hyperskill.app.auth.domain.repository.AuthRepository

class AuthInteractor(
    private val authRepository: AuthRepository
) {
    suspend fun authWithSocialToken(authCode: String, providerName: String): Result<Unit> =
        authRepository.authWithSocialToken(authCode, providerName)

    suspend fun authWithCode(authCode: String): Result<Unit> =
        authRepository.authWithCode(authCode)
}