package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor

interface StepQuizComponent {
    val stepQuizInteractor: StepQuizInteractor
}