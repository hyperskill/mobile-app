package org.hyperskill.app.reactions.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.reactions.data.source.ReactionsRemoteDataSource
import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.reactions.remote.model.ReactionsRequest
import org.hyperskill.app.reactions.remote.model.ReactionsResponse

class ReactionsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : ReactionsRemoteDataSource {
    override suspend fun createReaction(commentId: Long, reaction: ReactionType): Result<ReactionsResponse> =
        kotlin.runCatching {
            httpClient
                .post("/api/reactions") {
                    contentType(ContentType.Application.Json)
                    setBody(ReactionsRequest(commentId, reaction.shortName))
                }.body()
        }
}