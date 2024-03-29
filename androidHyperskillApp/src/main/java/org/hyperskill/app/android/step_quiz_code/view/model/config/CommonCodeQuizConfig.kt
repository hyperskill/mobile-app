package org.hyperskill.app.android.step_quiz_code.view.model.config

import org.hyperskill.app.android.step_quiz_code.view.model.CodeDetail
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz.view.displayLanguage
import org.hyperskill.app.submissions.domain.model.Reply

class CommonCodeQuizConfig(private val step: Step) : CodeStepQuizConfig {

    override val langName: String =
        requireNotNull(step.block.options.codeTemplates).keys.first()

    override val displayedLangName: String?
        get() = step.displayLanguage

    override val initialCode: String =
        step.block.options.codeTemplates?.get(langName) ?: ""

    override val codeDetails: List<CodeDetail>
        get() = step.block.options.samples
            ?.mapIndexed { i, sample ->
                CodeDetail.Sample(
                    i + 1,
                    sample.first().trim('\n'),
                    sample.last().trim('\n')
                )
            } ?: emptyList()

    override fun createReply(code: String?): Reply =
        Reply.code(code = code, language = langName)

    override fun getCode(reply: Reply?): String =
        reply?.code ?: initialCode
}