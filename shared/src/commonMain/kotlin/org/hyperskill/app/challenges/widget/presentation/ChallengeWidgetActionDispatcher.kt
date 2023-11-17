package org.hyperskill.app.challenges.widget.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.challenges.domain.repository.ChallengesRepository
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature.Action
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature.InternalAction
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature.InternalMessage
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature.Message
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.magic_links.domain.interactor.MagicLinksInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ChallengeWidgetActionDispatcher(
    config: ActionDispatcherOptions,
    private val challengesRepository: ChallengesRepository,
    private val magicLinksInteractor: MagicLinksInteractor,
    private val sentryInteractor: SentryInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            InternalAction.FetchChallenges ->
                handleFetchChallengesAction(::onNewMessage)
            is InternalAction.CreateMagicLink ->
                handleCreateMagicLinkAction(action, ::onNewMessage)
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchChallengesAction(onNewMessage: (Message) -> Unit) {
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildChallengeWidgetFeatureFetchChallenges(),
            onError = { InternalMessage.FetchChallengesError }
        ) {
            challengesRepository
                .getChallenges()
                .getOrThrow()
                .let(InternalMessage::FetchChallengesSuccess)
        }.let(onNewMessage)
    }

    private suspend fun handleCreateMagicLinkAction(
        action: InternalAction.CreateMagicLink,
        onNewMessage: (Message) -> Unit
    ) {
        magicLinksInteractor
            .createMagicLink(nextUrl = action.nextUrl)
            .fold(
                onSuccess = { magicLink ->
                    onNewMessage(InternalMessage.CreateMagicLinkSuccess(url = magicLink.url))
                },
                onFailure = {
                    onNewMessage(InternalMessage.CreateMagicLinkFailure)
                }
            )
    }
}