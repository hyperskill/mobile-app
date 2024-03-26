package org.hyperskill.app.theory_feedback.injection

import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.Action
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.Message
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface TheoryFeedbackComponent {
    val theoryFeedbackFeature: Feature<ViewState, Message, Action>
}