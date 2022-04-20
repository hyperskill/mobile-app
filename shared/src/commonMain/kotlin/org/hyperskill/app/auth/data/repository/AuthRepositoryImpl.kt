package org.hyperskill.app.auth.data.repository

import kotlinx.coroutines.flow.Flow
import org.hyperskill.app.auth.data.source.AuthCacheDataSource
import org.hyperskill.app.auth.data.source.AuthRemoteDataSource
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authCacheDataSource: AuthCacheDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override fun observeUserDeauthorization(): Flow<UserDeauthorized> =
        authRemoteDataSource.observeUserDeauthorization()

    override suspend fun isAuthorized(): Result<Boolean> =
        authCacheDataSource.isAuthorized()

    override suspend fun authWithCode(authCode: String): Result<Unit> =
        authRemoteDataSource.authWithCode(authCode)

    override suspend fun authWithEmail(email: String, password: String): Result<Unit> =
        authRemoteDataSource.authWithEmail(email, password)
}