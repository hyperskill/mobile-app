package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.notification.domain.interactor.NotificationInteractor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor
import org.hyperskill.app.step_quiz.domain.validation.StepQuizReplyValidator
import org.hyperskill.app.step_quiz.presentation.StepQuizActionDispatcher
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object StepQuizFeatureBuilder {
    fun build(
        stepRoute: StepRoute,
        stepQuizInteractor: StepQuizInteractor,
        stepQuizReplyValidator: StepQuizReplyValidator,
        profileInteractor: ProfileInteractor,
        notificationInteractor: NotificationInteractor,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        resourceProvider: ResourceProvider
    ): Feature<StepQuizFeature.State, StepQuizFeature.Message, StepQuizFeature.Action> {
        val stepQuizReducer = StepQuizReducer(stepRoute)
        val stepQuizActionDispatcher = StepQuizActionDispatcher(
            ActionDispatcherOptions(),
            stepQuizInteractor,
            stepQuizReplyValidator,
            profileInteractor,
            notificationInteractor,
            analyticInteractor,
            sentryInteractor,
            resourceProvider
        )

        return ReduxFeature(
            StepQuizFeature.State.Idle,
            stepQuizReducer
        )
            .wrapWithActionDispatcher(stepQuizActionDispatcher)
    }
}