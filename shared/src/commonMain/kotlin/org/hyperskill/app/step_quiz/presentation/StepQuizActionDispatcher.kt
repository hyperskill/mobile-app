package org.hyperskill.app.step_quiz.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.hyperskill.app.SharedResources
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.notification.data.extension.NotificationExtensions
import org.hyperskill.app.notification.domain.interactor.NotificationInteractor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.permissions.StepQuizUserPermissionRequest
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.domain.validation.StepQuizReplyValidator
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Action
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Message
import org.hyperskill.app.topics.domain.interactor.TopicsInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepQuizActionDispatcher(
    config: ActionDispatcherOptions,
    private val stepQuizInteractor: StepQuizInteractor,
    private val stepQuizReplyValidator: StepQuizReplyValidator,
    private val profileInteractor: ProfileInteractor,
    private val notificationInteractor: NotificationInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val resourceProvider: ResourceProvider,
    private val progressesInteractor: ProgressesInteractor,
    private val topicsInteractor: TopicsInteractor,
    private val readyToLoadNextStepQuizMutableSharedFlow: MutableSharedFlow<Step>
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        actionScope.launch {
            notificationInteractor.solvedStepsSharedFlow.collect { solvedStepId ->
                if (notificationInteractor.isRequiredToAskUserToEnableDailyReminders()) {
                    onNewMessage(Message.RequestUserPermission(StepQuizUserPermissionRequest.SEND_DAILY_STUDY_REMINDERS))
                } else {
                    val cachedProfile = profileInteractor
                        .getCurrentProfile(sourceType = DataSourceType.CACHE)
                        .getOrElse { return@collect }

                    if (cachedProfile.dailyStep == solvedStepId) {
                        val currentProfileHypercoinsBalance = profileInteractor
                            .getCurrentProfile(sourceType = DataSourceType.REMOTE)
                            .map { it.gamification.hypercoinsBalance }
                            .getOrElse { return@collect }

                        val gemsEarned = currentProfileHypercoinsBalance - cachedProfile.gamification.hypercoinsBalance

                        profileInteractor.notifyHypercoinsBalanceChanged(currentProfileHypercoinsBalance)
                        onNewMessage(
                            Message.ShowProblemOfDaySolvedModal(
                                earnedGemsText = resourceProvider.getQuantityString(SharedResources.plurals.earned_gems, gemsEarned, gemsEarned)
                            )
                        )
                    }
                }
            }
        }

        actionScope.launch {
            stepQuizInteractor.observeFailedToLoadNextStepQuiz().collect {
                onNewMessage(Message.NextStepQuizFetchedError)
            }
        }
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchAttempt -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildStepQuizScreenRemoteDataLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                val currentProfile = profileInteractor
                    .getCurrentProfile(sourceType = DataSourceType.CACHE)
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
                                onSuccess = { Message.FetchAttemptSuccess(action.step, attempt, it, currentProfile) },
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
                val currentProfile = profileInteractor
                    .getCurrentProfile(sourceType = DataSourceType.CACHE)
                    .getOrElse {
                        onNewMessage(Message.CreateAttemptError)
                        return
                    }

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
                                        currentProfile = currentProfile
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

                    onNewMessage(Message.CreateAttemptSuccess(action.step, action.attempt, submissionState, currentProfile))
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
                if (action.submission.originalStatus == SubmissionStatus.WRONG &&
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
                        attemptId = newAttempt?.id ?: action.attemptId,
                        reply = reply
                    )
                    .fold(
                        onSuccess = { newSubmission ->
                            if (action.stepRoute is StepRoute.Learn && newSubmission.status == SubmissionStatus.CORRECT) {
                                doSuspendableAction(Action.CheckTopicCompletion(action.step.topic))
                            }

                            sentryInteractor.finishTransaction(sentryTransaction)

                            onNewMessage(Message.CreateSubmissionSuccess(newSubmission, newAttempt))
                        },
                        onFailure = {
                            sentryInteractor.finishTransaction(sentryTransaction)
                            onNewMessage(Message.CreateSubmissionNetworkError)
                        }
                    )
            }
            is Action.RequestUserPermissionResult -> {
                when (action.userPermissionRequest) {
                    StepQuizUserPermissionRequest.RESET_CODE -> {}
                    StepQuizUserPermissionRequest.SEND_DAILY_STUDY_REMINDERS -> {
                        if (action.isGranted) {
                            notificationInteractor.setDailyStudyRemindersEnabled(true)
                            notificationInteractor.setDailyStudyRemindersIntervalStartHour(
                                NotificationExtensions.DAILY_REMINDERS_AFTER_STEP_SOLVED_START_HOUR
                            )
                        } else {
                            notificationInteractor.setLastTimeUserAskedToEnableDailyReminders(
                                Clock.System.now().toEpochMilliseconds()
                            )
                        }
                    }
                }
            }
            is Action.CheckTopicCompletion -> {
                val topicIsCompleted = progressesInteractor
                    .getTopicProgress(action.topicId)
                    .getOrElse {
                        return
                    }.isCompleted

                if (topicIsCompleted) {
                    val topicTitle = topicsInteractor
                        .getTopic(action.topicId)
                        .getOrElse {
                            return
                        }.title

                    onNewMessage(
                        Message.CurrentTopicCompleted(
                            resourceProvider.getString(SharedResources.strings.step_quiz_topic_completed_modal_text, topicTitle)
                        )
                    )
                }
            }
            is Action.NotifyFetchingNextStepQuiz -> {
                readyToLoadNextStepQuizMutableSharedFlow.emit(action.currentStep)
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {}
        }
    }

    private suspend fun getSubmissionState(attemptId: Long, stepId: Long, userId: Long): Result<StepQuizFeature.SubmissionState> =
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