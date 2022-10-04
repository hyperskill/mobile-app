package org.hyperskill.app.user_storage.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.user_storage.data.source.UserStorageRemoteDataSource
import org.hyperskill.app.user_storage.domain.model.UserStorage
import org.hyperskill.app.user_storage.domain.model.UserStorageKey
import org.hyperskill.app.user_storage.domain.model.UserStorageValue
import org.hyperskill.app.user_storage.remote.model.UserStorageResponse

class UserStorageRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : UserStorageRemoteDataSource {
    override suspend fun getUserStorage(): Result<UserStorage> =
        kotlin.runCatching {
            httpClient
                .get("/api/user-storages") {
                    contentType(ContentType.Application.Json)
                }.body<UserStorageResponse>().userStorages.first()
        }

    override suspend fun updateUserStorage(key: UserStorageKey, value: UserStorageValue): Result<UserStorage> =
        kotlin.runCatching {
            httpClient
                .put("/api/user-storages/$key") {
                    contentType(ContentType.Application.Json)
                    setBody(value)
                }.body<UserStorageResponse>().userStorages.first()
        }
}