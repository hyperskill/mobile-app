package org.hyperskill.app.analytic.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.domain.model.AnalyticEngine
import org.hyperskill.app.core.injection.AppGraph

class AnalyticComponentImpl(
    appGraph: AppGraph,
    platformAnalyticEngines: List<AnalyticEngine>
) : AnalyticComponent {
    private val analyticEngines: List<AnalyticEngine> =
        platformAnalyticEngines + listOf(appGraph.buildHyperskillAnalyticEngineComponent().hyperskillAnalyticEngine)

    override val analyticInteractor: AnalyticInteractor =
        AnalyticInteractor(
            analyticEngines = analyticEngines,
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            notificationInteractor = appGraph.buildNotificationComponent().notificationInteractor,
            eventMonitor = appGraph.sentryComponent.sentryInteractor,
            platform = appGraph.commonComponent.platform
        )
}