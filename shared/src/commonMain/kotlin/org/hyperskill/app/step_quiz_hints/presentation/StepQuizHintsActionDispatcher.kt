package org.hyperskill.app.step_quiz_hints.presentation

import org.hyperskill.app.comments.domain.interactor.CommentsInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.likes.domain.interactor.LikesInteractor
import org.hyperskill.app.reactions.domain.interactor.ReactionsInteractor
import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.step_quiz_hints.domain.model.HintState
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Action
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Message
import org.hyperskill.app.user_storage.domain.interactor.UserStorageInteractor
import org.hyperskill.app.user_storage.domain.model.UserStoragePathBuilder
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepQuizHintsActionDispatcher(
    config: ActionDispatcherOptions,
    private val likesInteractor: LikesInteractor,
    private val commentsInteractor: CommentsInteractor,
    private val reactionsInteractor: ReactionsInteractor,
    private val userStorageInteractor: UserStorageInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.ReportHint -> {
                likesInteractor.abuseComment(action.hintId)

                userStorageInteractor.updateUserStorage(
                    UserStoragePathBuilder.buildSeenHint(action.stepId, action.hintId),
                    HintState.UNHELPFUL.userStorageValue
                )
            }
            is Action.ReactHint -> {
                reactionsInteractor.createReaction(action.hintId, action.reaction)

                userStorageInteractor.updateUserStorage(
                    UserStoragePathBuilder.buildSeenHint(action.stepId, action.hintId),
                    when (action.reaction) {
                        ReactionType.HELPFUL -> HintState.HELPFUL.userStorageValue
                        ReactionType.UNHELPFUL -> HintState.UNHELPFUL.userStorageValue
                    }
                )
            }
            is Action.FetchNextHint -> {
                commentsInteractor
                    .getCommentDetails(action.nextHintId)
                    .onSuccess {
                        onNewMessage(Message.NextHintLoaded(it, action.remainingHintsIds))

                        userStorageInteractor.updateUserStorage(
                            UserStoragePathBuilder.buildSeenHint(it.targetId, it.id),
                            HintState.SEEN.userStorageValue
                        )
                    }
            }
        }
    }
}