package org.hyperskill.app.android.step_quiz.view.delegate

import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature

interface StepQuizFormDelegate {
    fun setState(state: StepQuizFeature.State.AttemptLoaded)

    /**
     * Generates reply from current form data
     */
    fun createReply(): Reply
}