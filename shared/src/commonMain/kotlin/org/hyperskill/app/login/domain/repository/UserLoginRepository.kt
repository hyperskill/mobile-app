package org.hyperskill.app.login.domain.repository

interface UserLoginRepository {
    suspend fun loginUser(login: String, password: String)
}