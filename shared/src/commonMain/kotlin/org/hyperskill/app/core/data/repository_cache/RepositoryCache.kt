package org.hyperskill.app.core.data.repository_cache

interface RepositoryCache<Key : Any, Value : Any?> {
    fun getAll(keys: List<Key>): List<Value>
    fun putAll(map: Map<Key, Value>)
    fun clearCache()
}