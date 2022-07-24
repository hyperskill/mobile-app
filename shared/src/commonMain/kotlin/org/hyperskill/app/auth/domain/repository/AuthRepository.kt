package org.hyperskill.app.auth.domain.repository

import kotlinx.coroutines.flow.Flow
import org.hyperskill.app.auth.domain.model.SocialAuthProvider
import org.hyperskill.app.auth.domain.model.UserDeauthorized

interface AuthRepository {
    fun observeUserDeauthorization(): Flow<UserDeauthorized>
    suspend fun isAuthorized(): Result<Boolean>
    suspend fun authWithSocial(authCode: String, socialProvider: SocialAuthProvider): Result<Unit>
    suspend fun authWithEmail(email: String, password: String): Result<Unit>
    suspend fun clearCache()
}