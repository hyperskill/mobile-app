package org.hyperskill.app.study_plan.screen.view

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.problems_limit.view.mapper.ProblemsLimitViewStateMapper
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import org.hyperskill.app.study_plan.widget.view.mapper.StudyPlanWidgetViewStateMapper

internal class StudyPlanScreenViewStateMapper(
    private val problemsLimitViewStateMapper: ProblemsLimitViewStateMapper,
    private val studyPlanWidgetViewStateMapper: StudyPlanWidgetViewStateMapper,
    private val resourceProvider: ResourceProvider
) {
    fun map(state: StudyPlanScreenFeature.State): StudyPlanScreenFeature.ViewState =
        StudyPlanScreenFeature.ViewState(
            trackTitle = getTrackTitle(state),
            toolbarState = state.toolbarState,
            problemsLimitViewState = problemsLimitViewStateMapper.mapState(state.problemsLimitState),
            studyPlanWidgetViewState = studyPlanWidgetViewStateMapper.map(state.studyPlanWidgetState),
            isRefreshing = state.isRefreshing
        )

    private fun getTrackTitle(state: StudyPlanScreenFeature.State): String? =
        state.studyPlanWidgetState.profile?.trackTitle
            ?.takeIf { it.isNotBlank() }
            ?.let { title ->
                resourceProvider.getString(
                    SharedResources.strings.study_plan_track_title_template,
                    title
                )
            }
}