package org.hyperskill.app.analytic.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import org.hyperskill.app.analytic.data.source.AnalyticHyperskillRemoteDataSource
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.remote.exception.AnalyticHyperskillResponseException
import org.hyperskill.app.analytic.remote.model.AnalyticHyperskillRequest

class AnalyticHyperskillRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : AnalyticHyperskillRemoteDataSource {
    override suspend fun flushEvents(events: List<AnalyticEvent>): Result<Unit> =
        kotlin.runCatching {
            if (events.isEmpty()) {
                return Result.success(Unit)
            }

            val httpResponse = httpClient
                .post("/api/frontend-events") {
                    contentType(ContentType.Application.Json)
                    setBody(AnalyticHyperskillRequest(events).toJsonElement())
                }

            if (httpResponse.status.isSuccess()) {
                return Result.success(Unit)
            } else {
                throw AnalyticHyperskillResponseException(httpResponse, httpResponse.bodyAsText())
            }
        }
}