package org.hyperskill.app.android.step_quiz_code.view.model

import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.pycharmCode
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz.domain.model.submissions.pycharmCode

class PycharmCodeQuizConfig(private val step: Step) : CodeStepQuizConfig {
    override val langName: String =
        step.block.options.language ?: ""

    override val initialCode: String =
        step.pycharmCode() ?: ""

    override fun createReply(code: String?): Reply =
        Reply.pycharm(step = step, pycharmCode = code)

    override fun getCode(submission: Submission?): String =
        submission?.reply?.pycharmCode() ?: ""
}