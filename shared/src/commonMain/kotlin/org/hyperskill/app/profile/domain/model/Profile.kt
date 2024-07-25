package org.hyperskill.app.profile.domain.model

import kotlinx.datetime.TimeZone
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.profile.cache.CurrentProfileStateHolderImpl
import org.hyperskill.app.subscriptions.domain.model.FreemiumChargeLimitsStrategy

/**
 * Represents a user profile.
 *
 * Warning!
 * This model is stored in the cache.
 * Adding new field or modifying old ones,
 * check that all fields will be deserialized from cache without an error.
 * All the new optional fields must have default values.
 * @see [CurrentProfileStateHolderImpl]
 */
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
    @SerialName("project")
    val projectId: Long? = null,
    @SerialName("is_beta")
    val isBeta: Boolean = false,
    @SerialName("timezone")
    val timeZone: TimeZone? = null,
    @SerialName("notification_hour")
    val notificationHour: Int? = null,
    @SerialName("feature_values")
    val featureValues: FeatureValues = FeatureValues(),
    @SerialName("features")
    private val featuresMap: Map<String, Boolean> = emptyMap(),
) {
    companion object

    val features: FeaturesMap
        get() = FeaturesMap(featuresMap)

    val isDailyLearningNotificationEnabled: Boolean
        get() = notificationHour != null
}

internal val Profile.isNewUser: Boolean
    get() = trackId == null

internal val Profile.isCurrentTrackCompleted: Boolean
    get() = trackId?.let { completedTracks.contains(it) } ?: false

internal val Profile.freemiumChargeLimitsStrategy: FreemiumChargeLimitsStrategy
    get() = if (features.isFreemiumWrongSubmissionChargeLimitsEnabled) {
        FreemiumChargeLimitsStrategy.AFTER_WRONG_SUBMISSION
    } else {
        FreemiumChargeLimitsStrategy.AFTER_CORRECT_SUBMISSION
    }

/**
 * A shortcut to copy [Profile] with updated hypercoinsBalance.
 */
internal fun Profile.copy(hypercoinsBalance: Int): Profile =
    copy(
        gamification = gamification.copy(
            hypercoinsBalance = hypercoinsBalance
        )
    )