package org.hyperskill.analytic.data.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hyperskill.app.analytic.cache.AnalyticHyperskillCacheDataSourceImpl
import org.hyperskill.app.analytic.data.repository.AnalyticHyperskillRepositoryImpl
import org.hyperskill.app.analytic.data.source.AnalyticHyperskillRemoteDataSource
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.AnalyticSource

class AnalyticHyperskillRepositoryTest {

    @Test
    fun `Logged event should be written to the cache`() {
        val testEvent = getAnalyticEventStub("Test event")

        val cacheDataSource = AnalyticHyperskillCacheDataSourceImpl()

        val remoteDataSource = object : AnalyticHyperskillRemoteDataSource {
            override suspend fun flushEvents(events: List<AnalyticEvent>, isAuthorized: Boolean): Result<Unit> {
                TODO("Not yet implemented")
            }
        }

        val repository = AnalyticHyperskillRepositoryImpl(remoteDataSource, cacheDataSource)

        runBlocking {
            repository.logEvent(testEvent)
            val actualEvents = cacheDataSource.getEvents()
            assertEquals(listOf(testEvent), actualEvents)
        }
    }

    @Test
    fun `Cached events should be removed from the cache after flushing`() {
        val initialEvent = getAnalyticEventStub("Initial")

        val cacheDataSource = AnalyticHyperskillCacheDataSourceImpl()

        val remoteDataSource = object : AnalyticHyperskillRemoteDataSource {
            override suspend fun flushEvents(events: List<AnalyticEvent>, isAuthorized: Boolean): Result<Unit> =
                Result.success(Unit)
        }

        val repository = AnalyticHyperskillRepositoryImpl(remoteDataSource, cacheDataSource)

        runBlocking {
            repository.logEvent(initialEvent)
            repository.flushEvents(isAuthorized = true)
            assertTrue(cacheDataSource.getEvents().isEmpty())
        }
    }

    @Test
    fun `Cached events should be returned to cache after failed flushing`() {
        val initialEvents = listOf(
            getAnalyticEventStub("First"),
            getAnalyticEventStub("Second"),
            getAnalyticEventStub("Third")
        )

        val extraEvent = getAnalyticEventStub("Extra")

        val cacheDataSource = AnalyticHyperskillCacheDataSourceImpl()
        val remoteDataSource = object : AnalyticHyperskillRemoteDataSource {
            override suspend fun flushEvents(events: List<AnalyticEvent>, isAuthorized: Boolean): Result<Unit> {
                delay(1.seconds)
                return Result.failure(Exception("Flush is failed"))
            }
        }

        val repository = AnalyticHyperskillRepositoryImpl(remoteDataSource, cacheDataSource)

        runBlocking {
            cacheDataSource.logEvents(initialEvents)
            repository.flushEvents(isAuthorized = true)
            repository.logEvent(extraEvent)
            assertEquals(
                expected = cacheDataSource.getEvents(),
                actual = initialEvents + extraEvent
            )
        }
    }

    private fun getAnalyticEventStub(
        name: String
    ): AnalyticEvent =
        object : AnalyticEvent {
            override val name: String = name
            override val sources: Set<AnalyticSource>
                get() = setOf(AnalyticSource.HYPERSKILL_API)

            override fun toString(): String = name
        }
}