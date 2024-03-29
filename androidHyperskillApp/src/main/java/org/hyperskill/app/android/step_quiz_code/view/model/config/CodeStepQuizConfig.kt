package org.hyperskill.app.android.step_quiz_code.view.model.config

import org.hyperskill.app.android.step_quiz_code.view.model.CodeDetail
import org.hyperskill.app.submissions.domain.model.Reply

interface CodeStepQuizConfig {

    val langName: String

    val displayedLangName: String?

    val initialCode: String

    val codeDetails: List<CodeDetail>

    fun createReply(code: String?): Reply

    fun getCode(reply: Reply?): String
}