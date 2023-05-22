package org.hyperskill.app.profile.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    @SerialName("id")
    val id: Long,
    @SerialName("avatar")
    val avatar: String,
    @SerialName("bio")
    val bio: String,
    @SerialName("fullname")
    val fullname: String,
    @SerialName("gamification")
    val gamification: Gamification = Gamification(),
    @SerialName("completed_tracks")
    val completedTracks: List<Long>,
    @SerialName("country")
    val country: String?,
    @SerialName("languages")
    val languages: List<String>?,
    @SerialName("experience")
    val experience: String,
    @SerialName("github_username")
    val githubUsername: String,
    @SerialName("linkedin_username")
    val linkedinUsername: String,
    @SerialName("twitter_username")
    val twitterUsername: String,
    @SerialName("reddit_username")
    val redditUsername: String,
    @SerialName("facebook_username")
    val facebookUsername: String,
    @SerialName("daily_step")
    val dailyStep: Long?,
    @SerialName("is_guest")
    val isGuest: Boolean,
    @SerialName("is_staff")
    val isStaff: Boolean,
    @SerialName("track_id")
    val trackId: Long?,
    @SerialName("track_title")
    val trackTitle: String?,
    @SerialName("is_beta")
    val isBeta: Boolean = false,
    @SerialName("features")
    private val featuresMap: Map<String, Boolean> = emptyMap()
) {
    companion object

    val features: FeaturesMap
        get() = FeaturesMap(featuresMap)
}

internal val Profile.isNewUser: Boolean
    get() = trackId == null

internal val Profile.isCurrentTrackCompleted: Boolean
    get() = trackId?.let { completedTracks.contains(it) } ?: false