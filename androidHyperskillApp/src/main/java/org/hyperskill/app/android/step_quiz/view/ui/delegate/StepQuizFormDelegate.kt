package org.hyperskill.app.android.step_quiz.view.ui.delegate

interface StepQuizFormDelegate {
    fun setState(state: Unit)

    /**
     * Generates reply from current form data
     */
    fun createReply(): Unit
}