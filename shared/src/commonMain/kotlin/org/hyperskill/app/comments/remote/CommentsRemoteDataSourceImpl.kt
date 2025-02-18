package org.hyperskill.app.comments.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.hyperskill.app.comments.data.source.CommentsRemoteDataSource
import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.remote.model.CommentsResponse
import org.hyperskill.app.network.remote.parameterIds

internal class CommentsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : CommentsRemoteDataSource {
    override suspend fun getComment(commentId: Long): Result<Comment> =
        runCatching {
            getComments(listOf(commentId))
                .map { it.first() }
                .getOrThrow()
        }

    // TODO: ALTAPPS-399/Shared-Implement-generic-GET-request-for-objects-by-ids-with-chunks
    override suspend fun getComments(commentsIds: List<Long>): Result<List<Comment>> =
        runCatching {
            if (commentsIds.isEmpty()) {
                return@runCatching emptyList()
            }

            val chunkSize = 100
            val chunks = commentsIds.chunked(chunkSize)

            coroutineScope {
                val requests = chunks.map { chunkIds ->
                    async {
                        httpClient.get("/api/comments") {
                            contentType(ContentType.Application.Json)
                            parameterIds(chunkIds)
                        }.body<CommentsResponse>().comments
                    }
                }
                requests.awaitAll().flatten()
            }
        }
}