package org.hyperskill.app.comments.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.hyperskill.app.comments.data.source.CommentsRemoteDataSource
import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.remote.model.CommentsResponse
import org.hyperskill.app.network.remote.parameterIds

class CommentsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : CommentsRemoteDataSource {
    override suspend fun getComment(commentId: Long): Result<Comment> =
        kotlin.runCatching {
            getComments(listOf(commentId))
                .map { it.first() }
                .getOrThrow()
        }

    // TODO: ALTAPPS-399/Shared-Implement-generic-GET-request-for-objects-by-ids-with-chunks
    override suspend fun getComments(commentsIds: List<Long>): Result<List<Comment>> =
        kotlin.runCatching {
            if (commentsIds.isEmpty()) {
                return Result.success(emptyList())
            }

            val chunkSize = 100
            val chunks = commentsIds.chunked(chunkSize)

            val requests: MutableList<Deferred<List<Comment>>> = mutableListOf()

            for (chunkIds in chunks) {
                val futureResult = httpClient.async {
                    httpClient.get("/api/comments") {
                        contentType(ContentType.Application.Json)
                        parameterIds(chunkIds)
                    }.body<CommentsResponse>().comments
                }
                requests.add(futureResult)
            }

            val responses = requests.awaitAll()

            return Result.success(responses.flatten())
        }
}