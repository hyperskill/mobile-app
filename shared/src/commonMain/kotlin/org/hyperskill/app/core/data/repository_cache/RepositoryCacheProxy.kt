package org.hyperskill.app.core.data.repository_cache

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class RepositoryCacheProxy<in Key : Any, Value : Any?>(
    private val cache: RepositoryCache<Key, Value> =
        InMemoryRepositoryCache(),
    private val loadValuesFromRemote: suspend (keys: List<Key>) -> Result<List<Value>>,
    private val getKey: (Value) -> Key?
) {
    private val mutex = Mutex()

    suspend fun getValues(keys: List<Key>, force: Boolean): Result<List<Value>> {
        if (keys.isEmpty()) return Result.success(emptyList())
        return mutex.withLock {
            if (keys.size == 1) {
                // fast route for single key request
                getSingleValue(keys.first(), force)
            } else {
                getManyValues(keys, force)
            }
        }
    }

    private suspend fun getSingleValue(key: Key, force: Boolean): Result<List<Value>> =
        if (!force && cache.containsKey(key)) {
            val cachedValue = cache[key]
            if (cachedValue != null) {
                Result.success(listOf(cachedValue))
            } else {
                loadManyValuesInternal(listOf(key))
            }
        } else {
            loadManyValuesInternal(listOf(key))
        }

    private suspend fun getManyValues(keys: List<Key>, force: Boolean): Result<List<Value>> =
        if (!force) {
            val keysToLoadFromRemote: MutableList<Key> = mutableListOf()
            val cachedValues: MutableList<Value> = mutableListOf()

            keys.forEach { key ->
                if (cache.containsKey(key)) {
                    val cachedValue = cache[key]
                    if (cachedValue != null) {
                        cachedValues.add(cachedValue)
                    } else {
                        keysToLoadFromRemote.add(key)
                    }
                } else {
                    keysToLoadFromRemote.add(key)
                }
            }

            if (keysToLoadFromRemote.isEmpty()) {
                Result.success(cachedValues)
            } else {
                loadManyValuesInternal(keysToLoadFromRemote)
                    .map { remoteValues ->
                        cachedValues + remoteValues
                    }
            }
        } else {
            loadManyValuesInternal(keys)
        }

    private suspend fun loadManyValuesInternal(keys: List<Key>): Result<List<Value>> =
        loadValuesFromRemote(keys).onSuccess { remoteValues ->
            remoteValues.forEach { value ->
                val key = getKey(value)
                if (key != null) {
                    cache[key] = value
                }
            }
        }

    fun clear() {
        cache.clear()
    }
}