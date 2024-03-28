package org.hyperskill.app.android.step_quiz.view.delegate

import com.google.android.material.button.MaterialButton
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.submissions.domain.model.Reply

interface StepQuizFormDelegate {

    fun setState(state: StepQuizFeature.StepQuizState.AttemptLoaded)

    /**
     * Generates reply from current form data
     */
    fun createReply(): Reply

    fun customizeSubmissionButton(button: MaterialButton) {}
}