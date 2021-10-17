package org.hyperskill.app.user_list.domain.repository

import org.hyperskill.app.user_list.domain.model.User

interface UserListRepository {
    suspend fun getUsers(query: String): Result<List<User>>
}