package org.hyperskill.app.step_quiz_hints.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.comments.domain.interactor.CommentsInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.likes.domain.interactor.LikesInteractor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.reactions.domain.interactor.ReactionsInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
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
    fun build(
        stepQuizHintsInteractor: StepQuizHintsInteractor,
        profileInteractor: ProfileInteractor,
        likesInteractor: LikesInteractor,
        commentsInteractor: CommentsInteractor,
        reactionsInteractor: ReactionsInteractor,
        userStorageInteractor: UserStorageInteractor,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor
    ): Feature<StepQuizHintsFeature.ViewState, StepQuizHintsFeature.Message, StepQuizHintsFeature.Action> {
        val stepQuizHintsReducer = StepQuizHintsReducer()

        val stepQuizHintsDispatcher = StepQuizHintsActionDispatcher(
            ActionDispatcherOptions(),
            stepQuizHintsInteractor,
            profileInteractor,
            likesInteractor,
            commentsInteractor,
            reactionsInteractor,
            userStorageInteractor,
            analyticInteractor,
            sentryInteractor
        )

        return ReduxFeature(StepQuizHintsFeature.State.Idle, stepQuizHintsReducer)
            .transformState(StepQuizHintsViewStateMapper::mapState)
            .wrapWithActionDispatcher(stepQuizHintsDispatcher)
    }
}