package org.hyperskill.app.main.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.main.presentation.AppActionDispatcher
import org.hyperskill.app.main.presentation.AppFeature.Action
import org.hyperskill.app.main.presentation.AppFeature.Message
import org.hyperskill.app.main.presentation.AppFeature.State
import org.hyperskill.app.main.presentation.AppReducer
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