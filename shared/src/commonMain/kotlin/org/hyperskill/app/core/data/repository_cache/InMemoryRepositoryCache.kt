package org.hyperskill.app.core.data.repository_cache

class InMemoryRepositoryCache<Key : Any, Value : Any?> : RepositoryCache<Key, Value> {
    private val cache: MutableMap<Key, Value> = mutableMapOf()

    override fun get(key: Key): Value? =
        cache[key]

    override fun set(key: Key, value: Value) {
        cache[key] = value
    }

    override fun containsKey(key: Key): Boolean =
        cache.containsKey(key)

    override fun clearCache() {
        cache.clear()
    }
}