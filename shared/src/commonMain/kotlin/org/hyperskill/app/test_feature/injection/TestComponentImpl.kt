package org.hyperskill.app.test_feature

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.test_feature.TestFeature.Action
import org.hyperskill.app.test_feature.TestFeature.Message
import org.hyperskill.app.test_feature.TestFeature.State
import ru.nobird.app.presentation.redux.feature.Feature

class TestComponentImpl(
    private val appGraph: AppGraph
) : TestComponent {
    override val test: Feature<State, Message, Action>
        get() = TestFeatureBuilder.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}