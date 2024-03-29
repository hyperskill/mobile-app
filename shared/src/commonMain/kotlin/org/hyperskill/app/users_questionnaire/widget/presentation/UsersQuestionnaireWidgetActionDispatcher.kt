package org.hyperskill.app.users_questionnaire.widget.presentation

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.model.isMobileUsersQuestionnaireEnabled
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.users_questionnaire.domain.repository.UsersQuestionnaireRepository
import org.hyperskill.app.users_questionnaire.widget.presentation.UsersQuestionnaireWidgetFeature.Action
import org.hyperskill.app.users_questionnaire.widget.presentation.UsersQuestionnaireWidgetFeature.InternalAction
import org.hyperskill.app.users_questionnaire.widget.presentation.UsersQuestionnaireWidgetFeature.InternalMessage
import org.hyperskill.app.users_questionnaire.widget.presentation.UsersQuestionnaireWidgetFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class UsersQuestionnaireWidgetActionDispatcher(
    config: ActionDispatcherOptions,
    private val usersQuestionnaireRepository: UsersQuestionnaireRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    init {
        currentProfileStateRepository.changes
            .map { it.features.isMobileUsersQuestionnaireEnabled }
            .distinctUntilChanged()
            .onEach { isMobileUsersQuestionnaireEnabled ->
                onNewMessage(
                    InternalMessage.UsersQuestionnaireFeatureFlagChanged(
                        isMobileUsersQuestionnaireEnabled
                    )
                )
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            InternalAction.FetchUsersQuestionnaireWidgetData ->
                handleFetchUsersQuestionnaireWidgetDataAction(::onNewMessage)
            InternalAction.FetchUsersQuestionnaireUrl ->
                handleFetchUsersQuestionnaireUrlAction(::onNewMessage)
            InternalAction.HideUsersQuestionnaireWidget ->
                usersQuestionnaireRepository.setIsUsersQuestionnaireWidgetHidden(true)
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchUsersQuestionnaireWidgetDataAction(onNewMessage: (Message) -> Unit) {
        val isMobileUsersQuestionnaireEnabled = currentProfileStateRepository
            .getState(forceUpdate = false)
            .map { it.features.isMobileUsersQuestionnaireEnabled }
            .getOrDefault(defaultValue = false)

        onNewMessage(
            InternalMessage.FetchUsersQuestionnaireWidgetDataResult(
                isUsersQuestionnaireEnabled = isMobileUsersQuestionnaireEnabled,
                isUsersQuestionnaireWidgetHidden = usersQuestionnaireRepository.getIsUsersQuestionnaireWidgetHidden()
            )
        )
    }

    private suspend fun handleFetchUsersQuestionnaireUrlAction(onNewMessage: (Message) -> Unit) {
        val currentUserId = currentProfileStateRepository
            .getState(forceUpdate = false)
            .map { it.id }
            .getOrNull() ?: return

        onNewMessage(
            InternalMessage.FetchUsersQuestionnaireUrlResult(
                "https://docs.google.com/forms/d/e/1FAIpQLSf6k3woOqZr2zfmbBNvA71DyD04LN4v7l6k-vuyqdAmdMUnOA/viewform?usp=pp_url&entry.193481738=$currentUserId" // ktlint-disable
            )
        )
    }
}