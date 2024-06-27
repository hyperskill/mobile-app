package org.hyperskill.app.comments.screen.presentation

import org.hyperskill.app.comments.screen.domain.interactor.CommentsScreenInteractor
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.Action
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.InternalAction
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.InternalMessage
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.Message
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class CommentsScreenActionDispatcher(
    config: ActionDispatcherOptions,
    private val commentsScreenInteractor: CommentsScreenInteractor,
    private val sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchDiscussions -> handleFetchDiscussionsAction(action, ::onNewMessage)
        }
    }

    private suspend fun handleFetchDiscussionsAction(
        action: InternalAction.FetchDiscussions,
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildCommentsScreenFeatureFetchDiscussions(),
            onError = { InternalMessage.DiscussionsFetchError }
        ) {
            commentsScreenInteractor
                .getDiscussionsWithRootComments(stepId = action.stepId, page = action.page)
                .getOrThrow()
                .let { (discussionsResponse, rootComments) ->
                    InternalMessage.DiscussionsFetchSuccess(discussionsResponse, rootComments)
                }
        }.let(onNewMessage)
    }
}