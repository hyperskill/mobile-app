package org.hyperskill.app.analytic.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.domain.model.AnalyticEngine
import org.hyperskill.app.analytic.domain.model.monitor.BatchAnalyticEventMonitor
import org.hyperskill.app.analytic.domain.model.monitor.LoggableAnalyticEventMonitor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor

class AnalyticComponentImpl(
    appGraph: AppGraph,
    platformAnalyticEngines: List<AnalyticEngine>
) : AnalyticComponent {

    companion object {
        private const val LOG_TAG = "AnalyticEngine"
    }

    private val analyticEngines: List<AnalyticEngine> =
        platformAnalyticEngines + listOf(appGraph.buildHyperskillAnalyticEngineComponent().hyperskillAnalyticEngine)

    private val sentryInteractor: SentryInteractor =
        appGraph.sentryComponent.sentryInteractor

    private val buildVariant: BuildVariant =
        appGraph.commonComponent.buildKonfig.buildVariant

    private val loggableAnalyticEventMonitor: LoggableAnalyticEventMonitor? =
        if (buildVariant != BuildVariant.RELEASE) {
            LoggableAnalyticEventMonitor(appGraph.loggerComponent.logger.withTag(LOG_TAG))
        } else {
            null
        }

    override val analyticInteractor: AnalyticInteractor =
        AnalyticInteractor(
            analyticEngines = analyticEngines,
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            currentSubscriptionStateRepositoryProvider = {
                appGraph.stateRepositoriesComponent.currentSubscriptionStateRepository
            },
            notificationInteractor = appGraph.buildNotificationComponent().notificationInteractor,
            eventMonitor = BatchAnalyticEventMonitor(listOfNotNull(sentryInteractor, loggableAnalyticEventMonitor)),
            platform = appGraph.commonComponent.platform
        )
}