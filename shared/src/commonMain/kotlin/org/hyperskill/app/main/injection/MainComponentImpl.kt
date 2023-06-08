package org.hyperskill.app.main.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.main.presentation.AppFeature
import ru.nobird.app.presentation.redux.feature.Feature

class MainComponentImpl(private val appGraph: AppGraph) : MainComponent {
    private val streakRecoveryComponent = appGraph.buildStreakRecoveryComponent()

    override fun appFeature(
        initialState: AppFeature.State?
    ): Feature<AppFeature.State, AppFeature.Message, AppFeature.Action> =
        AppFeatureBuilder.build(
            initialState,
            appGraph.buildMainDataComponent().appInteractor,
            appGraph.authComponent.authInteractor,
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.stateRepositoriesComponent,
            streakRecoveryComponent.streakRecoveryReducer,
            streakRecoveryComponent.streakRecoveryActionDispatcher
        )

    override fun appFeature(): Feature<AppFeature.State, AppFeature.Message, AppFeature.Action> =
        appFeature(null)
}