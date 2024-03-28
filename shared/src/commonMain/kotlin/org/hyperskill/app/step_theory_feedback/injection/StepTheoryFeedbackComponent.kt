package org.hyperskill.app.step_theory_feedback.injection

import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackFeature.Action
import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackFeature.Message
import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface StepTheoryFeedbackComponent {
    val stepTheoryFeedbackFeature: Feature<ViewState, Message, Action>
}