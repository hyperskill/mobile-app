package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.step.view.mapper.StepQuizStatsTextMapper
import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface StepQuizComponent {
    val stepQuizStatsTextMapper: StepQuizStatsTextMapper
    val stepQuizInteractor: StepQuizInteractor
    val stepQuizFeature: Feature<StepQuizFeature.State, StepQuizFeature.Message, StepQuizFeature.Action>
}