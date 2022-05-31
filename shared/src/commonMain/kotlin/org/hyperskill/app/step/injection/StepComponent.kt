package org.hyperskill.app.step.injection

import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step.view.mapper.CommentThreadTitleMapper
import org.hyperskill.app.step.view.mapper.StepQuizStatsTextMapper
import ru.nobird.app.presentation.redux.feature.Feature

interface StepComponent {
    val stepQuizStatsTextMapper: StepQuizStatsTextMapper
    val commentThreadTitleMapper: CommentThreadTitleMapper
    val stepFeature: Feature<StepFeature.State, StepFeature.Message, StepFeature.Action>
}