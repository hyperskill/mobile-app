package org.hyperskill.app.auth.domain.interactor

import org.hyperskill.app.auth.domain.repository.AuthRepository

class AuthInteractor(
    private val authRepository: AuthRepository
) {
    suspend fun authWithCode(code: String): Result<Unit> =
        authRepository.authWithCode(code)

    suspend fun authWithEmail(email: String, password: String): Result<Unit> =
        authRepository.authWithEmail(email, password)
}