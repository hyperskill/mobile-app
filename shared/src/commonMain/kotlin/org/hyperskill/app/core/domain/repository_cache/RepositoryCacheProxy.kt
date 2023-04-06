package org.hyperskill.app.core.domain.repository_cache

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * A proxy which controls loading values from remote or getting them from cache.
 * @param cache represents cache access interface.
 * @param [loadValuesFromRemote] represents a lambda that load values from remote by passed keys.
 * @param [getKeyFromValue] represents a lambda that returns key by passed value.
 */
class RepositoryCacheProxy<in Key : Comparable<Key>, Value : Any?>(
    private val cache: RepositoryCache<Key, Value>,
    private val loadValuesFromRemote: suspend (keys: List<Key>) -> Result<List<Value>>,
    private val getKeyFromValue: (Value) -> Key?
) {
    private val mutex = Mutex()

    /**
     * Fetch requested values from cache and load not cached values from remote by calling [loadValuesFromRemote].
     * Returned list is consists of cached and fresh remote values. It doesn't preserve values iteration order of the remote values.
     * It is safe to call this method in the concurrent environment.
     * @param keys describes keys of requested values.
     * @param forceLoadFromRemote marks necessity of force loading values from remote.
     * @return list of requested values or Throwable.
     */
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
                            (cachedValues + remoteValues).sortedBy(getKeyFromValue)
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