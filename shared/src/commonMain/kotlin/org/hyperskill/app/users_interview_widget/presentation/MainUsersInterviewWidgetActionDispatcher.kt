package org.hyperskill.app.users_interview_widget.presentation

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.model.isMobileUsersInterviewWidgetEnabled
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.users_interview_widget.domain.repository.UsersInterviewWidgetRepository
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature.Action
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature.InternalAction
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature.InternalMessage
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class MainUsersInterviewWidgetActionDispatcher(
    config: ActionDispatcherOptions,
    private val usersInterviewWidgetRepository: UsersInterviewWidgetRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    init {
        currentProfileStateRepository.changes
            .map { it.features.isMobileUsersInterviewWidgetEnabled }
            .distinctUntilChanged()
            .onEach { isMobileUsersInterviewWidgetEnabled ->
                onNewMessage(
                    InternalMessage.UsersInterviewWidgetFeatureFlagChanged(
                        isMobileUsersInterviewWidgetEnabled
                    )
                )
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            InternalAction.FetchUsersInterviewWidgetData ->
                handleFetchUsersInterviewWidgetDataAction(::onNewMessage)
            InternalAction.FetchUsersInterviewUrl ->
                handleFetchUsersInterviewUrlAction(::onNewMessage)
            InternalAction.HideUsersInterviewWidget ->
                usersInterviewWidgetRepository.setIsUsersInterviewWidgetHidden(true)
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchUsersInterviewWidgetDataAction(onNewMessage: (Message) -> Unit) {
        val isUsersInterviewWidgetEnabled = currentProfileStateRepository
            .getState(forceUpdate = false)
            .map { it.features.isMobileUsersInterviewWidgetEnabled }
            .getOrDefault(defaultValue = false)

        onNewMessage(
            InternalMessage.FetchUsersInterviewWidgetDataResult(
                isUsersInterviewWidgetEnabled = isUsersInterviewWidgetEnabled,
                isUsersInterviewWidgetHidden = usersInterviewWidgetRepository.getIsUsersInterviewWidgetHidden()
            )
        )
    }

    private fun handleFetchUsersInterviewUrlAction(onNewMessage: (Message) -> Unit) {
        onNewMessage(
            InternalMessage.FetchUsersInterviewUrlResult(
                "https://docs.google.com/forms/d/1qRTBkpjwZhjWSQ0sn9WtN0lwAU0quygsm5iPapw1AcM/"
            )
        )
    }
}