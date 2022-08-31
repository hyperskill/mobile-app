package org.hyperskill.app.placeholder_new_user.injection

import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserActionDispatcher
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.Action
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.Message
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.State
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserReducer
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object PlaceholderNewUserFeatureBuilder {
    fun build(
        profileInteractor: ProfileInteractor,
        analyticInteractor: AnalyticInteractor,
        authorizationFlow: MutableSharedFlow<UserDeauthorized>
    ): Feature<State, Message, Action> {
        val placeholderNewUserReducer = PlaceholderNewUserReducer()
        val placeholderNewUserActionDispatcher = PlaceholderNewUserActionDispatcher(
            ActionDispatcherOptions(),
            profileInteractor,
            analyticInteractor,
            authorizationFlow
        )

        return ReduxFeature(State.Content, placeholderNewUserReducer)
            .wrapWithActionDispatcher(placeholderNewUserActionDispatcher)
    }
}