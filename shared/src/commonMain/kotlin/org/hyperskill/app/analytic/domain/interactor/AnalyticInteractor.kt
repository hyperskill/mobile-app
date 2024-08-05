package org.hyperskill.app.analytic.domain.interactor

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.hyperskill.app.analytic.domain.model.Analytic
import org.hyperskill.app.analytic.domain.model.AnalyticEngine
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.AnalyticEventUserProperties
import org.hyperskill.app.analytic.domain.model.AnalyticKeys
import org.hyperskill.app.analytic.domain.model.monitor.AnalyticEventMonitor
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.domain.model.ScreenOrientation
import org.hyperskill.app.core.domain.platform.Platform
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import ru.nobird.app.core.model.mapOfNotNull

class AnalyticInteractor(
    // Provide CurrentSubscriptionStateRepository lazily
    // because it depends on AnalyticInteractor
    currentSubscriptionStateRepositoryProvider: () -> CurrentSubscriptionStateRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val notificationInteractor: NotificationInteractor,
    private val platform: Platform,
    private val analyticEngines: List<AnalyticEngine>,
    override val eventMonitor: AnalyticEventMonitor?
) : Analytic {

    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository by lazy {
        currentSubscriptionStateRepositoryProvider.invoke()
    }

    private var userProperties: MutableMap<String, Any> = mutableMapOf()

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

    override fun setUserProperty(key: String, value: Any) {
        userProperties[key] = value
    }

    override fun removeUserProperty(key: String) {
        userProperties.remove(key)
    }

    override fun setScreenOrientation(screenOrientation: ScreenOrientation) {
        userProperties[AnalyticKeys.PARAM_SCREEN_ORIENTATION] = when (screenOrientation) {
            ScreenOrientation.PORTRAIT -> AnalyticKeys.SCREEN_ORIENTATION_VALUE_PORTRAIT
            ScreenOrientation.LANDSCAPE -> AnalyticKeys.SCREEN_ORIENTATION_VALUE_LANDSCAPE
        }
    }

    override fun setAppTrackingTransparencyAuthorizationStatus(isAuthorized: Boolean) {
        userProperties[AnalyticKeys.PARAM_IS_ATT_ALLOW] = isAuthorized
    }

    private suspend fun getUserProperties(): AnalyticEventUserProperties =
        coroutineScope {
            val profileDeferred = async {
                currentProfileStateRepository.getState(forceUpdate = false).getOrNull()
            }
            val subscriptionDeferred = async {
                currentSubscriptionStateRepository.getState(forceUpdate = false).getOrNull()
            }

            val profile = profileDeferred.await()
            val subscription = subscriptionDeferred.await()

            AnalyticEventUserProperties(
                userId = profile?.id,
                properties = userProperties + mapOfNotNull(
                    AnalyticKeys.PARAM_PLATFORM to platform.analyticName,
                    AnalyticKeys.PARAM_SUBSCRIPTION_TYPE to subscription?.type,
                    AnalyticKeys.PARAM_SUBSCRIPTION_STATUS to subscription?.status,
                    AnalyticKeys.PARAM_IS_NOTIFICATIONS_ALLOW to
                        notificationInteractor.isNotificationsPermissionGranted(),
                    AnalyticKeys.PARAM_IS_INTERNAL_TESTING to (BuildKonfig.IS_INTERNAL_TESTING ?: false),
                    AnalyticKeys.PARAM_FEATURES to profile?.features?.origin?.takeIf { it.isNotEmpty() }
                ),
            )
        }
}