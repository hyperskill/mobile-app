package org.hyperskill.app.login.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.login.data.repository.UserLoginRepositoryImpl
import org.hyperskill.app.login.domain.interactor.UserLoginInteractor
import org.hyperskill.app.login.presentation.UserLoginDispatcher
import org.hyperskill.app.login.presentation.UserLoginFeature
import org.hyperskill.app.login.presentation.UserLoginReducer
import org.hyperskill.app.login.remote.UserLoginRemoteDataSourceImpl
import org.hyperskill.app.network.injection.NetworkModule
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

class UserLoginFeatureBuilder {
    fun build(): Feature<UserLoginFeature.State, UserLoginFeature.Message, UserLoginFeature.Action> {
        val json = NetworkModule.provideJson()
        val httpClient = NetworkModule.provideClient(json)

        val remoteDataSource = UserLoginRemoteDataSourceImpl(httpClient)
        val repository = UserLoginRepositoryImpl(remoteDataSource)
        val interactor = UserLoginInteractor(repository)

        return ReduxFeature(UserLoginFeature.State.NotAuthorized, UserLoginReducer())
            .wrapWithActionDispatcher(UserLoginDispatcher(ActionDispatcherOptions(), interactor))
    }
}