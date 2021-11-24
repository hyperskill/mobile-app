package org.hyperskill.app.login.data.repository

import org.hyperskill.app.login.domain.repository.UserLoginRepository
import org.hyperskill.app.login.remote.UserLoginRemoteDataSourceImpl

class UserLoginRepositoryImpl(
    private val remoteDataSourceImpl: UserLoginRemoteDataSourceImpl
) : UserLoginRepository {
    override suspend fun loginUser(login: String, password: String) {
        remoteDataSourceImpl.loginUser(login, password)
    }
}