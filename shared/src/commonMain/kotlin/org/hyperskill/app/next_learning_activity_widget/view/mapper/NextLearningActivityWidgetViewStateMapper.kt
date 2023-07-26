package org.hyperskill.app.next_learning_activity_widget.view.mapper

import org.hyperskill.app.learning_activities.view.mapper.LearningActivityTextsMapper
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature

object NextLearningActivityWidgetViewStateMapper {
    fun map(state: NextLearningActivityWidgetFeature.ContentState): NextLearningActivityWidgetFeature.ViewState =
        when (state) {
            NextLearningActivityWidgetFeature.ContentState.Idle -> {
                NextLearningActivityWidgetFeature.ViewState.Idle
            }
            NextLearningActivityWidgetFeature.ContentState.Loading -> {
                NextLearningActivityWidgetFeature.ViewState.Loading
            }
            NextLearningActivityWidgetFeature.ContentState.NetworkError -> {
                NextLearningActivityWidgetFeature.ViewState.NetworkError
            }
            NextLearningActivityWidgetFeature.ContentState.Empty -> {
                NextLearningActivityWidgetFeature.ViewState.Empty
            }
            is NextLearningActivityWidgetFeature.ContentState.Content -> {
                NextLearningActivityWidgetFeature.ViewState.Content(
                    id = state.learningActivity.id,
                    title = LearningActivityTextsMapper.mapLearningActivityToTitle(state.learningActivity),
                    subtitle = LearningActivityTextsMapper.mapLearningActivityToSubtitle(state.learningActivity),
                    isIdeRequired = state.learningActivity.isIdeRequired,
                    progress = state.learningActivity.progressPercentage,
                    formattedProgress = LearningActivityTextsMapper.mapLearningActivityToProgressString(
                        state.learningActivity
                    )
                )
            }
        }
}