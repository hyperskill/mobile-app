package org.hyperskill.app.progresses.injection

import org.hyperskill.app.progresses.presentation.ProgressScreenFeature.Action
import org.hyperskill.app.progresses.presentation.ProgressScreenFeature.Message
import org.hyperskill.app.progresses.view.ProgressScreenViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface ProgressScreenComponent {
    val progressScreenFeature: Feature<ProgressScreenViewState, Message, Action>
}