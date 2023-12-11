package org.hyperskill.search_results.remote.model

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.network.injection.NetworkModule
import org.hyperskill.app.search_results.domain.model.SearchResult
import org.hyperskill.app.search_results.domain.model.SearchResultTargetType
import org.hyperskill.app.search_results.remote.model.SearchResultsResponse

class SearchResultsResponseDeserializationTest {
    companion object {
        private val TEST_JSON_STRING = """
{
    "meta": {
        "page": 1,
        "has_next": true,
        "has_previous": false
    },
    "search-results": [
        {
            "target_type": "topic",
            "target_id": 22,
            "position": 1,
            "score": 79.00877
        },
        {
            "target_type": "topic",
            "target_id": 488,
            "position": 2,
            "score": 77.70255
        },
        {
            "target_type": "project",
            "target_id": 353,
            "position": 54,
            "score": 39.685966
        }
    ]
}
        """.trimIndent()
    }

    @Test
    fun `Test SearchResultsResponse deserialization`() {
        val json = NetworkModule.provideJson()
        val expected = SearchResultsResponse(
            meta = Meta(
                page = 1,
                hasNext = true,
                hasPrevious = false
            ),
            searchResults = listOf(
                SearchResult(
                    targetType = SearchResultTargetType.TOPIC,
                    targetId = 22
                ),
                SearchResult(
                    targetType = SearchResultTargetType.TOPIC,
                    targetId = 488
                ),
                SearchResult(
                    targetType = SearchResultTargetType.UNKNOWN,
                    targetId = 353
                )
            )
        )
        val decodedObject = json.decodeFromString(SearchResultsResponse.serializer(), TEST_JSON_STRING)
        assertEquals(expected.meta, decodedObject.meta)
        assertEquals(expected.searchResults, decodedObject.searchResults)
    }
}