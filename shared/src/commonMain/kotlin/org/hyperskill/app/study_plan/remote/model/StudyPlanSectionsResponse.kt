package org.hyperskill.app.study_plan.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection

@Serializable
class StudyPlanSectionsResponse(
    @SerialName("meta")
    override val meta: Meta,
    @SerialName("study-plan-sections")
    val studyPlanSections: List<StudyPlanSection>
) : MetaResponse