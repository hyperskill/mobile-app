package org.hyperskill.study_plan.domain.model

import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.model.StudyPlanSectionType

fun StudyPlanSection.Companion.stub(
    id: Long = 0,
    type: StudyPlanSectionType = StudyPlanSectionType.STAGE,
    nextActivityId: Long? = null,
    isVisible: Boolean = true,
    title: String = "title",
    subtitle: String = "subtitle",
    topicsCount: Int = 0,
    completedTopicsCount: Int = 0,
    secondsToComplete: Float = 100f,
    activities: List<Long> = emptyList()
): StudyPlanSection =
    StudyPlanSection(
        id = id,
        typeValue = type.value,
        nextActivityId = nextActivityId,
        isVisible = isVisible,
        title = title,
        subtitle = subtitle,
        topicsCount = topicsCount,
        completedTopicsCount = completedTopicsCount,
        secondsToComplete = secondsToComplete,
        activities = activities
    )