package org.hyperskill.app.analytic.domain.model.hyperskill

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.processor.AnalyticHyperskillEventProcessor
import org.hyperskill.app.analytic.domain.repository.AnalyticHyperskillRepository
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.domain.model.ScreenOrientation
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository

internal class HyperskillAnalyticEngineImpl(
    private val authInteractor: AuthInteractor,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val notificationInteractor: NotificationInteractor,
    private val analyticHyperskillRepository: AnalyticHyperskillRepository,
    private val analyticHyperskillEventProcessor: AnalyticHyperskillEventProcessor,
) : HyperskillAnalyticEngine() {

    companion object {
        private val FLUSH_EVENTS_DELAY_DURATION: Duration = 5.seconds
    }

    private var flushEventsJob: Job? = null

    private var screenOrientation: ScreenOrientation? = null
    private var isATTPermissionGranted: Boolean = false
    private val isInternalTesting: Boolean = BuildKonfig.IS_INTERNAL_TESTING ?: false

    override fun setScreenOrientation(screenOrientation: ScreenOrientation) {
        this.screenOrientation = screenOrientation
    }

    override fun setAppTrackingTransparencyAuthorizationStatus(isAuthorized: Boolean) {
        this.isATTPermissionGranted = isAuthorized
    }

    override suspend fun reportEvent(event: AnalyticEvent, force: Boolean) {
        internalReportEvent(event, forceReport = force)
    }

    override suspend fun flushEvents() {
        launchFlushEventsJob(withDelay = false)
    }

    private suspend fun internalReportEvent(event: AnalyticEvent, forceReport: Boolean) {
        kotlin.runCatching {
            if (event !is HyperskillAnalyticEvent) {
                return
            }

            val currentProfile: Profile =
                currentProfileStateRepository.getState().getOrElse { return }

            val processedEvent = analyticHyperskillEventProcessor.processEvent(
                event = event,
                userId = currentProfile.id,
                isNotificationsPermissionGranted = notificationInteractor.isNotificationsPermissionGranted(),
                isATTPermissionGranted = isATTPermissionGranted,
                screenOrientation = screenOrientation ?: ScreenOrientation.PORTRAIT,
                isInternalTesting = isInternalTesting
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

            analyticHyperskillRepository.flushEvents(isAuthorized)
        }
    }
}