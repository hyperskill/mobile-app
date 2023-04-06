package org.hyperskill.app.core.domain.repository_cache

import org.hyperskill.app.core.data.repository_cache.InMemoryRepositoryCache

/**
 * Represents a map-based cache to access values by key.
 * Default implementation is [InMemoryRepositoryCache].
 */
interface RepositoryCache<Key : Any, Value : Any?> {
    fun getAll(keys: List<Key>): List<Value>
    fun putAll(map: Map<Key, Value>)
    fun clearCache()
}