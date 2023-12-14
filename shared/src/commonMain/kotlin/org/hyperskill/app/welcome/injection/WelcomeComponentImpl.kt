package org.hyperskill.app.welcome.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.welcome.presentation.WelcomeFeature
import ru.nobird.app.presentation.redux.feature.Feature

internal class WelcomeComponentImpl(private val appGraph: AppGraph) : WelcomeComponent {
    override val welcomeFeature: Feature<WelcomeFeature.State, WelcomeFeature.Message, WelcomeFeature.Action>
        get() = WelcomeFeatureBuilder.build(
            appGraph.buildWelcomeDataComponent().welcomeInteractor,
            appGraph.profileDataComponent.currentProfileStateRepository,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.loggerComponent.logger,
            appGraph.commonComponent.buildKonfig.buildVariant
        )
}