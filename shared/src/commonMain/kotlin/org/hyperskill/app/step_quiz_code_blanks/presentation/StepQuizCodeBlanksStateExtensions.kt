package org.hyperskill.app.step_quiz_code_blanks.presentation

import org.hyperskill.app.core.utils.indexOfFirstOrNull
import org.hyperskill.app.submissions.domain.model.Reply

internal fun StepQuizCodeBlanksFeature.State.Content.activeCodeBlockIndex(): Int? =
    codeBlocks.indexOfFirstOrNull { codeBlock ->
        codeBlock.isActive || codeBlock.children.any { it.isActive }
    }

internal val StepQuizCodeBlanksFeature.State.isVariableSuggestionsAvailable: Boolean
    get() = (this as? StepQuizCodeBlanksFeature.State.Content)?.step?.let {
        StepQuizCodeBlanksResolver.isVariableSuggestionsAvailable(it)
    } ?: false

fun StepQuizCodeBlanksFeature.State.Content.createReply(): Reply =
    Reply.code(
        code = buildString {
            append("# solved with code blanks\n")
            append(codeBlocks.joinToString(separator = "\n") { it.toReplyString() })
        },
        language = step.block.options.codeTemplates?.keys?.firstOrNull()
    )