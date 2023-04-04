package org.hyperskill.app.study_plan.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.study_plan.domain.model.StudyPlan

@Serializable
class StudyPlanResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("study-plans")
    val studyPlans: List<StudyPlan>
) : MetaResponse