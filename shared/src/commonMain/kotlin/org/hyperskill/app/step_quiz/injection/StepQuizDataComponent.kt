package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor

interface StepQuizDataComponent {
    val stepQuizInteractor: StepQuizInteractor
}