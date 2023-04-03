package org.hyperskill.app.core.data.repository_cache

interface RepositoryCache<Key : Any, Value : Any?> {
    operator fun get(key: Key): Value?
    operator fun set(key: Key, value: Value)
    fun containsKey(key: Key): Boolean
    fun clear()
}