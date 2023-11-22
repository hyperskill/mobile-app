package org.hyperskill.app.learning_activities.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection

@Serializable
class LearningActivitiesWithSectionsResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("learning-activities")
    val learningActivities: List<LearningActivity>,

    @SerialName("study-plan-sections")
    val studyPlanSections: List<StudyPlanSection>
) : MetaResponse