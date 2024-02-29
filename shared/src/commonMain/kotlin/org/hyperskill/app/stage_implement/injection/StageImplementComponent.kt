package org.hyperskill.app.stage_implement.injection

import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Action
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Message
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface StageImplementComponent {
    val stageImplementFeature: Feature<ViewState, Message, Action>
}