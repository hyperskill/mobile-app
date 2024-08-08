package org.hyperskill.app.step_quiz_code_blanks.presentation

import org.hyperskill.app.submissions.domain.model.Reply

internal fun StepQuizCodeBlanksFeature.State.Content.activeCodeBlockIndex(): Int? =
    codeBlocks
        .indexOfFirst { codeBlock ->
            codeBlock.isActive || codeBlock.children.any { it.isActive }
        }
        .takeIf { it != -1 }

internal val StepQuizCodeBlanksFeature.State.isVariableSuggestionsAvailable: Boolean
    get() = (this as? StepQuizCodeBlanksFeature.State.Content)?.step?.let {
        StepQuizCodeBlanksFeature.isVariableSuggestionsAvailable(it)
    } ?: false

fun StepQuizCodeBlanksFeature.State.Content.createReply(): Reply =
    Reply.code(
        code = codeBlocks.joinToString(separator = "\n") { it.toString() },
        language = step.block.options.codeTemplates?.keys?.firstOrNull()
    )