package org.hyperskill.app.core.data.repository_cache

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class RepositoryCacheProxy<in Key : Any, Value : Any?>(
    private val cache: RepositoryCache<Key, Value>,
    private val loadValuesFromRemote: suspend (keys: List<Key>) -> Result<List<Value>>,
    private val getKeyFromValue: (Value) -> Key?
) {
    private val mutex = Mutex()

    suspend fun getValues(keys: List<Key>, forceLoadFromRemote: Boolean): Result<List<Value>> {
        if (keys.isEmpty()) return Result.success(emptyList())
        return mutex.withLock {
            if (keys.size == 1) {
                // fast route for single key request
                getSingleValue(keys.first(), forceLoadFromRemote)
            } else {
                getManyValues(keys, forceLoadFromRemote)
            }
        }
    }

    private suspend fun getSingleValue(key: Key, forceLoadFromRemote: Boolean): Result<List<Value>> =
        if (!forceLoadFromRemote && cache.containsKey(key)) {
            val cachedValue = cache[key]
            if (cachedValue != null) {
                Result.success(listOf(cachedValue))
            } else {
                loadValuesAndUpdateCache(listOf(key))
            }
        } else {
            loadValuesAndUpdateCache(listOf(key))
        }

    private suspend fun getManyValues(keys: List<Key>, forceLoadFromRemote: Boolean): Result<List<Value>> =
        if (!forceLoadFromRemote) {
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
                loadValuesAndUpdateCache(keysToLoadFromRemote)
                    .map { remoteValues ->
                        cachedValues + remoteValues
                    }
            }
        } else {
            loadValuesAndUpdateCache(keys)
        }

    private suspend fun loadValuesAndUpdateCache(keys: List<Key>): Result<List<Value>> =
        loadValuesFromRemote(keys).onSuccess { remoteValues ->
            remoteValues.forEach { value ->
                val key = getKeyFromValue(value)
                if (key != null) {
                    cache[key] = value
                }
            }
        }

    fun clearCache() {
        cache.clearCache()
    }
}