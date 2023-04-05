package org.hyperskill

import kotlinx.coroutines.runBlocking
import org.hyperskill.app.core.data.repository_cache.InMemoryRepositoryCache
import org.hyperskill.app.core.domain.repository_cache.RepositoryCache
import org.hyperskill.app.core.domain.repository_cache.RepositoryCacheProxy
import kotlin.test.Test
import kotlin.test.assertEquals

class RepositoryCacheProxyTest {

    @Test
    fun `Fetched values should be written into cache`() {
        val keys = listOf(1, 2, 3)
        val expectedValues = listOf("1", "2", "3")

        val cache = InMemoryRepositoryCache<Int, String>()
        val cacheProxy = RepositoryCacheProxy(
            cache = cache,
            loadValuesFromRemote = {
                Result.success(expectedValues)
            },
            getKeyFromValue = { it.toInt() }
        )

        runBlocking {
            cacheProxy.getValues(keys, false)
            assertEquals(expectedValues, cache.getAll(keys))
        }
    }

    @Test
    fun `loadValuesFromRemote should not be called if cache contains required value`() {
        val keys = listOf(1, 2, 3)
        val expectedValues = listOf("1", "2", "3")

        val cache = InMemoryRepositoryCache<Int, String>().apply {
            putAll(
                keys.associateWith { key -> expectedValues[key - 1] }
            )
        }

        val cacheProxy = RepositoryCacheProxy(
            cache = cache,
            loadValuesFromRemote = {
                Result.failure(
                    IllegalStateException("loadFromRemoteValues should not be called")
                )
            },
            getKeyFromValue = { it.toInt() }
        )

        runBlocking {
            val result = cacheProxy.getValues(keys, false).getOrThrow()
            repeat(keys.size) { index ->
                assertEquals(
                    expectedValues[index],
                    result[index]
                )
            }
        }
    }

    @Test
    fun `loadValuesFromRemote should be called if cache doesn't contain required values`() {
        val keys = listOf(1, 2, 3)
        val expectedValues = listOf("1", "2", "3")

        val cache = object : RepositoryCache<Int, String> {
            override fun getAll(keys: List<Int>): List<String> =
                emptyList()

            override fun putAll(map: Map<Int, String>) {}

            override fun clearCache() {
                throw IllegalStateException("cache.clearCache should not be called")
            }
        }

        val cacheProxy = RepositoryCacheProxy(
            cache = cache,
            loadValuesFromRemote = {
                Result.success(expectedValues)
            },
            getKeyFromValue = { it.toInt() }
        )

        runBlocking {
            val result = cacheProxy.getValues(keys, false).getOrThrow()
            repeat(keys.size) { index ->
                assertEquals(
                    expectedValues[index],
                    result[index]
                )
            }
        }
    }

    @Test
    fun `loadValuesFromRemote should be called only for not cached values`() {
        val cachedKeys = listOf(1, 2)
        val cachedValues = listOf("1", "2")

        val remoteKeys = listOf(3, 4)
        val remoteValues = listOf("3", "4")

        val cache = InMemoryRepositoryCache<Int, String>().apply {
            putAll(
                cachedKeys.associateWith { key -> cachedValues[key - 1] }
            )
        }

        var actualRemoteRequestedKeys: List<Int>? = null

        val cacheProxy = RepositoryCacheProxy(
            cache = cache,
            loadValuesFromRemote = { requestedKeys ->
                actualRemoteRequestedKeys = requestedKeys
                Result.success(remoteValues)
            },
            getKeyFromValue = { it.toInt() }
        )

        runBlocking {
            val actualValues = cacheProxy.getValues(cachedKeys + remoteKeys, false).getOrThrow()
            assertEquals(cachedValues + remoteValues, actualValues)
            assertEquals(remoteKeys, actualRemoteRequestedKeys)
        }
    }
}