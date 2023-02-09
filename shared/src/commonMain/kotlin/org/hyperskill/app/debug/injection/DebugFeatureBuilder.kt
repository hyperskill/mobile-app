package org.hyperskill.app.debug.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.debug.domain.interactor.DebugInteractor
import org.hyperskill.app.debug.presentation.DebugActionDispatcher
import org.hyperskill.app.debug.presentation.DebugFeature
import org.hyperskill.app.debug.presentation.DebugReducer
import org.hyperskill.app.debug.view.DebugViewStateMapper
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object DebugFeatureBuilder {
    fun build(
        debugInteractor: DebugInteractor
    ): Feature<DebugFeature.ViewState, DebugFeature.Message, DebugFeature.Action> {
        val debugReducer = DebugReducer()
        val debugActionDispatcher = DebugActionDispatcher(
            ActionDispatcherOptions(),
            debugInteractor
        )

        return ReduxFeature(DebugFeature.State.Idle, debugReducer)
            .transformState(DebugViewStateMapper::mapState)
            .wrapWithActionDispatcher(debugActionDispatcher)
    }
}