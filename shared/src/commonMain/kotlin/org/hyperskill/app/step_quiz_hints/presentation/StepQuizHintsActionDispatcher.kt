package org.hyperskill.app.step_quiz_hints.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.comments.domain.interactor.CommentsInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.likes.domain.interactor.LikesInteractor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile.domain.model.isFreemiumFeatureEnabled
import org.hyperskill.app.reactions.domain.interactor.ReactionsInteractor
import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.step_quiz_hints.domain.interactor.StepQuizHintsInteractor
import org.hyperskill.app.step_quiz_hints.domain.model.HintState
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Action
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Message
import org.hyperskill.app.subscriptions.domain.model.isFree
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.user_storage.domain.interactor.UserStorageInteractor
import org.hyperskill.app.user_storage.domain.model.UserStoragePathBuilder
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class StepQuizHintsActionDispatcher(
    config: ActionDispatcherOptions,
    private val stepQuizHintsInteractor: StepQuizHintsInteractor,
    private val profileInteractor: ProfileInteractor,
    private val likesInteractor: LikesInteractor,
    private val commentsInteractor: CommentsInteractor,
    private val reactionsInteractor: ReactionsInteractor,
    private val userStorageInteractor: UserStorageInteractor,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchHintsIds -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildStepQuizHintsScreenRemoteDataLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                val hintsIds = stepQuizHintsInteractor.getNotSeenHintsIds(action.stepId)

                val isFreemiumFeatureEnabled = profileInteractor
                    .getCurrentProfile()
                    .getOrNull()?.isFreemiumFeatureEnabled ?: false
                val isFreeSubscription = currentSubscriptionStateRepository
                    .getState()
                    .getOrNull()?.isFree ?: false

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
                        isFreemium = isFreemiumFeatureEnabled && isFreeSubscription,
                        stepId = action.stepId
                    )
                )
            }
            is Action.ReportHint -> {
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
            is Action.ReactHint -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildStepQuizHintsReactHint()
                sentryInteractor.startTransaction(sentryTransaction)

                reactionsInteractor
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
                            sentryInteractor.finishTransaction(sentryTransaction)

                            onNewMessage(Message.ReactHintSuccess)
                        },
                        onFailure = {
                            sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                            onNewMessage(Message.ReactHintFailure)
                        }
                    )
            }
            is Action.FetchNextHint -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildStepQuizHintsFetchNextHint()
                sentryInteractor.startTransaction(sentryTransaction)

                commentsInteractor
                    .getComment(action.nextHintId)
                    .onSuccess {
                        onNewMessage(
                            Message.NextHintLoaded(
                                it,
                                action.remainingHintsIds,
                                action.isFreemium,
                                action.stepId
                            )
                        )

                        userStorageInteractor.updateUserStorage(
                            UserStoragePathBuilder.buildSeenHint(it.targetId, it.id),
                            HintState.SEEN.userStorageValue
                        )
                        sentryInteractor.finishTransaction(sentryTransaction)
                    }
                    .onFailure {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        onNewMessage(
                            Message.NextHintLoadingError(
                                action.nextHintId,
                                action.remainingHintsIds,
                                action.isFreemium,
                                action.stepId
                            )
                        )
                    }
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {}
        }
    }
}