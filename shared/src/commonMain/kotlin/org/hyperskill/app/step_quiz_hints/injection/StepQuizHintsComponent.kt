package org.hyperskill.app.step_quiz_hints.injection

import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface StepQuizHintsComponent {
    val stepQuizHintsFeature: Feature<
        StepQuizHintsFeature.ViewState, StepQuizHintsFeature.Message, StepQuizHintsFeature.Action>
}