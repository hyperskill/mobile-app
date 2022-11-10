package org.hyperskill.app.step_quiz_hints.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.comments.domain.interactor.CommentsInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.likes.domain.interactor.LikesInteractor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.reactions.domain.interactor.ReactionsInteractor
import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.step_quiz_hints.domain.interactor.StepQuizHintsInteractor
import org.hyperskill.app.step_quiz_hints.domain.model.HintState
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Action
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Message
import org.hyperskill.app.user_storage.domain.interactor.UserStorageInteractor
import org.hyperskill.app.user_storage.domain.model.UserStoragePathBuilder
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepQuizHintsActionDispatcher(
    config: ActionDispatcherOptions,
    private val stepQuizHintsInteractor: StepQuizHintsInteractor,
    private val profileInteractor: ProfileInteractor,
    private val likesInteractor: LikesInteractor,
    private val commentsInteractor: CommentsInteractor,
    private val reactionsInteractor: ReactionsInteractor,
    private val userStorageInteractor: UserStorageInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchHintsIds -> {
                val hintsIds = stepQuizHintsInteractor.getNotSeenHintsIds(action.stepId)
                val dailyStepId = profileInteractor
                    .getCurrentProfile()
                    .fold(
                        onSuccess = { it.dailyStep },
                        onFailure = { null }
                    )

                val lastSeenHint = stepQuizHintsInteractor.getLastSeenHint(action.stepId)

                onNewMessage(
                    Message.HintsIdsLoaded(
                        hintsIds = hintsIds,
                        lastSeenHint = lastSeenHint,
                        isDailyStep = dailyStepId == action.stepId,
                        stepId = action.stepId
                    )
                )
            }
            is Action.ReportHint -> {
                val message = likesInteractor
                    .abuseComment(action.hintId)
                    .fold(
                        onSuccess = {
                            userStorageInteractor.updateUserStorage(
                                UserStoragePathBuilder.buildSeenHint(action.stepId, action.hintId),
                                HintState.UNHELPFUL.userStorageValue
                            )
                            Message.ReportHintSuccess
                        },
                        onFailure = { Message.ReportHintFailure }
                    )

                onNewMessage(message)
            }
            is Action.ReactHint -> {
                val message = reactionsInteractor
                    .createCommentReaction(action.hintId, action.reaction)
                    .fold(
                        onSuccess = {
                            userStorageInteractor.updateUserStorage(
                                UserStoragePathBuilder.buildSeenHint(action.stepId, action.hintId),
                                when (action.reaction) {
                                    ReactionType.HELPFUL -> HintState.HELPFUL.userStorageValue
                                    ReactionType.UNHELPFUL -> HintState.UNHELPFUL.userStorageValue
                                }
                            )
                            Message.ReactHintSuccess
                        },
                        onFailure = { Message.ReactHintFailure }
                    )

                onNewMessage(message)
            }
            is Action.FetchNextHint -> {
                commentsInteractor
                    .getComment(action.nextHintId)
                    .onSuccess {
                        onNewMessage(
                            Message.NextHintLoaded(
                                it,
                                action.remainingHintsIds,
                                action.isDailyStep,
                                action.stepId
                            )
                        )

                        userStorageInteractor.updateUserStorage(
                            UserStoragePathBuilder.buildSeenHint(it.targetId, it.id),
                            HintState.SEEN.userStorageValue
                        )
                    }
                    .onFailure {
                        onNewMessage(
                            Message.NextHintLoadingError(
                                action.nextHintId,
                                action.remainingHintsIds,
                                action.isDailyStep,
                                action.stepId
                            )
                        )
                    }
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
        }
    }
}