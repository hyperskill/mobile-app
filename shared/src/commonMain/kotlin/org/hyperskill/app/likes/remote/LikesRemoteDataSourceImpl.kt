package org.hyperskill.app.likes.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.likes.data.source.LikesRemoteDataSource
import org.hyperskill.app.likes.domain.model.LikeSubject
import org.hyperskill.app.likes.domain.model.LikeValue
import org.hyperskill.app.likes.remote.model.LikesRequest
import org.hyperskill.app.likes.remote.model.LikesResponse

class LikesRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : LikesRemoteDataSource {
    override suspend fun createLike(
        targetType: String,
        targetId: Long,
        subject: LikeSubject,
        value: LikeValue
    ): Result<LikesResponse> =
        kotlin.runCatching {
            httpClient
                .post("/api/likes") {
                    contentType(ContentType.Application.Json)
                    setBody(LikesRequest(targetType, targetId, subject, value.value))
                }.body()
        }
}