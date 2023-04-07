package org.hyperskill.app.study_plan.screen.view

import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.study_plan.widget.view.StudyPlanWidgetViewState

data class StudyPlanScreenViewState(
    val trackTitle: String?,
    val toolbarState: GamificationToolbarFeature.State,
    val studyPlanWidgetViewState: StudyPlanWidgetViewState,
    val isRefreshing: Boolean
)