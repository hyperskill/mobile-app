package org.hyperskill.app.main.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.main.presentation.AppFeature
import ru.nobird.app.presentation.redux.feature.Feature

class MainComponentImpl(private val appGraph: AppGraph) : MainComponent {
    override fun appFeature(
        initialState: AppFeature.State?
    ): Feature<AppFeature.State, AppFeature.Message, AppFeature.Action> {
        val clickedNotificationComponent = appGraph.buildClickedNotificationComponent()
        return AppFeatureBuilder.build(
            initialState = initialState,
            appInteractor = appGraph.buildMainDataComponent().appInteractor,
            authInteractor = appGraph.authComponent.authInteractor,
            profileInteractor = appGraph.buildProfileDataComponent().profileInteractor,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            stateRepositoriesComponent = appGraph.stateRepositoriesComponent,
            clickedNotificationReducer = clickedNotificationComponent.notificationClickHandlingReducer,
            notificationClickHandlingDispatcher = clickedNotificationComponent.notificationClickHandlingDispatcher
        )
    }

    override fun appFeature(): Feature<AppFeature.State, AppFeature.Message, AppFeature.Action> =
        appFeature(null)
}