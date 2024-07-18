package org.hyperskill.app.step_quiz_code_blanks.presentation

import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock

internal val StepQuizCodeBlanksFeature.State.Content.activeCodeBlockIndex: Int?
    get() = codeBlocks.indexOfFirst { it.isActive }.takeIf { it != -1 }