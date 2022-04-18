package org.hyperskill.app.auth.data.source

interface AuthRemoteDataSource {
    suspend fun authWithSocialToken(authCode: String, providerName: String): Result<Unit>
    suspend fun authWithCode(authCode: String): Result<Unit>
}