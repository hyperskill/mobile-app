package org.hyperskill.app.app.injection

import org.hyperskill.app.app.presentation.AppActionDispatcher
import org.hyperskill.app.app.presentation.AppFeature.Action
import org.hyperskill.app.app.presentation.AppFeature.Message
import org.hyperskill.app.app.presentation.AppFeature.State
import org.hyperskill.app.app.presentation.AppReducer
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object AppFeatureBuilder {
    fun build(): Feature<State, Message, Action> {
        val appReducer = AppReducer()
        val appActionDispatcher = AppActionDispatcher(ActionDispatcherOptions())

        return ReduxFeature(State.Idle, appReducer)
            .wrapWithActionDispatcher(appActionDispatcher)
    }
}