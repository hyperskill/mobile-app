package org.hyperskill.app.search_results.remote.model

class SearchResultsRequest(
    query: String,
    pageSize: Int,
    page: Int
) {
    companion object {
        private const val PARAM_QUERY = "query"
        private const val PARAM_PAGE_SIZE = "page_size"
        private const val PARAM_PAGE = "page"
    }

    val parameters: Map<String, Any> =
        mapOf(
            PARAM_QUERY to query,
            PARAM_PAGE_SIZE to pageSize,
            PARAM_PAGE to page
        )
}