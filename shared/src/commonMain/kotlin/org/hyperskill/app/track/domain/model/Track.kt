package org.hyperskill.app.track.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Track(
    @SerialName("description")
    val description: String,
    @SerialName("is_beta")
    val isBeta: Boolean,
    @SerialName("is_free")
    val isFree: Boolean,
    @SerialName("id")
    val id: Long,
    @SerialName("is_completed")
    val isCompleted: Boolean,
    @SerialName("can_issue_certificate")
    val canIssueCertificate: Boolean,
    @SerialName("projects_by_level")
    val projectsByLevel: ProjectsByLevel,
    @SerialName("results")
    val results: String,
    @SerialName("seconds_to_complete")
    val secondsToComplete: Float?,
    @SerialName("title")
    val title: String,
    @SerialName("topics_count")
    val topicsCount: Int,
    @SerialName("cover")
    val cover: String?,
    @SerialName("careers")
    val careers: String,
    @SerialName("projects")
    val projects: List<Long>,
    @SerialName("beta_projects")
    val betaProjects: List<Long>,
    @SerialName("progress_id")
    val progressId: String,
    @SerialName("is_public")
    val isPublic: Boolean,
    @SerialName("capstone_projects")
    val capstoneProjects: List<Long>,
    @SerialName("capstone_topics_count")
    val capstoneTopicsCount: Int,
    @SerialName("provider_id")
    val providerId: Long? = null,
    @SerialName("topic_providers")
    val topicProviders: List<Long>,

    // TODO: replace with TrackWithProgress to use composition way
    @Deprecated("Use TrackWithProgress instead")
    @Transient
    val progress: TrackProgress? = null
) {
    companion object
}

/**
 * Return the total number of topics in the track: [Track.topicsCount] + [Track.capstoneTopicsCount]
 */
val Track.totalTopicsCount: Int
    get() = topicsCount + capstoneTopicsCount

fun Track.getAllProjects(isBetaProfile: Boolean): List<Long> =
    if (isBetaProfile) {
        projects.union(betaProjects).toList()
    } else {
        projects
    }