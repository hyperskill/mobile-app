package org.hyperskill.app.user_list.domain.interactor

import org.hyperskill.app.user_list.domain.model.User
import org.hyperskill.app.user_list.domain.repository.UserListRepository

class UserListInteractor(
    private val userListRepository: UserListRepository
) {
    suspend fun getUsers(query: String): Result<List<User>> =
        userListRepository.getUsers(query)
}