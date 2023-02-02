package org.hyperskill.app.topics_repetitions.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository
import org.hyperskill.app.topics_repetitions.domain.flow.TopicRepeatedFlow
import org.hyperskill.app.topics_repetitions.domain.interactor.TopicsRepetitionsInteractor
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsActionDispatcher
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Action
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Message
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.State
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object TopicsRepetitionsFeatureBuilder {
    fun build(
        topicsRepetitionsInteractor: TopicsRepetitionsInteractor,
        profileInteractor: ProfileInteractor,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        topicRepeatedFlow: TopicRepeatedFlow,
        submissionRepository: SubmissionRepository
    ): Feature<State, Message, Action> {
        val topicsRepetitionsReducer = TopicsRepetitionsReducer()

        val topicsRepetitionsActionDispatcher = TopicsRepetitionsActionDispatcher(
            ActionDispatcherOptions(),
            topicsRepetitionsInteractor,
            profileInteractor,
            analyticInteractor,
            sentryInteractor,
            topicRepeatedFlow,
            submissionRepository
        )

        return ReduxFeature(State.Idle, topicsRepetitionsReducer)
            .wrapWithActionDispatcher(topicsRepetitionsActionDispatcher)
    }
}