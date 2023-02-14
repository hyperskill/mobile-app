package org.hyperskill.app.debug.injection

import org.hyperskill.app.debug.presentation.DebugFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface DebugComponent {
    val debugFeature: Feature<DebugFeature.ViewState, DebugFeature.Message, DebugFeature.Action>
}