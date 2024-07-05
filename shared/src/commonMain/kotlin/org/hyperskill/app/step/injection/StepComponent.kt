package org.hyperskill.app.step.injection

import org.hyperskill.app.step.presentation.StepFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface StepComponent {
    val stepFeature: Feature<StepFeature.ViewState, StepFeature.Message, StepFeature.Action>
}