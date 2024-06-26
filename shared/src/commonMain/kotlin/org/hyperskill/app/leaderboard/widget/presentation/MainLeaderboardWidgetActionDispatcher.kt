package org.hyperskill.app.leaderboard.widget.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.leaderboard.domain.repository.LeaderboardRepository
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetFeature.Action
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetFeature.InternalMessage
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetFeature.Message
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class MainLeaderboardWidgetActionDispatcher(
    config: ActionDispatcherOptions,
    private val leaderboardRepository: LeaderboardRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            LeaderboardWidgetFeature.InternalAction.FetchLeaderboardData -> {
                handleFetchLeaderboardDataAction(::onNewMessage)
            }
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchLeaderboardDataAction(onNewMessage: (Message) -> Unit) {
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildLeaderboardWidgetRemoteDataLoading(),
            onError = { InternalMessage.FetchLeaderboardDataError }
        ) {
            val currentProfile = currentProfileStateRepository
                .getState(forceUpdate = false)
                .getOrThrow()
            val leaderboard = leaderboardRepository
                .getLeaderboard()
                .getOrThrow()
            InternalMessage.FetchLeaderboardDataSuccess(
                currentUserId = currentProfile.id,
                leaderboard = leaderboard
            )
        }.let(onNewMessage)
    }
}