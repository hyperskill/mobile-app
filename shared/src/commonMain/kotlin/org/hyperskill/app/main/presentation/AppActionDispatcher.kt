package org.hyperskill.app.main.presentation

import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.main.presentation.AppFeature.Action
import org.hyperskill.app.main.presentation.AppFeature.Message
import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class AppActionDispatcher(
    config: ActionDispatcherOptions,
    private val authInteractor: AuthInteractor,
    private val profileInteractor: ProfileInteractor,
    private val onboardingInteractor: OnboardingInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    init {
        authInteractor
            .observeUserDeauthorization()
            .onEach { _ ->
                authInteractor.clearCache()
                onNewMessage(Message.UserDeauthorized)
            }
            .launchIn(actionScope)
    }
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            Action.DetermineUserAccountStatus -> {
                val profileResult = profileInteractor.getCurrentProfile(sourceType = DataSourceType.REMOTE)

                val message =
                    profileResult
                        .map { profile -> Message.UserAccountStatus(profile, onboardingInteractor.isOnboardingShown()) }
                        .getOrElse { Message.UserAccountStatusError }

                onNewMessage(message)
            }
        }
    }
}