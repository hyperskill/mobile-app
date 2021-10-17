package org.hyperskill.app.user_list.data.source

import org.hyperskill.app.user_list.domain.model.User

interface UserListRemoteDataSource {
    suspend fun getUsers(query: String): Result<List<User>>
}