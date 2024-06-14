package org.hyperskill.app.welcome_onboarding.track_details.presentation

import co.touchlab.kermit.Logger
import kotlinx.coroutines.delay
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.Action
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.InternalAction
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.InternalMessage
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class WelcomeOnboardingTrackDetailsActionDispatcher(
    config: ActionDispatcherOptions,
    private val profileRepository: ProfileRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val analyticInteractor: AnalyticInteractor,
    private val logger: Logger
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.event)
            is InternalAction.SelectTrack -> handleSelectTrack(action, ::onNewMessage)
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleSelectTrack(
        action: InternalAction.SelectTrack,
        onNewMessage: (InternalMessage) -> Unit
    ) {
        selectTrack(action.trackId)
            .fold(
                onSuccess = {
                    delay(2000L)
                    InternalMessage.SelectTrackSuccess
                },
                onFailure = { e ->
                    logger.e(e) { "Failed to select track with id=${action.trackId}" }
                    InternalMessage.SelectTrackFailed
                }
            )
            .let(onNewMessage)
    }

    private suspend fun selectTrack(trackId: Long): Result<Unit> =
        runCatching {
            val currentProfile = currentProfileStateRepository.getState().getOrThrow()
            val newProfile = profileRepository.selectTrack(currentProfile.id, trackId).getOrThrow()
            currentProfileStateRepository.updateState(newProfile)
        }
}