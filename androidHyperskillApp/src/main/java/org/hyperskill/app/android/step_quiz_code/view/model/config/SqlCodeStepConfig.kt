package org.hyperskill.app.android.step_quiz_code.view.model.config

import org.hyperskill.app.android.step_quiz_code.view.model.CodeDetail
import org.hyperskill.app.code.domain.model.ProgrammingLanguage
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.app.submissions.domain.model.Submission

class SqlCodeStepConfig(private val step: Step) : CodeStepQuizConfig {

    override val langName: String
        get() = ProgrammingLanguage.SQL.languageName

    override val displayedLangName: String?
        get() = null

    override val initialCode: String
        get() = step.block.options.codeTemplates?.get(langName) ?: ""

    override val codeDetails: List<CodeDetail>
        get() = emptyList()

    override fun createReply(code: String?): Reply =
        Reply.sql(code)

    override fun getCode(submission: Submission?): String =
        submission?.reply?.solveSql ?: initialCode
}