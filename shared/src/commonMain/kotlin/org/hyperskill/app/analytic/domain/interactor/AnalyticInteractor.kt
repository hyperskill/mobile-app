package org.hyperskill.app.analytic.domain.interactor

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.hyperskill.app.analytic.domain.model.Analytic
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.AnalyticEventMonitor
import org.hyperskill.app.analytic.domain.model.AnalyticSource
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.processor.AnalyticHyperskillEventProcessor
import org.hyperskill.app.analytic.domain.repository.AnalyticHyperskillRepository
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.core.domain.model.ScreenOrientation
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository

class AnalyticInteractor(
    private val authInteractor: AuthInteractor,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val notificationInteractor: NotificationInteractor,
    private val hyperskillRepository: AnalyticHyperskillRepository,
    private val hyperskillEventProcessor: AnalyticHyperskillEventProcessor,
    override val eventMonitor: AnalyticEventMonitor?
) : Analytic {
    companion object {
        private val FLUSH_EVENTS_DELAY_DURATION: Duration = 5.seconds
    }

    private var flushEventsJob: Job? = null

    private var screenOrientation: ScreenOrientation? = null

    override fun reportEvent(event: AnalyticEvent, forceReportEvent: Boolean) {
        MainScope().launch {
            logEvent(event, forceReportEvent)
        }
    }

    override suspend fun flushEvents() {
        launchHyperskillFlushEventsJob(withDelay = false)
        eventMonitor?.analyticDidFlushEvents()
    }

    suspend fun logEvent(event: AnalyticEvent, forceLogEvent: Boolean = false) {
        when (event.source) {
            AnalyticSource.HYPERSKILL_API -> logHyperskillEvent(event, forceLogEvent)
        }
        eventMonitor?.analyticDidReportEvent(event)
    }

    override fun setScreenOrientation(screenOrientation: ScreenOrientation) {
        this.screenOrientation = screenOrientation
    }

    private suspend fun logHyperskillEvent(event: AnalyticEvent, forceLogEvent: Boolean) {
        kotlin.runCatching {
            if (event !is HyperskillAnalyticEvent) {
                return
            }

            // Prevent multiple network calls when no cached profile
            val currentProfile: Profile =
                currentProfileStateRepository.getState().getOrElse { return }

            val processedEvent = hyperskillEventProcessor.processEvent(
                event = event,
                userId = currentProfile.id,
                isNotificationsPermissionGranted = notificationInteractor.isNotificationsPermissionGranted(),
                screenOrientation = screenOrientation ?: ScreenOrientation.PORTRAIT
            )
            hyperskillRepository.logEvent(processedEvent)

            if (flushEventsJob != null && !flushEventsJob!!.isCompleted) {
                return
            }

            launchHyperskillFlushEventsJob(withDelay = !forceLogEvent)
        }
    }

    private suspend fun launchHyperskillFlushEventsJob(withDelay: Boolean) {
        flushEventsJob?.cancelAndJoin()
        flushEventsJob = MainScope().launch {
            if (withDelay) {
                delay(FLUSH_EVENTS_DELAY_DURATION)
            }

            val isAuthorized = authInteractor.isAuthorized()
                .getOrDefault(false)

            hyperskillRepository.flushEvents(isAuthorized)
        }
    }
}