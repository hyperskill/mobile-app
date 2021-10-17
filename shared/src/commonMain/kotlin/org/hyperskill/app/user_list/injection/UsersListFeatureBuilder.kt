package org.hyperskill.app.user_list.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.network.injection.NetworkModule
import org.hyperskill.app.user_list.data.repository.UserListRepositoryImpl
import org.hyperskill.app.user_list.domain.interactor.UserListInteractor
import ru.nobird.app.presentation.redux.feature.Feature
import org.hyperskill.app.user_list.presentation.UsersListFeature.Action
import org.hyperskill.app.user_list.presentation.UsersListFeature.Message
import org.hyperskill.app.user_list.presentation.UsersListFeature.State
import org.hyperskill.app.user_list.presentation.UserListReducer
import org.hyperskill.app.user_list.presentation.UsersListDispatcher
import org.hyperskill.app.user_list.remote.UserListRemoteDataSourceImpl
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object UsersListFeatureBuilder {
    fun build(): Feature<State, Message, Action> {
        val json = NetworkModule.provideJson()
        val httpClient = NetworkModule.provideClient(json)

        val remoteDataSource = UserListRemoteDataSourceImpl(httpClient)
        val repository = UserListRepositoryImpl(remoteDataSource)
        val interactor = UserListInteractor(repository)

        return ReduxFeature(State.Idle, UserListReducer())
            .wrapWithActionDispatcher(UsersListDispatcher(ActionDispatcherOptions(), interactor))
    }
}