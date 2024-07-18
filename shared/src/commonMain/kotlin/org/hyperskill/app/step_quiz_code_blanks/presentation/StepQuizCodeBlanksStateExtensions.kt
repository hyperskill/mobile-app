package org.hyperskill.app.step_quiz_code_blanks.presentation

internal val StepQuizCodeBlanksFeature.State.Content.activeCodeBlockIndex: Int?
    get() = codeBlocks.indexOfFirst { it.isActive }.takeIf { it != -1 }