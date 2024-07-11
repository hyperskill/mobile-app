package org.hyperskill.app.likes.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.contentType
import org.hyperskill.app.core.domain.model.ContentType
import org.hyperskill.app.likes.data.source.LikesRemoteDataSource
import org.hyperskill.app.likes.domain.model.Like
import org.hyperskill.app.likes.domain.model.LikeSubject
import org.hyperskill.app.likes.domain.model.LikeValue
import org.hyperskill.app.likes.remote.model.LikesRequest
import org.hyperskill.app.likes.remote.model.LikesResponse

class LikesRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : LikesRemoteDataSource {
    override suspend fun createLike(
        targetType: ContentType,
        targetId: Long,
        subject: LikeSubject,
        value: LikeValue
    ): Result<Like> =
        kotlin.runCatching {
            httpClient
                .post("/api/likes") {
                    contentType(io.ktor.http.ContentType.Application.Json)
                    setBody(LikesRequest(targetType, targetId, subject, value.value))
                }.body<LikesResponse>().likes.first()
        }
}