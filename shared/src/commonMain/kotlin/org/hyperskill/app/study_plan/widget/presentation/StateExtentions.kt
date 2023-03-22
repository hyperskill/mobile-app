package org.hyperskill.app.study_plan.widget.presentation

import org.hyperskill.app.study_plan.domain.model.StudyPlanSection

internal fun StudyPlanWidgetFeature.State.firstSection(): StudyPlanSection? =
    studyPlanSections.values.firstOrNull()?.studyPlanSection