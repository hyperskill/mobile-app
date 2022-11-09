package org.hyperskill.app.comments.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.comments.data.source.CommentsRemoteDataSource
import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.model.Discussion
import org.hyperskill.app.comments.domain.model.Like
import org.hyperskill.app.comments.remote.model.CommentsResponse
import org.hyperskill.app.comments.remote.model.DiscussionsResponse
import org.hyperskill.app.comments.remote.model.LikesRequest
import org.hyperskill.app.comments.remote.model.LikesResponse

class CommentsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : CommentsRemoteDataSource {
    override suspend fun getDiscussions(
        targetType: String,
        targetID: Long,
        thread: String,
        ordering: String,
        isSpam: Boolean
    ): Result<List<Discussion>> =
        kotlin.runCatching {
            httpClient
                .get("/api/discussions") {
                    contentType(ContentType.Application.Json)
                    parameter("target_type", targetType)
                    parameter("target_id", targetID)
                    parameter("thread", thread)
                    parameter("ordering", ordering)
                    parameter("is_spam", isSpam)
                }.body<DiscussionsResponse>().discussions
        }

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
        value: Int
    ): Result<Like> =
        kotlin.runCatching {
            httpClient
                .post("/api/likes") {
                    contentType(ContentType.Application.Json)
                    setBody(LikesRequest(targetType, targetID, subject, value))
                }.body<LikesResponse>().likes.first()
        }

    override suspend fun createReaction(commentID: Long, shortName: String) {
        kotlin.runCatching {
            httpClient
                .post("/api/reactions") {
                    contentType(ContentType.Application.Json)
                    parameter("comment", commentID)
                    parameter("short_name", shortName)
                }
        }
    }
}