package org.hyperskill.app.login.data.source

interface UserLoginRemoteDataSource {
    suspend fun loginUser(login: String, password: String)
}