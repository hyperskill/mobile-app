package org.hyperskill.app.streak_recovery.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryActionDispatcher
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryReducer

internal class StreakRecoveryComponentImpl(
    private val appGraph: AppGraph
) : StreakRecoveryComponent {
    override val streakRecoveryReducer: StreakRecoveryReducer
        get() = StreakRecoveryReducer(resourceProvider = appGraph.commonComponent.resourceProvider)

    override val streakRecoveryActionDispatcher: StreakRecoveryActionDispatcher
        get() = StreakRecoveryActionDispatcher(
            ActionDispatcherOptions(),
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            streaksInteractor = appGraph.buildStreaksDataComponent().streaksInteractor,
            productsInteractor = appGraph.buildProductsDataComponent().productsInteractor,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            streakFlow = appGraph.streakFlowDataComponent.streakFlow,
            resourceProvider = appGraph.commonComponent.resourceProvider
        )
}