package org.hyperskill.app.step_quiz_hints.injection

import org.hyperskill.app.step_quiz_hints.domain.interactor.StepQuizHintsInteractor

interface StepQuizHintsComponent {
    val stepQuizHintsInteractor: StepQuizHintsInteractor
}