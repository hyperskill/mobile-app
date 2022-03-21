package org.hyperskill.app.user_list.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.user_list.domain.interactor.UserListInteractor
import ru.nobird.app.presentation.redux.feature.Feature
import org.hyperskill.app.user_list.presentation.UsersListFeature.Action
import org.hyperskill.app.user_list.presentation.UsersListFeature.Message
import org.hyperskill.app.user_list.presentation.UsersListFeature.State
import org.hyperskill.app.user_list.presentation.UserListReducer
import org.hyperskill.app.user_list.presentation.UsersListDispatcher
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object UsersListFeatureBuilder {
    fun build(userListInteractor: UserListInteractor): Feature<State, Message, Action> {
        val usersListReducer = UserListReducer()
        val usersListDispatcher = UsersListDispatcher(ActionDispatcherOptions(), userListInteractor)

        return ReduxFeature(State.Idle, usersListReducer)
            .wrapWithActionDispatcher(usersListDispatcher)
    }
}