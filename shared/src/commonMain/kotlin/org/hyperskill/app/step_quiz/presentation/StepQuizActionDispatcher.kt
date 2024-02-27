package org.hyperskill.app.step_quiz.presentation

import org.hyperskill.app.SharedResources
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.freemium.domain.interactor.FreemiumInteractor
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile.domain.repository.isFreemiumWrongSubmissionChargeLimitsEnabled
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.domain.model.submissions.isWrongOrRejected
import org.hyperskill.app.step_quiz.domain.validation.StepQuizReplyValidator
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Action
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.InternalAction
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.InternalMessage
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Message
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksMode
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class StepQuizActionDispatcher(
    config: ActionDispatcherOptions,
    private val stepQuizInteractor: StepQuizInteractor,
    private val stepQuizReplyValidator: StepQuizReplyValidator,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val freemiumInteractor: FreemiumInteractor,
    private val urlPathProcessor: UrlPathProcessor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val onboardingInteractor: OnboardingInteractor,
    private val resourceProvider: ResourceProvider
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
                val problemsLimitReachedModalData =
                    if (isProblemsLimitReached) getProblemsLimitReachedModalData() else null

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
                                        problemsLimitReachedModalData = problemsLimitReachedModalData,
                                        problemsOnboardingFlags = onboardingInteractor.getProblemsOnboardingFlags()
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
            is Action.SaveProblemOnboardingModalShownCacheFlag -> {
                when (action.modalType) {
                    StepQuizFeature.ProblemOnboardingModal.Parsons ->
                        onboardingInteractor.setParsonsOnboardingShown(isShown = true)
                    is StepQuizFeature.ProblemOnboardingModal.FillBlanks -> {
                        when (action.modalType.mode) {
                            FillBlanksMode.INPUT ->
                                onboardingInteractor.setFillBlanksInputModeOnboardingShown(isShown = true)
                            FillBlanksMode.SELECT ->
                                onboardingInteractor.setFillBlanksSelectModeOnboardingShown(isShown = true)
                        }
                    }
                }
            }
            is InternalAction.UpdateProblemsLimit ->
                handleUpdateProblemsLimitAction(action, ::onNewMessage)
            is InternalAction.CreateMagicLinkForUnsupportedQuiz -> {
                urlPathProcessor
                    .processUrlPath(HyperskillUrlPath.Step(action.stepRoute))
                    .fold(
                        onSuccess = { onNewMessage(InternalMessage.CreateMagicLinkForUnsupportedQuizSuccess(it)) },
                        onFailure = { onNewMessage(InternalMessage.CreateMagicLinkForUnsupportedQuizError) }
                    )
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

    private suspend fun handleUpdateProblemsLimitAction(
        action: InternalAction.UpdateProblemsLimit,
        onNewMessage: (Message) -> Unit
    ) {
        if (!currentProfileStateRepository.isFreemiumWrongSubmissionChargeLimitsEnabled()) return

        freemiumInteractor.chargeProblemsLimits(action.chargeStrategy)

        val isProblemsLimitReached = freemiumInteractor.isProblemsLimitReached().getOrDefault(false)
        val problemsLimitReachedModalData = if (isProblemsLimitReached) getProblemsLimitReachedModalData() else null

        onNewMessage(
            InternalMessage.UpdateProblemsLimitResult(
                isProblemsLimitReached = isProblemsLimitReached,
                problemsLimitReachedModalData = problemsLimitReachedModalData
            )
        )
    }

    private suspend fun getProblemsLimitReachedModalData(): StepQuizFeature.ProblemsLimitReachedModalData? {
        val stepsLimitTotal = freemiumInteractor
            .getStepsLimitTotal()
            .getOrElse { return null }
            ?: return null

        return if (currentProfileStateRepository.isFreemiumWrongSubmissionChargeLimitsEnabled()) {
            StepQuizFeature.ProblemsLimitReachedModalData(
                title = resourceProvider.getString(
                    SharedResources.strings.problems_limit_reached_modal_wrong_submission_charge_limits_title,
                    stepsLimitTotal
                ),
                description = resourceProvider.getString(
                    SharedResources.strings.problems_limit_reached_modal_wrong_submission_charge_limits_description
                )
            )
        } else {
            StepQuizFeature.ProblemsLimitReachedModalData(
                title = resourceProvider.getString(SharedResources.strings.problems_limit_reached_modal_title),
                description = resourceProvider.getString(
                    SharedResources.strings.problems_limit_reached_modal_description,
                    stepsLimitTotal
                )
            )
        }
    }
}