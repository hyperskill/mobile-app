package org.hyperskill.app.android.step_quiz_code.view.delegate

import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission

interface CodeStepQuizConfig {

    val langName: String

    val initialCode: String

    fun createReply(code: String?): Reply

    fun getCode(submission: Submission?): String
}