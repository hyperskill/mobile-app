package org.hyperskill.app.theory_feedback.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackActionDispatcher
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.Action
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.Message
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.ViewState
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackReducer
import org.hyperskill.app.theory_feedback.view.TheoryFeedbackViewStateMapper
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object TheoryFeedbackFeatureBuilder {
    private const val LOG_TAG = "TheoryFeedbackFeature"

    fun build(
        analyticInteractor: AnalyticInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<ViewState, Message, Action> {
        val theoryFeedbackReducer =
            TheoryFeedbackReducer()
                .wrapWithLogger(buildVariant, logger, LOG_TAG)

        val theoryFeedbackActionDispatcher = TheoryFeedbackActionDispatcher(
            ActionDispatcherOptions(),
            analyticInteractor
        )

        return ReduxFeature(
            initialState = TheoryFeedbackFeature.initialState(),
            reducer = theoryFeedbackReducer
        )
            .wrapWithActionDispatcher(theoryFeedbackActionDispatcher)
            .transformState(TheoryFeedbackViewStateMapper::map)
    }
}