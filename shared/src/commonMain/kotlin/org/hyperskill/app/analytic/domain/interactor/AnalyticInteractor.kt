package org.hyperskill.app.analytic.domain.interactor

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.hyperskill.app.analytic.domain.model.Analytic
import org.hyperskill.app.analytic.domain.model.AnalyticEngine
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.AnalyticEventMonitor
import org.hyperskill.app.analytic.domain.model.AnalyticEventUserProperties
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.domain.model.ScreenOrientation
import org.hyperskill.app.core.domain.platform.Platform
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository

class AnalyticInteractor(
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val notificationInteractor: NotificationInteractor,
    private val platform: Platform,
    private val analyticEngines: List<AnalyticEngine>,
    override val eventMonitor: AnalyticEventMonitor?
) : Analytic {

    private var screenOrientation: ScreenOrientation? = null
    private var isATTPermissionGranted: Boolean = false

    override fun reportEvent(event: AnalyticEvent, forceReportEvent: Boolean) {
        MainScope().launch {
            logEvent(event, forceReportEvent)
        }
    }

    override suspend fun flushEvents() {
        analyticEngines.forEach { it.flushEvents() }
        eventMonitor?.analyticDidFlushEvents()
    }

    suspend fun logEvent(event: AnalyticEvent, forceLogEvent: Boolean = false) {
        val engines = analyticEngines.filter { it.targetSource in event.sources }
        val userProperties = getUserProperties()
        engines.forEach { engine -> engine.reportEvent(event, userProperties, forceLogEvent) }
        eventMonitor?.analyticDidReportEvent(event)
    }

    override fun setScreenOrientation(screenOrientation: ScreenOrientation) {
        this.screenOrientation = screenOrientation
    }

    override fun setAppTrackingTransparencyAuthorizationStatus(isAuthorized: Boolean) {
        this.isATTPermissionGranted = isAuthorized
    }

    private suspend fun getUserProperties(): AnalyticEventUserProperties =
        coroutineScope {
            val profileDeferred = async {
                currentProfileStateRepository.getState(forceUpdate = false).getOrNull()
            }
            val subscriptionDeferred = async {
                currentSubscriptionStateRepository.getState(forceUpdate = false).getOrNull()
            }
            AnalyticEventUserProperties(
                userId = profileDeferred.await()?.id,
                subscriptionType = subscriptionDeferred.await()?.type,
                subscriptionStatus = subscriptionDeferred.await()?.status,
                isNotificationsPermissionGranted = notificationInteractor.isNotificationsPermissionGranted(),
                isATTPermissionGranted = isATTPermissionGranted,
                screenOrientation = screenOrientation ?: ScreenOrientation.PORTRAIT,
                isInternalTesting = BuildKonfig.IS_INTERNAL_TESTING ?: false,
                platform = platform.analyticName
            )
        }
}