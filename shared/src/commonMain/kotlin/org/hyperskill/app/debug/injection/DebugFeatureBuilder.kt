package org.hyperskill.app.debug.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.debug.domain.interactor.DebugInteractor
import org.hyperskill.app.debug.presentation.DebugActionDispatcher
import org.hyperskill.app.debug.presentation.DebugFeature.Action
import org.hyperskill.app.debug.presentation.DebugFeature.Message
import org.hyperskill.app.debug.presentation.DebugFeature.State
import org.hyperskill.app.debug.presentation.DebugReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object DebugFeatureBuilder {
    fun build(debugInteractor: DebugInteractor): Feature<State, Message, Action> {
        val debugReducer = DebugReducer()
        val debugActionDispatcher = DebugActionDispatcher(
            ActionDispatcherOptions(),
            debugInteractor
        )

        return ReduxFeature(State.Idle, debugReducer)
            .wrapWithActionDispatcher(debugActionDispatcher)
    }
}