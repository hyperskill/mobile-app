package org.hyperskill.app.step_quiz_hints.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.comments.domain.interactor.CommentsInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.freemium.domain.interactor.FreemiumInteractor
import org.hyperskill.app.likes.domain.interactor.LikesInteractor
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.reactions.domain.interactor.ReactionsInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_hints.domain.interactor.StepQuizHintsInteractor
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsActionDispatcher
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsReducer
import org.hyperskill.app.step_quiz_hints.view.mapper.StepQuizHintsViewStateMapper
import org.hyperskill.app.user_storage.domain.interactor.UserStorageInteractor
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object StepQuizHintsFeatureBuilder {
    private const val LOG_TAG = "StepQuizHintsFeature"

    fun build(
        stepRoute: StepRoute,
        stepQuizHintsInteractor: StepQuizHintsInteractor,
        likesInteractor: LikesInteractor,
        commentsInteractor: CommentsInteractor,
        reactionsInteractor: ReactionsInteractor,
        userStorageInteractor: UserStorageInteractor,
        freemiumInteractor: FreemiumInteractor,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<StepQuizHintsFeature.ViewState, StepQuizHintsFeature.Message, StepQuizHintsFeature.Action> {
        val stepQuizHintsReducer = StepQuizHintsReducer(stepRoute).wrapWithLogger(buildVariant, logger, LOG_TAG)

        val stepQuizHintsDispatcher = StepQuizHintsActionDispatcher(
            ActionDispatcherOptions(),
            stepQuizHintsInteractor,
            likesInteractor,
            commentsInteractor,
            reactionsInteractor,
            userStorageInteractor,
            freemiumInteractor,
            analyticInteractor,
            sentryInteractor
        )

        return ReduxFeature(StepQuizHintsFeature.State.Idle, stepQuizHintsReducer)
            .transformState(StepQuizHintsViewStateMapper::mapState)
            .wrapWithActionDispatcher(stepQuizHintsDispatcher)
    }
}