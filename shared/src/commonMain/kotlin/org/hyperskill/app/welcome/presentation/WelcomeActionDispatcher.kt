package org.hyperskill.app.welcome.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.welcome.domain.interactor.WelcomeInteractor
import org.hyperskill.app.welcome.presentation.WelcomeFeature.Action
import org.hyperskill.app.welcome.presentation.WelcomeFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class WelcomeActionDispatcher(
    config: ActionDispatcherOptions,
    private val welcomeInteractor: WelcomeInteractor,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchProfile -> {
                welcomeInteractor.setWelcomeScreenShown(true)
                currentProfileStateRepository
                    .getState()
                    .fold(
                        onSuccess = { onNewMessage(Message.ProfileFetchSuccess(it)) },
                        onFailure = { onNewMessage(Message.ProfileFetchFailure) }
                    )
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {}
        }
    }
}