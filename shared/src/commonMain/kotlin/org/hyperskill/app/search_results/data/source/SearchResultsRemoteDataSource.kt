package org.hyperskill.app.search_results.data.source

import org.hyperskill.app.search_results.remote.model.SearchResultsRequest
import org.hyperskill.app.search_results.remote.model.SearchResultsResponse

interface SearchResultsRemoteDataSource {
    suspend fun getSearchResults(request: SearchResultsRequest): Result<SearchResultsResponse>
}