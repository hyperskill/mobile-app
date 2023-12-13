package org.hyperskill.app.search.domain.interactor

import org.hyperskill.app.search_results.domain.repository.SearchResultsRepository
import org.hyperskill.app.search_results.domain.repository.getTopicSearchResults
import org.hyperskill.app.topics.domain.model.Topic
import org.hyperskill.app.topics.domain.repository.TopicsRepository

internal class SearchInteractor(
    private val searchResultsRepository: SearchResultsRepository,
    private val topicsRepository: TopicsRepository
) {
    suspend fun searchTopics(
        query: String,
        pageSize: Int = 20,
        page: Int = 1
    ): Result<List<Topic>> =
        searchResultsRepository
            .getTopicSearchResults(query = query, pageSize = pageSize, page = page)
            .map { searchResults ->
                searchResults.map { it.targetId }
            }
            .fold(
                onSuccess = { topicsIds ->
                    topicsRepository.getTopics(topicsIds)
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
}