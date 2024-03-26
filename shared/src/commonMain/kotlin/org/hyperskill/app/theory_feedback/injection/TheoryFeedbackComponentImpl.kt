package org.hyperskill.app.theory_feedback.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.Action
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.Message
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

internal class TheoryFeedbackComponentImpl(
    private val appGraph: AppGraph
) : TheoryFeedbackComponent {
    override val theoryFeedbackFeature: Feature<ViewState, Message, Action>
        get() = TheoryFeedbackFeatureBuilder.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}