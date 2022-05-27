package org.hyperskill.app.main.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.main.presentation.AppFeature
import ru.nobird.app.presentation.redux.feature.Feature

class MainComponentImpl(appGraph: AppGraph) : MainComponentManual {
    override val appFeature: Feature<AppFeature.State, AppFeature.Message, AppFeature.Action> =
        AppFeatureBuilder.build(appGraph.authComponentManual.authInteractor)
}