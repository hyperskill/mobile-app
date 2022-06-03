package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface StepQuizComponent {
    val stepQuizInteractor: StepQuizInteractor
    val stepQuizFeature: Feature<StepQuizFeature.State, StepQuizFeature.Message, StepQuizFeature.Action>
}