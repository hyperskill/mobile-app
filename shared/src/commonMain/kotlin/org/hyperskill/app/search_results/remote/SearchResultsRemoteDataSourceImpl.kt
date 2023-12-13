package org.hyperskill.app.search_results.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.search_results.data.source.SearchResultsRemoteDataSource
import org.hyperskill.app.search_results.remote.model.SearchResultsRequest
import org.hyperskill.app.search_results.remote.model.SearchResultsResponse

internal class SearchResultsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : SearchResultsRemoteDataSource {
    override suspend fun getSearchResults(request: SearchResultsRequest): Result<SearchResultsResponse> =
        kotlin.runCatching {
            httpClient
                .get("/api/search-results") {
                    contentType(ContentType.Application.Json)
                    request.parameters.forEach { (key, value) ->
                        parameter(key, value)
                    }
                }
                .body()
        }
}