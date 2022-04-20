package org.hyperskill.app.auth.data.source

import kotlinx.coroutines.flow.Flow
import org.hyperskill.app.auth.domain.model.SocialAuthProvider
import org.hyperskill.app.auth.domain.model.UserDeauthorized

interface AuthRemoteDataSource {
    fun observeUserDeauthorization(): Flow<UserDeauthorized>
    suspend fun authWithSocial(authCode: String, socialProvider: SocialAuthProvider): Result<Unit>
    suspend fun authWithEmail(email: String, password: String): Result<Unit>
}