package org.hyperskill.app.comments.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.comments.data.source.CommentsRemoteDataSource
import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.remote.model.CommentsResponse

class CommentsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : CommentsRemoteDataSource {
    override suspend fun getCommentDetails(commentID: Long): Result<Comment> =
        kotlin.runCatching {
            httpClient
                .get("/api/comments/$commentID") {
                    contentType(ContentType.Application.Json)
                }.body<CommentsResponse>().comments.first()
        }

    override suspend fun createLike(
        subject: String,
        targetType: String,
        targetID: Long,
        value: Long
    ) {
        kotlin.runCatching {
            httpClient
                .post("/api/likes") {
                    contentType(ContentType.Application.Json)
                    parameter("subject", subject)
                    parameter("target_type", targetType)
                    parameter("target_id", targetID)
                    parameter("value", value)
                }
        }
    }
}