package org.hyperskill.app.analytic.domain.model.hyperskill

import co.touchlab.kermit.Logger
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.AnalyticEventUserProperties
import org.hyperskill.app.analytic.domain.processor.AnalyticHyperskillEventProcessor
import org.hyperskill.app.analytic.domain.repository.AnalyticHyperskillRepository
import org.hyperskill.app.auth.domain.interactor.AuthInteractor

internal class HyperskillAnalyticEngineImpl(
    private val authInteractor: AuthInteractor,
    private val analyticHyperskillRepository: AnalyticHyperskillRepository,
    private val logger: Logger
) : HyperskillAnalyticEngine() {

    companion object {
        private val FLUSH_EVENTS_DELAY_DURATION: Duration = 5.seconds
    }

    private var flushEventsJob: Job? = null

    override suspend fun reportEvent(
        event: AnalyticEvent,
        userProperties: AnalyticEventUserProperties,
        force: Boolean
    ) {
        internalReportEvent(event, userProperties, forceReport = force)
    }

    override suspend fun flushEvents() {
        launchFlushEventsJob(withDelay = false)
    }

    private suspend fun internalReportEvent(
        event: AnalyticEvent,
        userProperties: AnalyticEventUserProperties,
        forceReport: Boolean
    ) {
        kotlin.runCatching {
            if (event !is HyperskillAnalyticEvent || userProperties.userId == null) {
                return
            }

            val processedEvent = AnalyticHyperskillEventProcessor.processEvent(
                event = event,
                userId = userProperties.userId,
                userProperties = userProperties
            )
            analyticHyperskillRepository.logEvent(processedEvent)

            if (flushEventsJob != null && !flushEventsJob!!.isCompleted) {
                return
            }

            launchFlushEventsJob(withDelay = !forceReport)
        }
    }

    private suspend fun launchFlushEventsJob(withDelay: Boolean) {
        flushEventsJob?.cancelAndJoin()
        flushEventsJob = MainScope().launch {
            if (withDelay) {
                delay(FLUSH_EVENTS_DELAY_DURATION)
            }

            val isAuthorized = authInteractor.isAuthorized()
                .getOrDefault(false)

            analyticHyperskillRepository
                .flushEvents(isAuthorized)
                .onFailure { logger.e(it) { "Failed to flush events" } }
        }
    }
}