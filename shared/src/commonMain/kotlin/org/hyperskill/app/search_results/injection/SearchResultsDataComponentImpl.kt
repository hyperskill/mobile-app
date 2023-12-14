package org.hyperskill.app.search_results.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.search_results.data.repository.SearchResultsRepositoryImpl
import org.hyperskill.app.search_results.data.source.SearchResultsRemoteDataSource
import org.hyperskill.app.search_results.domain.repository.SearchResultsRepository
import org.hyperskill.app.search_results.remote.SearchResultsRemoteDataSourceImpl

internal class SearchResultsDataComponentImpl(appGraph: AppGraph) : SearchResultsDataComponent {
    private val searchResultsRemoteDataSource: SearchResultsRemoteDataSource =
        SearchResultsRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    override val searchResultsRepository: SearchResultsRepository
        get() = SearchResultsRepositoryImpl(searchResultsRemoteDataSource)
}