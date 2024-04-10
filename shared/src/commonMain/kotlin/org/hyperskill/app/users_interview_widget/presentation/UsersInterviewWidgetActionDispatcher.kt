package org.hyperskill.app.users_interview_widget.presentation

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.model.isMobileUsersInterviewWidgetEnabled
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.users_interview_widget.domain.repository.UsersInterviewWidgetRepository
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature.Action
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature.InternalAction
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature.InternalMessage
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class UsersInterviewWidgetActionDispatcher(
    config: ActionDispatcherOptions,
    private val usersInterviewWidgetRepository: UsersInterviewWidgetRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val analyticInteractor: AnalyticInteractor
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
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
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

    private suspend fun handleFetchUsersInterviewUrlAction(onNewMessage: (Message) -> Unit) {
        val currentUserId = currentProfileStateRepository
            .getState(forceUpdate = false)
            .map { it.id }
            .getOrNull() ?: return

        onNewMessage(
            InternalMessage.FetchUsersInterviewUrlResult(
                "https://docs.google.com/forms/d/e/1FAIpQLSf6k3woOqZr2zfmbBNvA71DyD04LN4v7l6k-vuyqdAmdMUnOA/viewform?usp=pp_url&entry.193481738=$currentUserId" // ktlint-disable
            )
        )
    }
}