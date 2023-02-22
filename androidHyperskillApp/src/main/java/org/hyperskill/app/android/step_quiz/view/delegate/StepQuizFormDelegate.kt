package org.hyperskill.app.android.step_quiz.view.delegate

import android.content.Context
import com.google.android.material.button.MaterialButton
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature

interface StepQuizFormDelegate {

    fun setState(state: StepQuizFeature.State.AttemptLoaded)

    /**
     * Generates reply from current form data
     */
    fun createReply(): Reply

    fun getQuizDescription(context: Context, state: StepQuizFeature.State.AttemptLoaded): String

    fun customizeSubmissionButton(button: MaterialButton) {}
}