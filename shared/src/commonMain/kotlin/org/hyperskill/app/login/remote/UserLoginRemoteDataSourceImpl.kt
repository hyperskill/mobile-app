package org.hyperskill.app.login.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Cookie
import io.ktor.http.setCookie
import org.hyperskill.app.login.data.source.UserLoginRemoteDataSource
import org.hyperskill.app.user_list.remote.model.UsersResponse

class UserLoginRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : UserLoginRemoteDataSource {
    override suspend fun loginUser(login: String, password: String) {
        kotlin.runCatching {
            val response = httpClient
                .get<UsersResponse>("https://api.github.com/search/users?q=kek&page=1&per_page=20")
                .items

            httpClient.post<UsersResponse>("https://api.github.com/search/users?q=kek&page=1&per_page=20") {
                this.body
            }
        }
        return
    }

    suspend fun getCsrfToken(): Result<List<Cookie>> =
        kotlin.runCatching {
            (httpClient.get("https://hyperskill.org") as HttpResponse).setCookie()
        }
}