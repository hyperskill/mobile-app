package org.hyperskill.app.step_quiz_hints.domain.interactor

import org.hyperskill.app.comments.domain.interactor.CommentsDataInteractor
import org.hyperskill.app.comments.domain.model.ReactionType
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.step_quiz_hints.domain.model.HintState
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Action
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Message
import org.hyperskill.app.user_storage.domain.interactor.UserStorageInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepQuizHintsActionDispatcher(
    config: ActionDispatcherOptions,
    private val commentsDataInteractor: CommentsDataInteractor,
    private val userStorageInteractor: UserStorageInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    companion object {
        const val HINTS_USER_STORAGE_KEY = "seenHints"
    }
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.ReportHint -> {
                commentsDataInteractor.abuseComment(action.hintID)

                userStorageInteractor.updateUserStorage(
                    "$HINTS_USER_STORAGE_KEY.${action.stepID}.${action.hintID}",
                    HintState.UNHELPFULL.userStorageValue
                )
            }
            is Action.ReactHint -> {
                commentsDataInteractor.createReaction(action.hintID, action.reaction)

                userStorageInteractor.updateUserStorage(
                    "$HINTS_USER_STORAGE_KEY.${action.stepID}.${action.hintID}",
                    when (action.reaction) {
                        ReactionType.HELPFULL -> HintState.HELPFUL.userStorageValue
                        ReactionType.UNHELPFULL -> HintState.UNHELPFULL.userStorageValue
                    }
                )
            }
            is Action.FetchNextHint -> {
                commentsDataInteractor
                    .getCommentDetails(action.nextHintID)
                    .onSuccess {
                        onNewMessage(Message.NextHintLoaded(it, action.remainingHintsIDs))

                        userStorageInteractor.updateUserStorage(
                            "$HINTS_USER_STORAGE_KEY.${it.targetID}.${it.id}",
                            HintState.SEEN.userStorageValue
                        )
                    }
            }
        }
    }
}