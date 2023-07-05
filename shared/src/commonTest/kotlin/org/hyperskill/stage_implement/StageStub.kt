package org.hyperskill.stage_implement

import org.hyperskill.app.stages.domain.model.Stage

fun Stage.Companion.stub(
    stepId: Long? = null
): Stage =
    Stage(
        id = 0,
        title = "",
        isCompleted = false,
        projectId = 0,
        projectStagesCount = 1,
        stepId = stepId,
        stepIndex = null
    )