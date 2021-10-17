package org.hyperskill.app.user_list.data.repository

import org.hyperskill.app.user_list.data.source.UserListRemoteDataSource
import org.hyperskill.app.user_list.domain.model.User
import org.hyperskill.app.user_list.domain.repository.UserListRepository

class UserListRepositoryImpl(
    private val userListRemoteDataSource: UserListRemoteDataSource
) : UserListRepository {
    override suspend fun getUsers(query: String): Result<List<User>> =
        userListRemoteDataSource.getUsers(query)
}