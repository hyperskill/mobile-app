package org.hyperskill.app.step_feedback.injection

import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.Action
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.Message
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface StepFeedbackComponent {
    val stepFeedbackFeature: Feature<ViewState, Message, Action>
}