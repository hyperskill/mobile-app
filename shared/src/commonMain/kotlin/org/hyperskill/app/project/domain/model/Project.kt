package org.hyperskill.app.project.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    @SerialName("id")
    val id: Long,
    @SerialName("description")
    val description: String,
    @SerialName("environment")
    val environment: String,
    @SerialName("is_beta")
    val isBeta: Boolean,
    @SerialName("is_template_based")
    val isTemplateBased: Boolean,
    @SerialName("language")
    val language: String,
    @SerialName("lesson_stepik_id")
    val lessonStepikId: Long,
    @SerialName("n_first_prerequisites")
    val nFirstPrerequisites: Int,
    @SerialName("n_last_prerequisites")
    val nLastPrerequisites: Int,
    @SerialName("preview_step")
    val previewStep: Long?,
    @SerialName("default_score")
    val defaultScore: Float,
    @SerialName("results")
    val results: String,
    @SerialName("stages_count")
    val stagesCount: Int,
    @SerialName("stages_ids")
    val stagesIds: List<Long>,
    @SerialName("title")
    val title: String,
    @SerialName("tracks")
    val tracks: Map<String, Track>,
    @SerialName("use_ide")
    val useIDE: Boolean,
    @SerialName("is_deprecated")
    val isDeprecated: Boolean,
    @SerialName("progress_id")
    val progressId: String,
    @SerialName("readiness")
    val readiness: Int,
    @SerialName("is_public")
    val isPublic: Boolean,
    @SerialName("provider_id")
    val providerId: Long
)
