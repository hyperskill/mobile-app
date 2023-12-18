package org.hyperskill.app.search_results.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.search_results.domain.model.SearchResult

@Serializable
class SearchResultsResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("search-results")
    val searchResults: List<SearchResult>
) : MetaResponse