package org.hyperskill.app.stage_implement.injection

import org.hyperskill.app.stage_implement.presentation.StageImplementFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface StageImplementComponent {
    val stageImplementFeature: Feature<
        StageImplementFeature.ViewState, StageImplementFeature.Message, StageImplementFeature.Action>
}