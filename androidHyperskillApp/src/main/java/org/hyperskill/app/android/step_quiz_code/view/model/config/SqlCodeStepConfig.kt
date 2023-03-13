package org.hyperskill.app.android.step_quiz_code.view.model.config

import org.hyperskill.app.android.code.presentation.model.ProgrammingLanguage
import org.hyperskill.app.android.step_quiz_code.view.model.CodeDetail
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission

class SqlCodeStepConfig(private val step: Step) : CodeStepQuizConfig {

    override val langName: String
        get() = ProgrammingLanguage.SQL.serverPrintableName

    override val initialCode: String
        get() = step.block.options.codeTemplates?.get(langName) ?: ""

    override val codeDetails: List<CodeDetail>
        get() = emptyList()

    override fun createReply(code: String?): Reply =
        Reply.sql(code)

    override fun getCode(submission: Submission?): String =
        submission?.reply?.solveSql ?: initialCode
}