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
import org.hyperskill.app.comments.domain.model.Reaction
import org.hyperskill.app.comments.remote.model.CommentsResponse
import org.hyperskill.app.comments.remote.model.DiscussionsResponse
import org.hyperskill.app.comments.remote.model.LikesRequest
import org.hyperskill.app.comments.remote.model.LikesResponse
import org.hyperskill.app.comments.remote.model.ReactionsRequest
import org.hyperskill.app.comments.remote.model.ReactionsResponse

class CommentsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : CommentsRemoteDataSource {
    override suspend fun getDiscussions(
        targetType: String,
        targetId: Long,
        thread: String,
        ordering: String,
        isSpam: Boolean
    ): Result<List<Discussion>> =
        kotlin.runCatching {
            httpClient
                .get("/api/discussions") {
                    contentType(ContentType.Application.Json)
                    parameter("target_type", targetType)
                    parameter("target_id", targetId)
                    parameter("thread", thread)
                    parameter("ordering", ordering)
                    parameter("is_spam", isSpam)
                }.body<DiscussionsResponse>().discussions
        }

    override suspend fun getCommentDetails(commentId: Long): Result<Comment> =
        kotlin.runCatching {
            httpClient
                .get("/api/comments/$commentId") {
                    contentType(ContentType.Application.Json)
                }.body<CommentsResponse>().comments.first()
        }

    override suspend fun createLike(
        subject: String,
        targetType: String,
        targetId: Long,
        value: Int
    ): Result<Like> =
        kotlin.runCatching {
            httpClient
                .post("/api/likes") {
                    contentType(ContentType.Application.Json)
                    setBody(LikesRequest(targetType, targetId, subject, value))
                }.body<LikesResponse>().likes.first()
        }

    override suspend fun createReaction(commentId: Long, shortName: String): Result<Reaction> =
        kotlin.runCatching {
            httpClient
                .post("/api/reactions") {
                    contentType(ContentType.Application.Json)
                    setBody(ReactionsRequest(commentId, shortName))
                }.body<ReactionsResponse>().reactions.first()
        }
}