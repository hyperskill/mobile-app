package org.hyperskill.app.search_results.domain.repository

import org.hyperskill.app.search_results.domain.model.SearchResult
import org.hyperskill.app.search_results.domain.model.SearchResultTargetType
import org.hyperskill.app.search_results.remote.model.SearchResultsRequest
import org.hyperskill.app.search_results.remote.model.SearchResultsResponse

interface SearchResultsRepository {
    suspend fun getSearchResults(request: SearchResultsRequest): Result<SearchResultsResponse>
}

suspend fun SearchResultsRepository.getTopicSearchResults(
    query: String,
    pageSize: Int = 20,
    page: Int = 1
): Result<List<SearchResult>> =
    getSearchResults(
        SearchResultsRequest(
            query = query,
            pageSize = pageSize,
            page = page
        )
    ).map { response: SearchResultsResponse ->
        response.searchResults.filter { it.targetType == SearchResultTargetType.TOPIC }
    }