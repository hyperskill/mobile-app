package org.hyperskill.app.profile.presentation

import kotlinx.coroutines.launch
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile.presentation.ProfileFeature.Action
import org.hyperskill.app.profile.presentation.ProfileFeature.Message
import org.hyperskill.app.streak.domain.interactor.StreakInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ProfileActionDispatcher(
    config: ActionDispatcherOptions,
    private val profileInteractor: ProfileInteractor,
    private val streakInteractor: StreakInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val urlPathProcessor: UrlPathProcessor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        actionScope.launch {
            val currentProfile = profileInteractor
                .getCurrentProfile()
                .getOrElse {
                    return@launch
                }
            profileInteractor.solvedStepsSharedFlow.collect { id ->
                if (id == currentProfile.dailyStep) {
                    onNewMessage(Message.DailyStepSolved)
                }
            }
        }
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchCurrentProfile -> {
                val currentProfile = profileInteractor
                    .getCurrentProfile(sourceType = DataSourceType.REMOTE)
                    .getOrElse {
                        onNewMessage(Message.ProfileLoaded.Error(it.message ?: ""))
                        return
                    }

                val message = streakInteractor
                    .getStreaks(currentProfile.id)
                    .map { Message.ProfileLoaded.Success(currentProfile, it.firstOrNull()) }
                    .getOrElse { Message.ProfileLoaded.Error(it.message ?: "") }

                onNewMessage(message)
            }
            is Action.FetchProfile -> {
                // TODO add code when GET on any profile is implemented
            }
            is Action.UpdateStreakInfo -> {
                val currentProfile = profileInteractor
                    .getCurrentProfile()
                    .getOrElse { return }

                val updatedStreak = action.streak?.getStreakWithTodaySolved()

                val message = Message.ProfileLoaded.Success(currentProfile, updatedStreak)
                onNewMessage(message)
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            is Action.GetMagicLink ->
                getLink(action.path, ::onNewMessage)
        }
    }

    private suspend fun getLink(path: HyperskillUrlPath, onNewMessage: (Message) -> Unit): Unit =
        urlPathProcessor.processUrlPath(path)
            .fold(
                onSuccess = { url ->
                    onNewMessage(Message.GetMagicLinkReceiveSuccess(url))
                },
                onFailure = {
                    onNewMessage(Message.GetMagicLinkReceiveFailure)
                }
            )
}