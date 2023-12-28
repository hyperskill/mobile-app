package org.hyperskill.app.interview_preparation.presentation

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.repository.updateState
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.Action
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.InternalAction
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.InternalMessage
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.Message
import org.hyperskill.app.interview_steps.domain.repository.InterviewStepsStateRepository
import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.step.domain.model.StepId
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository
import ru.nobird.app.core.model.mutate
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class InterviewPreparationWidgetActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor,
    private val interviewStepsStateRepository: InterviewStepsStateRepository,
    private val sentryInteractor: SentryInteractor,
    private val onboardingInteractor: OnboardingInteractor,
    submissionRepository: SubmissionRepository
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        submissionRepository.solvedStepsSharedFlow
            .onEach {
                val stepId = StepId(it)
                interviewStepsStateRepository.updateState { origin ->
                    origin.mutate { remove(stepId) }
                }
                onNewMessage(
                    InternalMessage.StepSolved(stepId)
                )
            }
            .launchIn(actionScope)
    }
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            InternalAction.FetchInterviewSteps -> {
                sentryInteractor.withTransaction(
                    HyperskillSentryTransactionBuilder.buildInterviewPreparationWidgetFeatureFetchInterviewSteps(),
                    onError = { InternalMessage.FetchInterviewStepsResult.Error }
                ) {
                    interviewStepsStateRepository
                        .getState(forceUpdate = false)
                        .getOrThrow()
                        .let(InternalMessage.FetchInterviewStepsResult::Success)
                }.let(::onNewMessage)
            }
            is InternalAction.FetchOnboardingFlag -> {
                try {
                    InternalMessage.OnboardingFlagFetchResult.Success(
                        onboardingInteractor.wasInterviewPreparationOnboardingShown()
                    )
                } catch (_: Exception) {
                    InternalMessage.OnboardingFlagFetchResult.Error
                }.let(::onNewMessage)
            }
            is InternalAction.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.event)
            }
            else -> {
                // no op
            }
        }
    }
}