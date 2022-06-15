package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.view.mapper.StepQuizStatsTextMapper
import org.hyperskill.app.step_quiz.view.mapper.StepQuizTitleMapper
import ru.nobird.app.presentation.redux.feature.Feature

interface StepQuizComponent {
    val stepQuizStatsTextMapper: StepQuizStatsTextMapper
    val stepQuizTitleMapper: StepQuizTitleMapper
    val stepQuizInteractor: StepQuizInteractor
    val stepQuizFeature: Feature<StepQuizFeature.State, StepQuizFeature.Message, StepQuizFeature.Action>
}