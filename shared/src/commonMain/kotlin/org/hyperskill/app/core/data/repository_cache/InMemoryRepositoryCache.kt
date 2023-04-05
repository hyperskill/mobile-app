package org.hyperskill.app.core.data.repository_cache

import org.hyperskill.app.core.domain.repository_cache.RepositoryCache

/**
 * Represent an in-memory cache based on [MutableMap].
 * It is not safe to use it in a concurrent environment without additional synchronization.
 */
class InMemoryRepositoryCache<Key : Any, Value : Any?> : RepositoryCache<Key, Value> {
    private val cache: MutableMap<Key, Value> = mutableMapOf()

    override fun getAll(keys: List<Key>): List<Value> =
        keys.mapNotNull {  key ->
            cache[key]
        }

    override fun putAll(map: Map<Key, Value>) {
        cache.putAll(map)
    }

    override fun clearCache() {
        cache.clear()
    }
}