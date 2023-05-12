package org.hyperskill.app.study_plan.screen.view

import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import org.hyperskill.app.study_plan.widget.view.StudyPlanWidgetViewState

data class StudyPlanScreenViewState(
    val trackTitle: String?,
    val toolbarState: GamificationToolbarFeature.State,
    val problemsLimitViewState: ProblemsLimitFeature.ViewState,
    val studyPlanWidgetViewState: StudyPlanWidgetViewState,
    val isRefreshing: Boolean
)