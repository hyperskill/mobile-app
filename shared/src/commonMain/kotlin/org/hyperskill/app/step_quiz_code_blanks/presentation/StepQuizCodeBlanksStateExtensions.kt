package org.hyperskill.app.step_quiz_code_blanks.presentation

import org.hyperskill.app.submissions.domain.model.Reply

internal fun StepQuizCodeBlanksFeature.State.Content.activeCodeBlockIndex(): Int? =
    codeBlocks.indexOfFirst { it.isActive }.takeIf { it != -1 }

fun StepQuizCodeBlanksFeature.State.Content.createReply(): Reply =
    Reply.code(
        code = codeBlocks.joinToString(separator = "\n") { it.toString() },
        language = step.block.options.codeTemplates?.keys?.firstOrNull()
    )