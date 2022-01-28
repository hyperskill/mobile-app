package org.hyperskill.app.login.domain.interactor

import org.hyperskill.app.login.domain.repository.UserLoginRepository

class UserLoginInteractor(
    private val repository: UserLoginRepository
) {
    suspend fun loginUser(login: String, password: String) {
        repository.loginUser(login, password)
    }
}