package org.hyperskill.app.auth.data.source

import kotlinx.coroutines.flow.Flow
import org.hyperskill.app.auth.domain.model.UserDeauthorized

interface AuthRemoteDataSource {
    fun observeUserDeauthorization(): Flow<UserDeauthorized>
    suspend fun authWithCode(authCode: String): Result<Unit>
    suspend fun authWithEmail(email: String, password: String): Result<Unit>
}