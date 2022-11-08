package org.hyperskill.app.step_quiz_hints.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.comments.domain.interactor.CommentsDataInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.step_quiz_hints.domain.interactor.StepQuizHintsInteractor
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Action
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Message
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.State
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsActionDispatcher
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsReducer
import org.hyperskill.app.user_storage.domain.interactor.UserStorageInteractor
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object StepQuizHintsFeatureBuilder {
    fun build(
        commentsDataInteractor: CommentsDataInteractor,
        userStorageInteractor: UserStorageInteractor,
        stepQuizHintsInteractor: StepQuizHintsInteractor,
        analyticInteractor: AnalyticInteractor,
        profileInteractor: ProfileInteractor
    ): Feature<State, Message, Action> {
        val stepQuizHintsReducer = StepQuizHintsReducer()

        val stepQuizHintsDispatcher = StepQuizHintsActionDispatcher(
            ActionDispatcherOptions(),
            commentsDataInteractor,
            userStorageInteractor,
            stepQuizHintsInteractor,
            analyticInteractor,
            profileInteractor
        )

        return ReduxFeature(State.Idle, stepQuizHintsReducer)
            .wrapWithActionDispatcher(stepQuizHintsDispatcher)
    }
}