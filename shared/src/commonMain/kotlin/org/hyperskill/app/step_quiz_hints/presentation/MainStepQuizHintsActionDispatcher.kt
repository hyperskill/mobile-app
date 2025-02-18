package org.hyperskill.app.step_quiz_hints.presentation

import org.hyperskill.app.comments.domain.repository.CommentsRepository
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.likes.domain.interactor.LikesInteractor
import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.reactions.domain.repository.ReactionsRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.step_quiz_hints.domain.interactor.StepQuizHintsInteractor
import org.hyperskill.app.step_quiz_hints.domain.model.HintState
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Action
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.InternalAction
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Message
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.user_storage.domain.interactor.UserStorageInteractor
import org.hyperskill.app.user_storage.domain.model.UserStoragePathBuilder
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class MainStepQuizHintsActionDispatcher(
    config: ActionDispatcherOptions,
    private val stepQuizHintsInteractor: StepQuizHintsInteractor,
    private val likesInteractor: LikesInteractor,
    private val commentsRepository: CommentsRepository,
    private val reactionsRepository: ReactionsRepository,
    private val userStorageInteractor: UserStorageInteractor,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchHintsIds -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildStepQuizHintsScreenRemoteDataLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                val hintsIds = stepQuizHintsInteractor.getNotSeenHintsIds(action.stepId)

                val areHintsLimited =
                    currentSubscriptionStateRepository
                        .getState()
                        .getOrNull()
                        ?.type
                        ?.areHintsLimited
                        ?: false

                val lastSeenHint = stepQuizHintsInteractor.getLastSeenHint(action.stepId)

                val lastSeenHintHasReaction = if (lastSeenHint != null) {
                    stepQuizHintsInteractor
                        .getCachedHintState(action.stepId, lastSeenHint.id)
                        .map { it?.hasReaction ?: false }
                        .getOrDefault(false)
                } else {
                    false
                }

                sentryInteractor.finishTransaction(sentryTransaction)

                onNewMessage(
                    Message.HintsIdsLoaded(
                        hintsIds = hintsIds,
                        lastSeenHint = lastSeenHint,
                        lastSeenHintHasReaction = lastSeenHintHasReaction,
                        areHintsLimited = areHintsLimited,
                        stepId = action.stepId
                    )
                )
            }
            is InternalAction.ReportHint -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildStepQuizHintsReportHint()
                sentryInteractor.startTransaction(sentryTransaction)

                likesInteractor
                    .abuseComment(action.hintId)
                    .fold(
                        onSuccess = {
                            userStorageInteractor.updateUserStorage(
                                UserStoragePathBuilder.buildSeenHint(action.stepId, action.hintId),
                                HintState.UNHELPFUL.userStorageValue
                            )
                            sentryInteractor.finishTransaction(sentryTransaction)

                            onNewMessage(Message.ReportHintSuccess)
                        },
                        onFailure = {
                            sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                            onNewMessage(Message.ReportHintFailure)
                        }
                    )
            }
            is InternalAction.ReactHint -> handleReactHintAction(action, ::onNewMessage)
            is InternalAction.FetchNextHint -> handleFetchNextHintAction(action, ::onNewMessage)
            else -> {}
        }
    }

    private suspend fun handleReactHintAction(
        action: InternalAction.ReactHint,
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildStepQuizHintsReactHint(),
            onError = { Message.ReactHintFailure }
        ) {
            val createdReaction = reactionsRepository
                .createCommentReaction(action.hintId, action.reaction)
                .getOrThrow()

            userStorageInteractor.updateUserStorage(
                key = UserStoragePathBuilder.buildSeenHint(action.stepId, action.hintId),
                value = when (createdReaction.reactionType) {
                    ReactionType.HELPFUL -> HintState.HELPFUL.userStorageValue
                    ReactionType.UNHELPFUL -> HintState.UNHELPFUL.userStorageValue
                    else -> {
                        error("Unknown reaction type: $action.reaction")
                    }
                }
            )

            Message.ReactHintSuccess
        }.let(onNewMessage)
    }

    private suspend fun handleFetchNextHintAction(
        action: InternalAction.FetchNextHint,
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildStepQuizHintsFetchNextHint(),
            onError = {
                Message.NextHintLoadingError(
                    action.nextHintId,
                    action.remainingHintsIds,
                    action.areHintsLimited,
                    action.stepId
                )
            }
        ) {
            val comment = commentsRepository
                .getComment(action.nextHintId)
                .getOrThrow()

            userStorageInteractor.updateUserStorage(
                UserStoragePathBuilder.buildSeenHint(stepId = comment.targetId, hintId = comment.id),
                HintState.SEEN.userStorageValue
            )

            Message.NextHintLoaded(
                comment,
                action.remainingHintsIds,
                action.areHintsLimited,
                action.stepId
            )
        }.let(onNewMessage)
    }
}