package org.hyperskill.app.step_quiz_hints.injection

import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.step_quiz_hints.view.model.StepQuizHintsViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface StepQuizHintsComponent {
    val stepQuizHintsFeature: Feature<StepQuizHintsViewState, StepQuizHintsFeature.Message, StepQuizHintsFeature.Action>
}