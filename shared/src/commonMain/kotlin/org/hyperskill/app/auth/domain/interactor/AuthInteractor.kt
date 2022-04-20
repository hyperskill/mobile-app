package org.hyperskill.app.auth.domain.interactor

import kotlinx.coroutines.flow.Flow
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.auth.domain.repository.AuthRepository

class AuthInteractor(
    private val authRepository: AuthRepository
) {
    fun observeUserDeauthorization(): Flow<UserDeauthorized> =
        authRepository.observeUserDeauthorization()

    suspend fun authWithCode(code: String): Result<Unit> =
        authRepository.authWithCode(code)

    suspend fun authWithEmail(email: String, password: String): Result<Unit> =
        authRepository.authWithEmail(email, password)
}