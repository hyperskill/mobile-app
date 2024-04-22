package org.hyperskill.app.study_plan.screen.view

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.gamification_toolbar.view.mapper.GamificationToolbarViewStateMapper
import org.hyperskill.app.problems_limit.presentation.LegacyProblemsLimitFeature
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import org.hyperskill.app.study_plan.widget.view.mapper.StudyPlanWidgetViewStateMapper

internal class StudyPlanScreenViewStateMapper(
    private val studyPlanWidgetViewStateMapper: StudyPlanWidgetViewStateMapper,
    private val resourceProvider: ResourceProvider
) {
    fun map(state: StudyPlanScreenFeature.State): StudyPlanScreenFeature.ViewState =
        StudyPlanScreenFeature.ViewState(
            trackTitle = getTrackTitle(state),
            toolbarViewState = GamificationToolbarViewStateMapper.map(state.toolbarState),
            problemsLimitViewState = LegacyProblemsLimitFeature.ViewState.Idle,
            usersInterviewWidgetState = state.usersInterviewWidgetState,
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