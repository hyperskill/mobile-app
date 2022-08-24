package org.hyperskill.app.step_quiz.presentation

import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.notification.data.extension.NotificationExtensions
import org.hyperskill.app.notification.domain.NotificationInteractor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Action
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepQuizActionDispatcher(
    config: ActionDispatcherOptions,
    private val stepQuizInteractor: StepQuizInteractor,
    private val profileInteractor: ProfileInteractor,
    private val notificationInteractor: NotificationInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        actionScope.launch {
            notificationInteractor.solvedStepsSharedFlow.collect {
                if (notificationInteractor.isRequiredToAskUserToEnableDailyReminders()) {
                    onNewMessage(Message.NeedToAskUserToEnableDailyReminders)
                }
            }
        }
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchAttempt -> {
                val currentProfile = profileInteractor
                    .getCurrentProfile(sourceType = DataSourceType.CACHE)
                    .getOrElse {
                        onNewMessage(Message.FetchAttemptError)
                        return
                    }

                val message = stepQuizInteractor
                    .getAttempt(action.step.id, currentProfile.id)
                    .fold(
                        onSuccess = { attempt ->
                            val message = getSubmissionState(attempt.id, action.step.id, currentProfile.id).fold(
                                onSuccess = { Message.FetchAttemptSuccess(attempt, it) },
                                onFailure = {
                                    Message.FetchAttemptError
                                }
                            )
                            message
                        },
                        onFailure = { Message.FetchAttemptError }
                    )
                onNewMessage(message)
            }
            is Action.CreateAttempt -> {
                if (stepQuizInteractor.isNeedRecreateAttemptForNewSubmission(action.step)) {
                    val reply = (action.submissionState as? StepQuizFeature.SubmissionState.Loaded)
                        ?.submission
                        ?.reply

                    val message = stepQuizInteractor
                        .createAttempt(action.step.id)
                        .fold(
                            onSuccess = {
                                Message.CreateAttemptSuccess(it, StepQuizFeature.SubmissionState.Empty(reply = reply))
                            },
                            onFailure = {
                                Message.CreateAttemptError
                            }
                        )
                    onNewMessage(message)
                } else {
                    val submissionState = (action.submissionState as? StepQuizFeature.SubmissionState.Loaded)
                        ?.submission
                        ?.let { Submission(id = it.id + 1, attempt = action.attempt.id, reply = it.reply, status = SubmissionStatus.LOCAL) }
                        ?.let { StepQuizFeature.SubmissionState.Loaded(it) }
                        ?: StepQuizFeature.SubmissionState.Empty()

                    onNewMessage(Message.CreateAttemptSuccess(action.attempt, submissionState))
                }
            }
            is Action.CreateSubmission -> {
                val message = stepQuizInteractor
                    .createSubmission(action.step.id, action.attemptId, action.reply)
                    .fold(
                        onSuccess = { newSubmission ->
                            Message.CreateSubmissionSuccess(newSubmission)
                        },
                        onFailure = {
                            Message.CreateSubmissionError
                        }
                    )
                onNewMessage(message)
            }
            is Action.NotifyUserAgreedToEnableDailyReminders -> {
                notificationInteractor.setDailyStudyRemindersEnabled(true)
                notificationInteractor.setDailyStudyRemindersIntervalStartHour(NotificationExtensions.DAILY_REMINDERS_AFTER_STEP_SOLVED_START_HOUR)
            }
            is Action.NotifyUserDeclinedToEnableDailyReminders -> {
                notificationInteractor.setLastTimeUserAskedToEnableDailyReminders(Clock.System.now().toEpochMilliseconds())
            }
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