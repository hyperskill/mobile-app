package org.hyperskill.app.auth.domain.interactor

import kotlinx.coroutines.flow.Flow
import org.hyperskill.app.auth.domain.model.SocialAuthProvider
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.auth.domain.repository.AuthRepository

class AuthInteractor(
    private val authRepository: AuthRepository
) {
    fun observeUserDeauthorization(): Flow<UserDeauthorized> =
        authRepository.observeUserDeauthorization()

    suspend fun isAuthorized(): Result<Boolean> =
        authRepository.isAuthorized()

    suspend fun authWithSocial(authCode: String, idToken: String?, socialProvider: SocialAuthProvider): Result<Unit> =
        authRepository.authWithSocial(authCode, idToken, socialProvider)

    suspend fun authWithEmail(email: String, password: String): Result<Unit> =
        authRepository.authWithEmail(email, password)

    suspend fun clearCache() {
        authRepository.clearCache()
    }
}