package org.hyperskill.app.progress_screen.injection

import org.hyperskill.app.progress_screen.presentation.ProgressScreenFeature.Action
import org.hyperskill.app.progress_screen.presentation.ProgressScreenFeature.Message
import org.hyperskill.app.progress_screen.view.ProgressScreenViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface ProgressScreenComponent {
    val progressScreenFeature: Feature<ProgressScreenViewState, Message, Action>
}