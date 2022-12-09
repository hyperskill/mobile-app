package org.hyperskill.app.main.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.main.presentation.AppFeature
import ru.nobird.app.presentation.redux.feature.Feature

class MainComponentImpl(private val appGraph: AppGraph) : MainComponent {

    override val appFeature: Feature<AppFeature.State, AppFeature.Message, AppFeature.Action>
        get() = AppFeatureBuilder.build(
            appGraph.authComponent.authInteractor,
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor
        )
}