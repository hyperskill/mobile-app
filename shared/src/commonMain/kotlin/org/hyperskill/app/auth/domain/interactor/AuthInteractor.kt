package org.hyperskill.app.auth.domain.interactor

import org.hyperskill.app.auth.domain.model.SocialAuthProvider
import org.hyperskill.app.auth.domain.repository.AuthRepository

class AuthInteractor(
    private val authRepository: AuthRepository
) {
    suspend fun authWithSocial(authCode: String, socialProvider: SocialAuthProvider): Result<Unit> =
        authRepository.authWithSocial(authCode, socialProvider)

    suspend fun authWithEmail(email: String, password: String): Result<Unit> =
        authRepository.authWithEmail(email, password)
}