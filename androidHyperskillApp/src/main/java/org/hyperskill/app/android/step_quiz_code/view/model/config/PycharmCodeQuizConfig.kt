package org.hyperskill.app.android.step_quiz_code.view.model.config

import org.hyperskill.app.android.step_quiz_code.view.model.CodeDetail
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.pycharmCode
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.app.submissions.domain.model.pycharmCode

class PycharmCodeQuizConfig(private val step: Step) : CodeStepQuizConfig {
    override val langName: String =
        step.block.options.language ?: ""

    override val displayedLangName: String?
        get() = null

    override val initialCode: String =
        step.pycharmCode() ?: ""

    override val codeDetails: List<CodeDetail> =
        emptyList()

    override fun createReply(code: String?): Reply =
        Reply.pycharm(step = step, pycharmCode = code)

    override fun getCode(reply: Reply?): String =
        reply?.pycharmCode() ?: initialCode
}