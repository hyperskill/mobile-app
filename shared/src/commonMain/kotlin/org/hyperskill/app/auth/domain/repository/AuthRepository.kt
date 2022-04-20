package org.hyperskill.app.auth.domain.repository

import kotlinx.coroutines.flow.Flow
import org.hyperskill.app.auth.domain.model.UserDeauthorized

interface AuthRepository {
    fun observeUserDeauthorization(): Flow<UserDeauthorized>
    suspend fun isAuthorized(): Result<Boolean>
    suspend fun authWithCode(authCode: String): Result<Unit>
    suspend fun authWithEmail(email: String, password: String): Result<Unit>
}