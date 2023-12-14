package org.hyperskill.app.search_results.data.repository

import org.hyperskill.app.search_results.data.source.SearchResultsRemoteDataSource
import org.hyperskill.app.search_results.domain.repository.SearchResultsRepository
import org.hyperskill.app.search_results.remote.model.SearchResultsRequest
import org.hyperskill.app.search_results.remote.model.SearchResultsResponse

internal class SearchResultsRepositoryImpl(
    private val searchResultsRemoteDataSource: SearchResultsRemoteDataSource
) : SearchResultsRepository {
    override suspend fun getSearchResults(request: SearchResultsRequest): Result<SearchResultsResponse> =
        searchResultsRemoteDataSource.getSearchResults(request)
}