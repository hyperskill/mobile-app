package org.hyperskill.app.track.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.progress_screen.domain.model.Progress
import org.hyperskill.app.progress_screen.domain.model.averageRating

@Serializable
data class TrackProgress(
    @SerialName("id")
    val id: String,
    @SerialName("vid")
    val vid: String,
    @SerialName("clarity")
    override val clarity: Float?,
    @SerialName("fun")
    override val funMeasure: Float?,
    @SerialName("usefulness")
    override val usefulness: Float?,
    @SerialName("completed_capstone_projects")
    val completedCapstoneProjects: List<Long> = emptyList(),
    @SerialName("applied_capstone_topics_count")
    val appliedCapstoneTopicsCount: Int = 0,
    @SerialName("is_completed")
    val isCompleted: Boolean,
    @SerialName("learned_topics_count")
    val learnedTopicsCount: Int = 0,
    @SerialName("skipped_topics_count")
    val skippedTopicsCount: Int = 0,
    @SerialName("rank")
    val rank: Int = 0
) : Progress {

    @Deprecated(
        "Use on iOS side only!",
        replaceWith = ReplaceWith(
            "averageRating()",
            "org.hyperskill.app.progress_screen.domain.model.averageRating"
        )
    )
    val averageRating: Double
        get() = averageRating()

    val completedTopics: Int
        get() = learnedTopicsCount + skippedTopicsCount
}

internal val TrackProgress.trackId: Long?
    get() = vid.substringAfter("track-").toLongOrNull()