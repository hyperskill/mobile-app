package org.hyperskill.app.debug.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.debug.domain.interactor.DebugInteractor
import org.hyperskill.app.debug.presentation.DebugActionDispatcher
import org.hyperskill.app.debug.presentation.DebugFeature
import org.hyperskill.app.debug.presentation.DebugReducer
import org.hyperskill.app.debug.view.DebugViewStateMapper
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.main.domain.interactor.AppInteractor
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object DebugFeatureBuilder {
    private const val LOG_TAG = "DebugFeature"

    fun build(
        debugInteractor: DebugInteractor,
        appInteractor: AppInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<DebugFeature.ViewState, DebugFeature.Message, DebugFeature.Action> {
        val debugReducer = DebugReducer()
        val debugActionDispatcher = DebugActionDispatcher(
            ActionDispatcherOptions(),
            debugInteractor,
            appInteractor
        )

        return ReduxFeature(
            DebugFeature.State.Idle,
            debugReducer.wrapWithLogger(buildVariant, logger, LOG_TAG)
        )
            .transformState(DebugViewStateMapper::mapState)
            .wrapWithActionDispatcher(debugActionDispatcher)
    }
}