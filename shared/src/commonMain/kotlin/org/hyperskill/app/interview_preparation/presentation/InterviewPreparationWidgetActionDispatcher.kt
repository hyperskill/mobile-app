package org.hyperskill.app.interview_preparation.presentation

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.SharedResources
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.Action
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.InternalAction
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.InternalMessage
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.Message
import org.hyperskill.app.interview_steps.domain.repository.InterviewStepsStateRepository
import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class InterviewPreparationWidgetActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor,
    private val interviewStepsStateRepository: InterviewStepsStateRepository,
    private val sentryInteractor: SentryInteractor,
    private val resourceProvider: ResourceProvider,
    private val onboardingInteractor: OnboardingInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        interviewStepsStateRepository.changes
            .onEach { interviewSteps ->
                onNewMessage(InternalMessage.StepsCountChanged(interviewSteps.count()))
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchInterviewSteps -> {
                sentryInteractor.withTransaction(
                    HyperskillSentryTransactionBuilder.buildInterviewPreparationWidgetFeatureFetchInterviewSteps(),
                    onError = { InternalMessage.FetchInterviewStepsResultError }
                ) {
                    interviewStepsStateRepository
                        .getState(forceUpdate = action.forceLoadFromNetwork)
                        .getOrThrow()
                        .let(InternalMessage::FetchInterviewStepsResultSuccess)
                }.let(::onNewMessage)
            }
            is InternalAction.FetchNextInterviewStep -> {
                handleFetchNextInterviewStep(::onNewMessage)
            }
            is InternalAction.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.event)
            }
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchNextInterviewStep(onNewMessage: (Message) -> Unit) {
        fun getErrorMessage(): InternalMessage.FetchNextInterviewStepResultError =
            InternalMessage.FetchNextInterviewStepResultError(
                resourceProvider.getString(SharedResources.strings.common_error)
            )

        try {
            val currentStepIds = interviewStepsStateRepository
                .getState(forceUpdate = false)
                .getOrNull()

            val nextInterviewStepId = if (!currentStepIds.isNullOrEmpty()) {
                val shuffledStepIds = currentStepIds.shuffled()
                interviewStepsStateRepository.updateState(shuffledStepIds)
                shuffledStepIds.last()
            } else {
                null
            }

            if (nextInterviewStepId != null) {
                InternalMessage.FetchNextInterviewStepResultSuccess(
                    stepId = nextInterviewStepId,
                    wasOnboardingShown = onboardingInteractor.wasInterviewPreparationOnboardingShown()
                )
            } else {
                getErrorMessage()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            getErrorMessage()
        }.let(onNewMessage)
    }
}