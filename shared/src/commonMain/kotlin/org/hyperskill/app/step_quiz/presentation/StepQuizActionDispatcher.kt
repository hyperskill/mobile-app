package org.hyperskill.app.step_quiz.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.freemium.domain.interactor.FreemiumInteractor
import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.domain.model.submissions.isWrongOrRejected
import org.hyperskill.app.step_quiz.domain.validation.StepQuizReplyValidator
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Action
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepQuizActionDispatcher(
    config: ActionDispatcherOptions,
    private val stepQuizInteractor: StepQuizInteractor,
    private val stepQuizReplyValidator: StepQuizReplyValidator,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val freemiumInteractor: FreemiumInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val onboardingInteractor: OnboardingInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchAttempt -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildStepQuizScreenRemoteDataLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                val currentProfile = currentProfileStateRepository
                    .getState(forceUpdate = false)
                    .getOrElse {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        onNewMessage(Message.FetchAttemptError(it))
                        return
                    }

                val isProblemsLimitReached = freemiumInteractor
                    .isProblemsLimitReached()
                    .getOrElse {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        onNewMessage(Message.FetchAttemptError(it))
                        return
                    }

                val message = stepQuizInteractor
                    .getAttempt(action.step.id, currentProfile.id)
                    .fold(
                        onSuccess = { attempt ->
                            val message = getSubmissionState(attempt.id, action.step.id, currentProfile.id).fold(
                                onSuccess = {
                                    Message.FetchAttemptSuccess(
                                        step = action.step,
                                        attempt = attempt,
                                        submissionState = it,
                                        isProblemsLimitReached = isProblemsLimitReached,
                                        isParsonsOnboardingShown = onboardingInteractor.isParsonsOnboardingShown()
                                    )
                                },
                                onFailure = {
                                    Message.FetchAttemptError(it)
                                }
                            )
                            message
                        },
                        onFailure = { Message.FetchAttemptError(it) }
                    )

                sentryInteractor.finishTransaction(
                    transaction = sentryTransaction,
                    throwable = (message as? Message.FetchAttemptError)?.throwable
                )

                onNewMessage(message)
            }
            is Action.CreateAttempt -> {
                if (StepQuizResolver.isNeedRecreateAttemptForNewSubmission(action.step)) {
                    val sentryTransaction = HyperskillSentryTransactionBuilder.buildStepQuizCreateAttempt()
                    sentryInteractor.startTransaction(sentryTransaction)

                    val reply = (action.submissionState as? StepQuizFeature.SubmissionState.Loaded)
                        ?.submission
                        ?.reply

                    stepQuizInteractor
                        .createAttempt(action.step.id)
                        .fold(
                            onSuccess = {
                                sentryInteractor.finishTransaction(sentryTransaction)
                                onNewMessage(
                                    Message.CreateAttemptSuccess(
                                        step = action.step,
                                        attempt = it,
                                        submissionState = StepQuizFeature.SubmissionState.Empty(
                                            reply = if (action.shouldResetReply) null else reply
                                        ),
                                        isProblemsLimitReached = action.isProblemsLimitReached
                                    )
                                )
                            },
                            onFailure = {
                                sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                                onNewMessage(Message.CreateAttemptError)
                            }
                        )
                } else {
                    val submissionState = (action.submissionState as? StepQuizFeature.SubmissionState.Loaded)
                        ?.submission
                        ?.let {
                            it.copy(
                                id = it.id + 1,
                                status = SubmissionStatus.LOCAL,
                                hint = if (action.shouldResetReply) null else it.hint,
                                reply = if (action.shouldResetReply) null else it.reply,
                                attempt = action.attempt.id
                            )
                        }
                        ?.let { StepQuizFeature.SubmissionState.Loaded(it) }
                        ?: StepQuizFeature.SubmissionState.Empty()

                    onNewMessage(
                        Message.CreateAttemptSuccess(
                            action.step,
                            action.attempt,
                            submissionState,
                            action.isProblemsLimitReached
                        )
                    )
                }
            }
            is Action.CreateSubmissionValidateReply -> {
                val validationResult = stepQuizReplyValidator.validate(action.reply, action.step.block.name)
                onNewMessage(Message.CreateSubmissionReplyValidationResult(action.step, action.reply, validationResult))
            }
            is Action.CreateSubmission -> {
                val reply = action.submission.reply ?: return onNewMessage(Message.CreateSubmissionNetworkError)

                val sentryTransaction = HyperskillSentryTransactionBuilder.buildStepQuizCreateSubmission()
                sentryInteractor.startTransaction(sentryTransaction)

                var newAttempt: Attempt? = null
                if (action.submission.originalStatus.isWrongOrRejected &&
                    StepQuizResolver.isNeedRecreateAttemptForNewSubmission(action.step)
                ) {
                    newAttempt = stepQuizInteractor
                        .createAttempt(action.step.id)
                        .getOrElse {
                            sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                            return onNewMessage(Message.CreateSubmissionNetworkError)
                        }
                }

                stepQuizInteractor
                    .createSubmission(
                        stepId = action.step.id,
                        solvingContext = action.stepContext,
                        attemptId = newAttempt?.id ?: action.attemptId,
                        reply = reply
                    )
                    .fold(
                        onSuccess = { newSubmission ->
                            sentryInteractor.finishTransaction(sentryTransaction)
                            onNewMessage(Message.CreateSubmissionSuccess(newSubmission, newAttempt))
                        },
                        onFailure = {
                            sentryInteractor.finishTransaction(sentryTransaction)
                            onNewMessage(Message.CreateSubmissionNetworkError)
                        }
                    )
            }
            is Action.SaveParsonsProblemOnboardingModalShownCacheFlag -> {
                onboardingInteractor.setParsonsOnboardingShown(isShown = true)
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {}
        }
    }

    private suspend fun getSubmissionState(
        attemptId: Long,
        stepId: Long,
        userId: Long
    ): Result<StepQuizFeature.SubmissionState> =
        stepQuizInteractor
            .getSubmission(attemptId, stepId, userId)
            .map { submission ->
                if (submission == null) {
                    StepQuizFeature.SubmissionState.Empty()
                } else {
                    StepQuizFeature.SubmissionState.Loaded(submission)
                }
            }
}