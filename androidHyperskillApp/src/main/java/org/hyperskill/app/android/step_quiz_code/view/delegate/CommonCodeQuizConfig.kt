package org.hyperskill.app.android.step_quiz_code.view.delegate

import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission

class CommonCodeQuizConfig(step: Step) : CodeStepQuizConfig {

    override val langName: String =
        requireNotNull(step.block.options.limits).keys.first()

    override val initialCode: String =
        step.block.options.codeTemplates?.get(langName) ?: ""

    override fun createReply(code: String?): Reply =
        Reply(code = code, language = langName)

    override fun getCode(submission: Submission?): String =
        submission?.reply?.code ?: initialCode
}