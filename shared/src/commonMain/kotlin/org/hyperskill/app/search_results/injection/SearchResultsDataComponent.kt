package org.hyperskill.app.search_results.injection

import org.hyperskill.app.search_results.domain.repository.SearchResultsRepository

interface SearchResultsDataComponent {
    val searchResultsRepository: SearchResultsRepository
}