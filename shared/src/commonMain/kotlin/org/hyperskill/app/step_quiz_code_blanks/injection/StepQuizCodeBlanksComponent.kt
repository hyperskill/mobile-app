package org.hyperskill.app.step_quiz_code_blanks.injection

import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksActionDispatcher
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksReducer

interface StepQuizCodeBlanksComponent {
    val stepQuizCodeBlanksReducer: StepQuizCodeBlanksReducer
    val stepQuizCodeBlanksActionDispatcher: StepQuizCodeBlanksActionDispatcher
}