package org.hyperskill.app.auth.data.repository

import org.hyperskill.app.auth.data.source.AuthCacheDataSource
import org.hyperskill.app.auth.data.source.AuthRemoteDataSource
import org.hyperskill.app.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authCacheDataSource: AuthCacheDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override suspend fun isAuthorized(): Result<Boolean> =
        authCacheDataSource.isAuthorized()

    override suspend fun authWithCode(authCode: String): Result<Unit> =
        authRemoteDataSource.authWithCode(authCode)
}