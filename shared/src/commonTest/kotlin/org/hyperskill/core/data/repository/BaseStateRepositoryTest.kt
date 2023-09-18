package org.hyperskill.core.data.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class BaseStateRepositoryTest {

    @Test
    fun `Single request should be executed`() =
        runBlocking {
            val expectedResult = TestStateRepository.DEFAULT_RESPONSE
            var loadStateCallsCount = 0
            val repository = TestStateRepository(
                externalLoadState = {
                    loadStateCallsCount++
                    expectedResult
                }
            )
            val result = repository.getState(forceUpdate = true)
            assertEquals(expectedResult, result.getOrThrow())
            assertEquals(1, loadStateCallsCount)
        }

    @Test
    fun `Concurrent forceUpdate call should trigger only one state loading`() =
        runBlocking {
            var loadStateCallsCount = 0
            val repository = TestStateRepository(
                externalLoadState = {
                    loadStateCallsCount++
                    delay(2000L)
                    TestStateRepository.DEFAULT_RESPONSE
                }
            )
            val deferreds = List(100) {
                async {
                    repository.getState(forceUpdate = true)
                }
            }
            deferreds.awaitAll()
            assertEquals(1, loadStateCallsCount)
        }

    @Test
    fun `Concurrent cached and forceUpdate call should returns expected results`() =
        runBlocking {
            val expectedCachedCallResult = "CacheResult"
            val expectedForceUpdateCallResult = "ForceUpdateResult"
            var loadStateCallsCount = 0
            val repository = TestStateRepository(
                // Cached value should be read with 2-seconds delay
                stateHolder = TestStateHolder(readStateDelay = 2.seconds),
                externalLoadState = {
                    val count = loadStateCallsCount++
                    when (count) {
                        0 -> expectedCachedCallResult
                        else -> expectedForceUpdateCallResult
                    }
                }
            )

            // Warm up call to have cached value
            repository.getState(forceUpdate = true)

            // Fetch cached value
            val cachedCallDeferred = async {
                repository.getState(forceUpdate = false)
            }

            // Fetch refreshed value
            val forceUpdateCallDeferred = async {
                repository.getState(forceUpdate = true)
            }

            val actualCachedCallResult = cachedCallDeferred.await().getOrThrow()
            val actualForceUpdateCallResults = forceUpdateCallDeferred.await().getOrThrow()

            assertEquals(expectedCachedCallResult, actualCachedCallResult)
            assertEquals(expectedForceUpdateCallResult, actualForceUpdateCallResults)
            assertEquals(2, loadStateCallsCount)
        }
}