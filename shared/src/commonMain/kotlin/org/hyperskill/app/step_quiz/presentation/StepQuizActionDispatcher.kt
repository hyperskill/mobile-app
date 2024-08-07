package org.hyperskill.app.step_quiz.presentation

import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.features.data.source.FeaturesDataSource
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor
import org.hyperskill.app.profile.domain.model.freemiumChargeLimitsStrategy
import org.hyperskill.app.profile.domain.model.isFreemiumWrongSubmissionChargeLimitsEnabled
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizCreateSubmissionAmplitudeAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizSubmissionCreatedAmplitudeAnalyticEvent
import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.validation.StepQuizReplyValidator
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Action
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.InternalAction
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.InternalMessage
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Message
import org.hyperskill.app.submissions.domain.model.SubmissionStatus
import org.hyperskill.app.submissions.domain.model.isWrongOrRejected
import org.hyperskill.app.subscriptions.domain.interactor.SubscriptionsInteractor
import org.hyperskill.app.subscriptions.domain.model.isProblemsLimitReached
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class StepQuizActionDispatcher(
    config: ActionDispatcherOptions,
    private val stepQuizInteractor: StepQuizInteractor,
    private val stepQuizReplyValidator: StepQuizReplyValidator,
    private val subscriptionsInteractor: SubscriptionsInteractor,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val featuresDataSource: FeaturesDataSource,
    private val urlPathProcessor: UrlPathProcessor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val onboardingInteractor: OnboardingInteractor,
    private val logger: Logger
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        subscriptionsInteractor
            .subscribeOnSubscriptionWithLimitType()
            .distinctUntilChangedBy { it.isProblemsLimitReached }
            .onEach {
                onNewMessage(
                    InternalMessage.ProblemsLimitChanged(
                        subscription = it.subscription,
                        isProblemsLimitReached = it.isProblemsLimitReached
                    )
                )
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchAttempt ->
                handleFetchAttempt(action, ::onNewMessage)
            is Action.CreateAttempt -> {
                if (StepQuizResolver.isNeedRecreateAttemptForNewSubmission(action.step)) {
                    val sentryTransaction = HyperskillSentryTransactionBuilder.buildStepQuizCreateAttempt(
                        blockName = action.step.block.name
                    )
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
                val validationResult = stepQuizReplyValidator.validate(
                    dataset = action.dataset,
                    reply = action.reply,
                    stepBlockName = action.step.block.name
                )
                onNewMessage(Message.CreateSubmissionReplyValidationResult(action.step, action.reply, validationResult))
            }
            is Action.CreateSubmission -> {
                val reply = action.submission.reply ?: return onNewMessage(Message.CreateSubmissionNetworkError)

                analyticInteractor.logEvent(
                    StepQuizCreateSubmissionAmplitudeAnalyticEvent(
                        stepId = action.step.id,
                        blockName = action.step.block.name
                    )
                )

                val sentryTransaction = HyperskillSentryTransactionBuilder.buildStepQuizCreateSubmission(
                    blockName = action.step.block.name
                )
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
                            analyticInteractor.logEvent(
                                StepQuizSubmissionCreatedAmplitudeAnalyticEvent(
                                    stepId = action.step.id,
                                    blockName = action.step.block.name,
                                    submissionStatus = newSubmission.status
                                )
                            )
                            onNewMessage(Message.CreateSubmissionSuccess(newSubmission, newAttempt))
                        },
                        onFailure = { e ->
                            sentryInteractor.finishTransaction(sentryTransaction)
                            logger.e(e) { "Failed to create submission" }
                            onNewMessage(Message.CreateSubmissionNetworkError)
                        }
                    )
            }
            is Action.SaveProblemOnboardingModalShownCacheFlag -> {
                when (action.modalType) {
                    StepQuizFeature.ProblemOnboardingModal.Parsons ->
                        onboardingInteractor.setParsonsOnboardingShown(isShown = true)
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
            else -> {}
        }
    }

    private suspend fun handleFetchAttempt(
        action: Action.FetchAttempt,
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            transaction = HyperskillSentryTransactionBuilder.buildStepQuizScreenRemoteDataLoading(
                blockName = action.step.block.name
            ),
            onError = { InternalMessage.FetchAttemptError }
        ) {
            val currentProfile =
                currentProfileStateRepository
                    .getState()
                    .getOrThrow()

            val subscriptionWithLimitType =
                subscriptionsInteractor.getSubscriptionWithLimitType().getOrThrow()

            val attempt =
                stepQuizInteractor
                    .getAttempt(action.step.id, currentProfile.id)
                    .getOrThrow()
            val submissionState =
                getSubmissionState(attempt.id, action.step.id, currentProfile.id)
                    .getOrThrow()

            InternalMessage.FetchAttemptSuccess(
                step = action.step,
                attempt = attempt,
                submissionState = submissionState,
                subscription = subscriptionWithLimitType.subscription,
                chargeLimitsStrategy = currentProfile.freemiumChargeLimitsStrategy,
                problemsOnboardingFlags = onboardingInteractor.getProblemsOnboardingFlags(),
                isProblemsLimitReached = subscriptionWithLimitType.isProblemsLimitReached
            )
        }.let(onNewMessage)
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
        val features = featuresDataSource.getFeaturesMap()
        if (!features.isFreemiumWrongSubmissionChargeLimitsEnabled) return

        subscriptionsInteractor.chargeProblemsLimits(action.chargeStrategy)

        val currentSubscriptionWithLimitType =
            subscriptionsInteractor.getSubscriptionWithLimitType().getOrElse { return }

        onNewMessage(
            InternalMessage.UpdateProblemsLimitResult(
                subscription = currentSubscriptionWithLimitType.subscription,
                isProblemsLimitReached = currentSubscriptionWithLimitType.isProblemsLimitReached,
                chargeLimitsStrategy = features.freemiumChargeLimitsStrategy
            )
        )
    }
}