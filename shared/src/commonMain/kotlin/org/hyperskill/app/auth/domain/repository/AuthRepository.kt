package org.hyperskill.app.auth.domain.repository

import org.hyperskill.app.auth.domain.model.SocialAuthProvider

interface AuthRepository {
    suspend fun isAuthorized(): Result<Boolean>
    suspend fun authWithSocial(authCode: String, socialProvider: SocialAuthProvider): Result<Unit>
    suspend fun authWithEmail(email: String, password: String): Result<Unit>
}