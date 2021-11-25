package org.hyperskill.app.user_list.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.launch
import org.hyperskill.app.user_list.data.source.UserListRemoteDataSource
import org.hyperskill.app.user_list.domain.model.User
import org.hyperskill.app.user_list.remote.model.UsersResponse

class UserListRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : UserListRemoteDataSource {
    override suspend fun getUsers(query: String): Result<List<User>> =
        kotlin.runCatching {
            httpClient
                .get<UsersResponse>("https://api.github.com/search/users?q=$query&page=1&per_page=20")
                .items
        }
}