package org.hyperskill.app.auth.data.source

interface AuthRemoteDataSource {
    suspend fun authWithCode(authCode: String): Result<Unit>
    suspend fun authWithEmail(email: String, password: String): Result<Unit>
}