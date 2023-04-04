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
            if (!forceLoadFromRemote) {
                val cachedValues = cache.getAll(keys)
                val cachedKeys = cachedValues.mapNotNull(getKeyFromValue)
                val keysToLoadFromRemote = keys.subtract(cachedKeys.toSet())
                if (keysToLoadFromRemote.isEmpty()) {
                    Result.success(cachedValues)
                } else {
                    loadValuesAndUpdateCache(keysToLoadFromRemote.toList())
                        .map { remoteValues ->
                            cachedValues + remoteValues
                        }
                }
            } else {
                loadValuesAndUpdateCache(keys)
            }
        }
    }

    private suspend fun loadValuesAndUpdateCache(keys: List<Key>): Result<List<Value>> =
        loadValuesFromRemote(keys).onSuccess { remoteValues ->
            val keyToRemoteValueMap: Map<Key, Value> = buildMap(keys.size) {
                remoteValues.forEach { value ->
                    val key = getKeyFromValue(value)
                    if (key != null) {
                        set(key, value)
                    }
                }
            }
            cache.putAll(keyToRemoteValueMap)
        }

    fun clearCache() {
        cache.clearCache()
    }
}