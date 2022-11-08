package org.hyperskill.app.comments.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.comments.data.source.CommentsRemoteDataSource
import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.remote.model.CommentsResponse

class CommentsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : CommentsRemoteDataSource {
    override suspend fun getComment(commentId: Long): Result<Comment> =
        kotlin.runCatching {
            getComments(listOf(commentId))
                .map { it.comments.first() }
                .getOrThrow()
        }

    override suspend fun getComments(commentsIds: List<Long>): Result<CommentsResponse> =
        kotlin.runCatching {
            httpClient
                .get("/api/comments") {
                    contentType(ContentType.Application.Json)
                    parameter("ids", commentsIds.joinToString(separator = ","))
                }.body()
        }
}