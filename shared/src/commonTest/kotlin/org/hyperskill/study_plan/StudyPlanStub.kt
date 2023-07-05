package org.hyperskill.study_plan

import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.study_plan.domain.model.StudyPlanStatus

fun StudyPlan.Companion.stub(
    id: Long = 0,
    trackId: Long? = null,
    projectId: Long? = null,
    status: StudyPlanStatus = StudyPlanStatus.READY,
    sections: List<Long> = emptyList()
): StudyPlan =
    StudyPlan(
        id = id,
        trackId = trackId,
        projectId = projectId,
        sections = sections,
        status = status,
        secondsToReachTrack = 0f,
        secondsToReachProject = 0f
    )